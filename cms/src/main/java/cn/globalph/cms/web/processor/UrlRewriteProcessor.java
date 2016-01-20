package cn.globalph.cms.web.processor;

import cn.globalph.cms.file.service.StaticAssetService;
import cn.globalph.common.storage.service.StaticAssetPathService;
import cn.globalph.common.web.WebRequestContext;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * A Thymeleaf processor that processes the given url through the StaticAssetService's
 * {@link StaticAssetService#convertAssetPath(String, String, boolean)} method to determine
 * the appropriate URL for the asset to be served from.
 * 
 */
public class UrlRewriteProcessor extends AbstractAttributeModifierAttrProcessor {
    
    @Resource(name = "blStaticAssetPathService")
    protected StaticAssetPathService staticAssetPathService;

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public UrlRewriteProcessor() {
        super("src");
    }
    
    @Override
    public int getPrecedence() {
        return 1000;
    }
    
    /**
     * @return true if the current request.scheme = HTTPS or if the request.isSecure value is true.
     */
    protected boolean isRequestSecure(HttpServletRequest request) {
        return ("HTTPS".equalsIgnoreCase(request.getScheme()) || request.isSecure());
    } 

    
    @Override
    protected Map<String, String> getModifiedAttributeValues(Arguments arguments, Element element, String attributeName) {
        Map<String, String> attrs = new HashMap<String, String>();
        HttpServletRequest request = WebRequestContext.getWebRequestContext().getRequest();
        
        boolean secureRequest = isRequestSecure(request);
        
        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValue(attributeName));
        String assetPath = (String) expression.execute(arguments.getConfiguration(), arguments);
        
        assetPath = staticAssetPathService.convertAssetPath(assetPath, request.getContextPath(), secureRequest);
        
        attrs.put("src", assetPath);
        
        return attrs;
    }

    @Override
    protected ModificationType getModificationType(Arguments arguments, Element element, String attributeName, String newAttributeName) {
        return ModificationType.SUBSTITUTION;
    }

    @Override
    protected boolean removeAttributeIfEmpty(Arguments arguments, Element element, String attributeName, String newAttributeName) {
        return true;
    }

    @Override
    protected boolean recomputeProcessorsAfterExecution(Arguments arguments, Element element, String attributeName) {
        return false;
    }
}
