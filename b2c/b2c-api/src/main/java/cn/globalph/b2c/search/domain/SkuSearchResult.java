package cn.globalph.b2c.search.domain;

import cn.globalph.b2c.product.domain.Sku;

import java.util.List;

/**
 * Container that holds the result of a SKU search
 * 
 * @author felix.wu
 */
public class SkuSearchResult {
    
    protected List<Sku> skus;
    protected List<SearchFacetDTO> facets;
    
    protected Integer totalResults;
    protected Integer page;
    protected Integer pageSize;

    public List<Sku> getSkus() {
		return skus;
	}

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}

	public List<SearchFacetDTO> getFacets() {
        return facets;
    }

    public void setFacets(List<SearchFacetDTO> facets) {
        this.facets = facets;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    public Integer getStartResult() {
        return (skus == null || skus.size() == 0) ? 0 : ((page - 1) * pageSize) + 1;
    }
    
    public Integer getEndResult() {
        return Math.min(page * pageSize, totalResults);
    }
    
    public Integer getTotalPages() {
        return (skus == null || skus.size() == 0) ? 1 : (int) Math.ceil(totalResults * 1.0 / pageSize);
    }

}
