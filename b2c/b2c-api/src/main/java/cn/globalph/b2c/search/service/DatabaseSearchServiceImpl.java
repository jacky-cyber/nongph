package cn.globalph.b2c.search.service;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.search.dao.FieldDao;
import cn.globalph.b2c.search.dao.SearchFacetDao;
import cn.globalph.b2c.search.domain.*;
import cn.globalph.common.exception.ServiceException;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;

@Service("blSearchService")
public class DatabaseSearchServiceImpl implements SearchService {
    
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;
    
    @Resource(name = "blSearchFacetDao")
    protected SearchFacetDao searchFacetDao;
    
    @Resource(name = "blFieldDao")
    protected FieldDao fieldDao;
    
    protected static String CACHE_NAME = "blStandardElements";
    protected static String CACHE_KEY_PREFIX = "facet:";
    protected Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
    
    @Override
    public SkuSearchResult findExplicitSkusByCategory(Category category, SkuSearchCriteria searchCriteria) throws ServiceException {
        throw new UnsupportedOperationException("See findProductsByCategory or use the SolrSearchService implementation");
    }
    
    @Override
    public SkuSearchResult findProductsByCategoryAndQuery(Category category, String query, SkuSearchCriteria searchCriteria) throws ServiceException {
        throw new UnsupportedOperationException("This operation is only supported by the SolrSearchService by default");
    }
    
    @Override
    public SkuSearchResult findSkusByCategory(Category category, SkuSearchCriteria searchCriteria) {
        SkuSearchResult result = new SkuSearchResult();
        setQualifiedKeys( searchCriteria );
        List<Sku> skus = catalogService.findActiveSkusByCategory(category, searchCriteria);
        List<SearchFacetDTO> facets = getCategoryFacets(category);
        setActiveFacets(facets, searchCriteria);
//        result.setSkus( skus );
//        result.setFacets(facets);
//        result.setTotalResults( skus.size() );
//        result.setPage(1);
//        result.setPageSize( skus.size() );
//        return result;
        result.setSkus(skus);
        result.setFacets(facets);
        result.setTotalResults( searchCriteria.getTotalPage());
        result.setPage(searchCriteria.getPage());
        result.setPageSize(searchCriteria.getPageSize());
        return result;
    }

    @Override
    public SkuSearchResult findSkusByQuery(String query, SkuSearchCriteria searchCriteria) {
        SkuSearchResult result = new SkuSearchResult();
        setQualifiedKeys(searchCriteria);
        query = query.replaceAll("%", "\\\\%");
        query = query.replaceAll("_", "\\\\_");
        List<Sku> skus = catalogService.findActiveSkusByQuery(query, searchCriteria);
        List<SearchFacetDTO> facets = getSearchFacets();
        setActiveFacets(facets, searchCriteria);
        result.setSkus(skus);
        result.setFacets(facets);
        result.setTotalResults( searchCriteria.getTotalPage());
        result.setPage(searchCriteria.getPage());
        result.setPageSize(searchCriteria.getPageSize());
        return result;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<SearchFacetDTO> getSearchFacets() {
        List<SearchFacetDTO> facets = null;
        
        String cacheKey = CACHE_KEY_PREFIX + "blc-search";
        Element element = cache.get(cacheKey);
        if (element != null) {
            facets = (List<SearchFacetDTO>) element.getValue();
        }
        
        if (facets == null) {
            facets = buildSearchFacetDtos(searchFacetDao.readAllSearchFacets(),null);
            element = new Element(cacheKey, facets);
            cache.put(element);
        }
        return facets;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<SearchFacetDTO> getCategoryFacets(Category category) {
        List<SearchFacetDTO> facets = null;
        
        String cacheKey = CACHE_KEY_PREFIX + "category:" + category.getId();
        Element element = cache.get(cacheKey);
        if (element != null) {
            facets = (List<SearchFacetDTO>) element.getValue();
        }
        
        if (facets == null) {
            List<CategorySearchFacet> categorySearchFacets = category.getCumulativeSearchFacets();
            List<SearchFacet> searchFacets = new ArrayList<SearchFacet>();
            List<List<CategorySearchFacetValue>> values = new ArrayList<List<CategorySearchFacetValue>>();
            for (CategorySearchFacet categorySearchFacet : categorySearchFacets) {
                searchFacets.add(categorySearchFacet.getSearchFacet());
                values.add(categorySearchFacet.getCategorySearchFacetValues());
            }
            facets = buildSearchFacetDtos(searchFacets,values);
            element = new Element(cacheKey, facets);
            cache.put(element);
        }
        return facets;
    }
    
    /**
     * Perform any necessary conversion of the key to be used by the search service
     * @param criteria
     */
    protected void setQualifiedKeys(SkuSearchCriteria criteria) {
        // Convert the filter criteria url keys
        Map<String, String[]> convertedFilterCriteria = new HashMap<String, String[]>();
        for (Entry<String, String[]> entry : criteria.getFilterCriteria().entrySet()) {
            Field field = fieldDao.readFieldByAbbreviation(entry.getKey());
            if (field != null) {
                String qualifiedFieldName = getDatabaseQualifiedFieldName(field.getQualifiedFieldName());
                convertedFilterCriteria.put(qualifiedFieldName, entry.getValue());
            }
        }
        criteria.setFilterCriteria(convertedFilterCriteria);
        
        // Convert the sort criteria url keys
        if (StringUtils.isNotBlank(criteria.getSortQuery())) {
            StringBuilder convertedSortQuery = new StringBuilder();
            for (String sortQuery : criteria.getSortQuery().split(",")) {
                String[] sort = sortQuery.split(" ");
                if (sort.length == 2) {
                    String key = sort[0];
                    Field field = fieldDao.readFieldByAbbreviation(key);
                    if (field != null) {
                        String qualifiedFieldName = getDatabaseQualifiedFieldName(field.getQualifiedFieldName());

                        if (convertedSortQuery.length() > 0) {
                            convertedSortQuery.append(",");
                        }

                        convertedSortQuery.append(qualifiedFieldName).append(" ").append(sort[1]);
                    }
                }
            }
            criteria.setSortQuery(convertedSortQuery.toString());
        }
        
    }
    
    /**
     * From the Field's qualifiedName, build out the qualified name to be used by the ProductDao
     * to find the requested products.
     * 
     * @param qualifiedFieldName
     * @return the database qualified name
     */
    protected String getDatabaseQualifiedFieldName(String qualifiedFieldName) {
        if (qualifiedFieldName.contains("productAttributes")) {
            return qualifiedFieldName.replace("product.", "");
        } else if (qualifiedFieldName.contains("defaultSku")) {
            return qualifiedFieldName.replace("product.", "");
        } else {
            return qualifiedFieldName;
        }
    }
    
    
    protected void setActiveFacets(List<SearchFacetDTO> facets, SkuSearchCriteria searchCriteria) {
        for (SearchFacetDTO facet : facets) {
            String qualifiedFieldName = getDatabaseQualifiedFieldName(facet.getFacet().getField().getQualifiedFieldName());
            for (Entry<String, String[]> entry : searchCriteria.getFilterCriteria().entrySet()) {
                if (qualifiedFieldName.equals(entry.getKey())) {
                    facet.setActive(true);
                }
            }
        }
    }
    
    /**
     * Create the wrapper DTO around the SearchFacet
     * @param categoryFacets
     * @return the wrapper DTO
     */
    protected List<SearchFacetDTO> buildSearchFacetDtos(List<SearchFacet> categoryFacets,List<List<CategorySearchFacetValue>> values) {
        List<SearchFacetDTO> facets = new ArrayList<SearchFacetDTO>();
        
        for (int i = 0;i<categoryFacets.size();i++) {
            SearchFacetDTO dto = new SearchFacetDTO();
            dto.setFacet(categoryFacets.get(i));
            dto.setShowQuantity(false);
            dto.setFacetValues(getFacetValues(categoryFacets.get(i),(values!=null&&values.size()!=0?values.get(i):null)));
            dto.setActive(false);
            facets.add(dto);
        }
        
        return facets;
    }
    
    protected List<SearchFacetResultDTO> getFacetValues(SearchFacet facet,List<CategorySearchFacetValue> facetValues) {
    	if (facetValues!=null&&facetValues.size()>0){
    		List<SearchFacetResultDTO> results = getValues(facet,facetValues);
    		if(results!=null&&results.size()>0) return results;
    	}
        if (facet.getSearchFacetRanges().size() > 0) {
            return getRangeFacetValues(facet);
        }
        else {
            return getMatchFacetValues(facet);
        }
    }
    
    protected List<SearchFacetResultDTO>  getValues(SearchFacet facet,List<CategorySearchFacetValue> facetValues){
    	List<String> ls = new ArrayList<String>();
    	for(CategorySearchFacetValue facetValue : facetValues){
    		if(facetValue.getSearchFieldId().equals(facet.getField().getId())){
    			ls.add(facetValue.getValue());
    		}
    	}
    	List<SearchFacetResultDTO> results = new ArrayList<SearchFacetResultDTO>();
        Collections.sort(ls);
        for (String value : ls) {
            SearchFacetResultDTO dto = new SearchFacetResultDTO();
            dto.setValue(value);
            dto.setFacet(facet);
            results.add(dto);
        }
        
        return results;
    }
    
    protected List<SearchFacetResultDTO> getRangeFacetValues(SearchFacet facet) {
        List<SearchFacetResultDTO> results = new ArrayList<SearchFacetResultDTO>();
        
        List<SearchFacetRange> ranges = facet.getSearchFacetRanges();
        Collections.sort(ranges, new Comparator<SearchFacetRange>() {
            public int compare(SearchFacetRange o1, SearchFacetRange o2) {
                return o1.getMinValue().compareTo(o2.getMinValue());
            }
        });
        
        for (SearchFacetRange range : ranges) {
            SearchFacetResultDTO dto = new SearchFacetResultDTO();
            dto.setMinValue(range.getMinValue());
            dto.setMaxValue(range.getMaxValue());
            dto.setFacet(facet);
            results.add(dto);
        }
        return results;
    }
    
    protected List<SearchFacetResultDTO> getMatchFacetValues(SearchFacet facet) {
        List<SearchFacetResultDTO> results = new ArrayList<SearchFacetResultDTO>();
        
        String qualifiedFieldName = facet.getField().getQualifiedFieldName();
        qualifiedFieldName = getDatabaseQualifiedFieldName(qualifiedFieldName);
        List<String> values = searchFacetDao.readDistinctValuesForField(qualifiedFieldName, String.class);
        
        Collections.sort(values);
        
        for (String value : values) {
            SearchFacetResultDTO dto = new SearchFacetResultDTO();
            dto.setValue(value);
            dto.setFacet(facet);
            results.add(dto);
        }
        
        return results;
    }
    
    @Override
    public void rebuildIndex() {
        throw new UnsupportedOperationException("Indexes are not supported by this implementation");
    }

}
