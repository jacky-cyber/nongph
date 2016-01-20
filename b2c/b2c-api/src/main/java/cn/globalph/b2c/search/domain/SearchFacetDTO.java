package cn.globalph.b2c.search.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author felix.wu
 */
public class SearchFacetDTO implements Serializable{
    
	private static final long serialVersionUID = 1L;
	
	protected SearchFacet facet;
    protected boolean showQuantity;
    protected List<SearchFacetResultDTO> facetValues = new ArrayList<SearchFacetResultDTO>();
    protected boolean active;
    
    public SearchFacet getFacet() {
        return facet;
    }
    
    public void setFacet(SearchFacet facet) {
        this.facet = facet;
    }
    
    public boolean isShowQuantity() {
        return showQuantity;
    }
    
    public void setShowQuantity(boolean showQuantity) {
        this.showQuantity = showQuantity;
    }
    
    public List<SearchFacetResultDTO> getFacetValues() {
        return facetValues;
    }
    
    public void setFacetValues(List<SearchFacetResultDTO> facetValues) {
        this.facetValues = facetValues;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
}
