package cn.globalph.b2c.web.processor;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.b2c.web.util.ProcessorUtils;
import cn.globalph.common.web.WebRequestContext;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Thymeleaf Processor that replaces the "href" attribute on an <a/> element, maintaining the current search criteria
 * of the request and adding (or replacing, if it exists) the sort parameter on the request.
 *
 * @author Joseph Fridye (jfridye)
 */
public class PaginationSortLinkProcessor extends AbstractAttributeModifierAttrProcessor {

    public PaginationSortLinkProcessor() {
        super("pagination-sort-link");
    }

    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    protected Map<String, String> getModifiedAttributeValues(Arguments arguments, Element element, String attributeName) {

        Map<String, String> attributes = new HashMap<String, String>();

        HttpServletRequest request = WebRequestContext.getWebRequestContext().getRequest();

        String baseUrl = request.getRequestURL().toString();

        Map<String, String[]> params = new HashMap<String, String[]>(request.getParameterMap());

        String sort = element.getAttributeValue(attributeName);

        if (StringUtils.isNotBlank(sort)) {
            params.put(SkuSearchCriteria.SORT_STRING, new String[]{sort});
        } else {
            params.remove(SkuSearchCriteria.SORT_STRING);
        }

        // If there is a page number parameter, remove it. This ensures that when the search results refresh the
        // first page of results will be displayed.
        params.remove(SkuSearchCriteria.PAGE_NUMBER);

        String url = ProcessorUtils.getUrl(baseUrl, params);

        attributes.put("href", url);

        return attributes;

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
