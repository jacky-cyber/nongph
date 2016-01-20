package cn.globalph.b2c.catalog.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public interface CategoryXref extends Serializable {

    public BigDecimal getDisplayOrder();

    public void setDisplayOrder(final BigDecimal displayOrder);
    
    public Category getCategory();

    public void setCategory(final Category category);

    public Category getSubCategory();

    public void setSubCategory(final Category subCategory);

    public void setId(Long id);

    public Long getId();

}
