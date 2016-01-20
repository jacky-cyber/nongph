package cn.globalph.b2c.search.domain;

import cn.globalph.b2c.catalog.domain.Category;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Andre Azzolini (apazzolini)
 */
public interface CategorySearchFacet extends Serializable {

    /**
     * Gets the internal id
     * 
     * @return the internal id
     */
    public Long getId();

    /** 
     * Sets the internal id
     * 
     * @param id
     */
    public void setId(Long id);

    /**
     * Gets the associated category
     * 
     * @return the associated category
     */
    public Category getCategory();

    /**
     * Sets the associated category
     * 
     * @param category
     */
    public void setCategory(Category category);

    /**
     * Gets the associated search facet
     * 
     * @return the associated search facet
     */
    public SearchFacet getSearchFacet();

    /**
     * Sets the associated search facet
     * 
     * @param searchFacet
     */
    public void setSearchFacet(SearchFacet searchFacet);

    /**
     * Gets the priority of this search facet in relationship to other search facets in this category
     * 
     * @return the sequence of this search facet
     */
    public BigDecimal getSequence();

    /**
     * Sets the sequence of this search facet
     * 
     * @see #getPosition()
     * @param position
     */
    public void setSequence(BigDecimal sequence);
    
    public List<CategorySearchFacetValue> getCategorySearchFacetValues();
    
    public void setCategorySearchFacetValues(List<CategorySearchFacetValue> categorySearchFacetValues);

}
