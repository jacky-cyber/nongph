
package cn.globalph.b2c.web.processor;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.web.service.SimpleCacheKeyResolver;
import cn.globalph.b2c.web.service.TemplateCacheKeyResolverService;
import cn.globalph.common.config.service.SystemPropertiesService;
import cn.globalph.common.web.WebRequestContext;

import org.springframework.web.context.request.WebRequest;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.standard.processor.attr.StandardFragmentAttrProcessor;

import java.util.Set;

import javax.annotation.Resource;

/**
 * Allows for a customizable cache mechanism that can be used to avoid expensive thymeleaf processing for 
 * html fragments that are static.   
 * 
 * For high volume sites, even a 30 second cache of pages can have significant overall performance impacts.
 * 
 * The parameters allowed for this processor include a "cacheTimeout" and "cacheKey".    This component will rely on
 * an implementation of {@link TemplateCacheKeyResolverService} to build the actual cacheKey used by the underlying
 * caching implementation.   The parameter named "cacheKey" will be used in the construction of the actual cacheKey
 * which may rely on variables like the customer, site, theme, etc. to build the actual cacheKey.
 * 
 * The "cacheTimeout" variable defines the maximum length of time that the fragment will be allowed to be cached.   This
 * is important for fragments for which a good cacheKey would be difficult to generate.
 * 
 * The cacheKey resolution  is pluggable using an instance of {@link TemplateCacheKeyResolverService} which will be 
 * invoked by this.   The default implementation is {@link SimpleCacheKeyResolver}.   
 * 
 * Implementors can create more functional cacheKey mechanisms.   For example, Broadleaf Enterprise provides an 
 * additional implementation named <code>EnterpriseCacheKeyResolver</code> with support for additional caching 
 * features.
 *  
 * @author bpolster
 */
public class BroadleafCacheProcessor extends AbstractAttrProcessor {

    private static final Log LOG = LogFactory.getLog(BroadleafCacheProcessor.class);

    public static final String ATTR_NAME = "cache";

    protected Cache cache;

    @Resource(name = "blSystemPropertiesService")
    protected SystemPropertiesService systemPropertiesService;

    @Resource(name = "blTemplateCacheKeyResolver")
    protected TemplateCacheKeyResolverService cacheKeyResolver;

    public BroadleafCacheProcessor() {
        super(ATTR_NAME);
    }

    public void fixElement(Element element, Arguments arguments) {
        boolean elementAdded = false;
        boolean removeElement = false;
        Set<String> attributeNames = element.getAttributeMap().keySet();

        for (String a : attributeNames) {
            String attrName = a.toLowerCase();
            if (attrName.startsWith("th")) {
                if (attrName.equals("th:substituteby") || (attrName.equals("th:replace") || attrName.equals("th:include"))) {
                    if (!elementAdded) {
                        Element extraDiv = new Element("div");
                        String attrValue = element.getAttributeValue(attrName);
                        element.removeAttribute(attrName);
                        extraDiv.setAttribute(attrName, attrValue);
                        element.addChild(extraDiv);
                        elementAdded = true;
                        element.setNodeProperty("templateName", attrValue);

                        // This will ensure that the substituteby and replace processors only run for the child element
                        element.setRecomputeProcessorsImmediately(true);
                    }
                } else if (attrName.equals("th:remove")) {
                    Attribute attr = element.getAttributeMap().get(attrName);
                    if ("tag".equals(attr.getValue())) {
                        removeElement = true;

                        // The cache functionality will remove the element. 
                        element.setAttribute(attrName, "none");
                    }
                }
            }
        }

        if (!elementAdded || removeElement) {
            element.setNodeProperty("blcOutputParentNode", Boolean.TRUE);
        }
    }

    protected boolean shouldCache(Arguments args, Element element, String attributeName) {
        String cacheAttrValue = element.getAttributeValue(attributeName);
        element.removeAttribute(attributeName);

        if (StringUtils.isEmpty(cacheAttrValue)) {
            return false;
        }

        cacheAttrValue = cacheAttrValue.toLowerCase();
        if (!isCachingEnabled() || "false".equals(cacheAttrValue)) {
            return false;
        } else if ("true".equals(cacheAttrValue)) {
            return true;
        }

        // Check for an expression
        Expression expression = (Expression) StandardExpressions.getExpressionParser(args.getConfiguration())
                .parseExpression(args.getConfiguration(), args, cacheAttrValue);
        Object o = expression.execute(args.getConfiguration(), args);
        if (o instanceof Boolean) {
            return (Boolean) o;
        } else if (o instanceof String) {
            cacheAttrValue = (String) o;
            cacheAttrValue = cacheAttrValue.toLowerCase();
            return "true".equals(cacheAttrValue);
        }
        return false;
    }

    @Override
    public ProcessorResult processAttribute(final Arguments arguments, final Element element, String attributeName) {
        if (shouldCache(arguments, element, attributeName)) {
            fixElement(element, arguments);
            if (checkCacheForElement(arguments, element)) {
                // This template has been cached.
                element.clearChildren();
                element.clearAttributes();
                element.setRecomputeProcessorsImmediately(true);
            }
        }
        return ProcessorResult.OK;
    }

    /**
     * If this template was found in cache, adds the response to the element and returns true.
     * 
     * If not found in cache, adds the cacheKey to the element so that the Writer can cache after the
     * first process.
     * 
     * @param arguments
     * @param element
     * @return
     */
    protected boolean checkCacheForElement(Arguments arguments, Element element) {

        if (isCachingEnabled()) {
            String cacheKey = cacheKeyResolver.resolveCacheKey(arguments, element);
    
            if (!StringUtils.isEmpty(cacheKey)) {
                element.setNodeProperty("cacheKey", cacheKey);
    
                net.sf.ehcache.Element cacheElement = getCache().get(cacheKey);
                if (cacheElement != null && !checkExpired(element, cacheElement)) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Template Cache Hit with cacheKey " + cacheKey + " found in cache.");
                    }
                    element.setNodeProperty("blCacheResponse", (String) cacheElement.getObjectValue());
                    return true;
                } else {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Template Cache Miss with cacheKey " + cacheKey + " not found in cache.");
                    }
                }
            } else {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Template not cached due to empty cacheKey");
                }
            }
        } else {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Template caching disabled - not retrieving template from cache");
            }
        }
        return false;
    }

    /**
     * Returns true if the item has been 
     * @param element
     * @param cacheElement
     * @return
     */
    protected boolean checkExpired(Element element, net.sf.ehcache.Element cacheElement) {
        if (cacheElement.isExpired()) {
            return true;
        } else {
            String cacheTimeout = element.getAttributeValue("cacheTimeout");
            if (!StringUtils.isEmpty(cacheTimeout) && StringUtils.isNumeric(cacheTimeout)) {
                Long timeout = Long.valueOf(cacheTimeout) * 1000;
                Long expiryTime = cacheElement.getCreationTime() + timeout;
                if (expiryTime < System.currentTimeMillis()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String getFragmentSignatureUnprefixedAttributeName(final Arguments arguments, final Element element,
            final String attributeName, final String attributeValue) {
        return StandardFragmentAttrProcessor.ATTR_NAME;
    }

    @Override
    public int getPrecedence() {
        return Integer.MIN_VALUE;
    }

    public Cache getCache() {
        if (cache == null) {
            cache = CacheManager.getInstance().getCache("blTemplateElements");
        }
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public boolean isCachingEnabled() {
        boolean disabled = !systemPropertiesService.resolveBooleanSystemProperty("disableThymeleafTemplateCaching");
        if (!disabled) {
            // check for a URL param that overrides caching - useful for testing if this processor is incorrectly
            // caching a page (possibly due to an bad cacheKey).

            WebRequestContext brc = WebRequestContext.getWebRequestContext();
            if (brc != null && brc.getWebRequest() != null) {
                WebRequest request = brc.getWebRequest();
                String disableCachingParam = request.getParameter("disableThymeleafTemplateCaching");
                if ("true".equals(disableCachingParam)) {
                    return false;
                }
            }
        }
        return disabled;
    }
}
