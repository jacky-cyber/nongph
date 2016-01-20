package cn.globalph.b2c.web.processor;

import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.b2c.web.util.ProcessorUtils;
import cn.globalph.common.web.WebRequestContext;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * A Thymeleaf processor that generates a search query link that will reflect the current 
 * search criteria in addition to the requested sort string
 * 
 * @author apazzolini
 */
public class AddSortLinkProcessor extends AbstractAttributeModifierAttrProcessor {
    
    protected boolean allowMultipleSorts = false;
    
    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public AddSortLinkProcessor() {
        super("addsortlink");
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
        
        String key = SkuSearchCriteria.SORT_STRING;
        String sortField = element.getAttributeValue(attributeName);
        
        List<String[]> sortedFields = new ArrayList<String[]>();
        
        String[] paramValues = params.get(key);
        if (paramValues != null && paramValues.length > 0) {
            String sortQueries = paramValues[0];
            for (String sortQuery : sortQueries.split(",")) {
                String[] sort = sortQuery.split(" ");
                if (sort.length == 2) {
                    sortedFields.add(new String[] { sort[0], sort[1] });
                }
            }
        }
        
        boolean currentlySortingOnThisField = false;
        boolean currentlyAscendingOnThisField = false;
        
        for (String[] sortedField : sortedFields) {
            if (sortField.equals(sortedField[0])) {
                currentlySortingOnThisField = true;
                currentlyAscendingOnThisField = sortedField[1].equals("asc");
                sortedField[1] = currentlyAscendingOnThisField ? "desc" : "asc";
            }
        }
        
        String sortString = sortField;
        String classString = "";
        
        if (currentlySortingOnThisField) {
            classString += "active ";
            if (currentlyAscendingOnThisField) {
                sortString += " desc";
                classString += "asc ";
            } else {
                sortString += " asc";
                classString += "desc ";
            }
        } else {
            sortString += " asc";
            classString += "asc ";
            params.remove(SkuSearchCriteria.PAGE_NUMBER);
        }
        
        if (allowMultipleSorts) {
            StringBuilder sortSb = new StringBuilder();
            for (String[] sortedField : sortedFields) {
                sortSb.append(sortedField[0]).append(" ").append(sortedField[1]).append(",");
            }
            
            sortString = sortSb.toString();
            if (sortString.charAt(sortString.length() - 1) == ',') {
                sortString = sortString.substring(0, sortString.length() - 1);
            }
        }
        
        params.put(key, new String[] { sortString } );
        
        String url = ProcessorUtils.getUrl(baseUrl, params);
        
        attrs.put("class", classString);
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
