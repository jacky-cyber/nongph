package cn.globalph.b2c.search.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author felix.wu
 */
public class SearchFacetResultDTO implements Serializable{
    
	private static final long serialVersionUID = 1L;

	protected SearchFacet facet;
    
    protected String value;
    
    protected BigDecimal minValue;
    protected BigDecimal maxValue;
    
    protected Integer quantity;
    
    protected boolean active;
    
    public SearchFacet getFacet() {
        return facet;
    }

    public void setFacet(SearchFacet facet) {
        this.facet = facet;
    }

    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public String getValueKey() {
        String value = getValue();
        
        if (value == null) {
            value = "range[" + getMinValue() + ":" + getMaxValue() + "]";
        }
        
        return value;
    }
    
}
