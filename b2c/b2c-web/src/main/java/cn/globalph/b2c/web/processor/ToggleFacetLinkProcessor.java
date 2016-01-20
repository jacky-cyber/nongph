package cn.globalph.b2c.web.processor;

import org.apache.commons.lang.ArrayUtils;

import cn.globalph.b2c.search.domain.SearchFacetResultDTO;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.b2c.search.service.SearchFacetDTOService;
import cn.globalph.b2c.web.util.ProcessorUtils;
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
 * A Thymeleaf processor that processes the value attribute on the element it's tied to
 * with a predetermined value based on the SearchFacetResultDTO object that is passed into this
 * processor. 
 * 
 */
public class ToggleFacetLinkProcessor extends AbstractAttributeModifierAttrProcessor {
    
    @Resource(name = "blSearchFacetDTOService")
    protected SearchFacetDTOService facetService;

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public ToggleFacetLinkProcessor() {
        super("togglefacetlink");
    }
    
    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map<String, String> getModifiedAttributeValues(Arguments arguments, Element element, String attributeName) {
        Map<String, String> attrs = new HashMap<String, String>();
        
        WebRequestContext blcContext = WebRequestContext.getWebRequestContext();
        HttpServletRequest request = blcContext.getRequest();
        
        String baseUrl = request.getRequestURL().toString();
        Map<String, String[]> params = new HashMap<String, String[]>(request.getParameterMap());
        
        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValue(attributeName));
        SearchFacetResultDTO result = (SearchFacetResultDTO) expression.execute(arguments.getConfiguration(), arguments);
        
        String key = facetService.getUrlKey(result);
        String value = facetService.getValue(result);
        String[] paramValues = params.get(key);
        
        if (ArrayUtils.contains(paramValues, facetService.getValue(result))) {
            paramValues = (String[]) ArrayUtils.removeElement(paramValues, facetService.getValue(result));
        } else {
            paramValues = (String[]) ArrayUtils.add(paramValues, value);
        }
        
        params.remove(SkuSearchCriteria.PAGE_NUMBER);
        params.put(key, paramValues);
        
        String url = ProcessorUtils.getUrl(baseUrl, params);
        
        attrs.put("href", url);
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
