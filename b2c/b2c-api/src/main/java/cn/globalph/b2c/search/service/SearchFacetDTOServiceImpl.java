package cn.globalph.b2c.search.service;

import org.apache.commons.lang.StringUtils;

import cn.globalph.b2c.search.domain.SearchFacetDTO;
import cn.globalph.b2c.search.domain.SearchFacetResultDTO;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.common.util.BLCSystemProperty;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

@Service("blSearchFacetDTOService")
public class SearchFacetDTOServiceImpl implements SearchFacetDTOService {
    
    protected int getDefaultPageSize() {
        return BLCSystemProperty.resolveIntSystemProperty("web.defaultPageSize");
    }

    protected int getMaxPageSize() {
        return BLCSystemProperty.resolveIntSystemProperty("web.maxPageSize");
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public SkuSearchCriteria buildSearchCriteria(HttpServletRequest request) {
        SkuSearchCriteria searchCriteria = new SkuSearchCriteria();
        searchCriteria.setPageSize(getDefaultPageSize());
        
        Map<String, String[]> facets = new HashMap<String, String[]>();
        
        for (Iterator<Entry<String,String[]>> iter = request.getParameterMap().entrySet().iterator(); iter.hasNext();){
            Map.Entry<String, String[]> entry = iter.next();
            String key = entry.getKey();
            
            if (key.equals(SkuSearchCriteria.SORT_STRING)) {
                searchCriteria.setSortQuery(StringUtils.join(entry.getValue(), ","));
            } else if (key.equals(SkuSearchCriteria.PAGE_NUMBER)) {
                searchCriteria.setPage(Integer.parseInt(entry.getValue()[0]));
            } else if (key.equals(SkuSearchCriteria.PAGE_SIZE_STRING)) {
                int requestedPageSize = Integer.parseInt(entry.getValue()[0]);
                int maxPageSize = getMaxPageSize();
                searchCriteria.setPageSize(Math.min(requestedPageSize, maxPageSize));
            } else if (key.equals(SkuSearchCriteria.QUERY_STRING)) {
                continue; // This is handled by the controller
            } else {
                facets.put(key, entry.getValue());
            }
        }
        
        searchCriteria.setFilterCriteria(facets);
        
        return searchCriteria;
    }
    
    @Override
    public void setActiveFacetResults(List<SearchFacetDTO> facets, HttpServletRequest request) {
        if (facets != null) {
            for (SearchFacetDTO facet : facets) {
                for (SearchFacetResultDTO facetResult : facet.getFacetValues()) {
                    facetResult.setActive(isActive(facetResult, request));
                }
            }
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean isActive(SearchFacetResultDTO result, HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        for (Entry<String, String[]> entry : params.entrySet()) {
            String key = entry.getKey();
            if (key.equals(getUrlKey(result))) {
                for (String val : entry.getValue()) {
                    if (val.equals(getValue(result))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public String getUrlKey(SearchFacetResultDTO result) {
        return result.getFacet().getField().getAbbreviation();
    }
    
    @Override
    public String getValue(SearchFacetResultDTO result) {
        return result.getValueKey();
    }

}
