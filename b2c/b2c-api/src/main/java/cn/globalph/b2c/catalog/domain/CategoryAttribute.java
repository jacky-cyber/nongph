package cn.globalph.b2c.catalog.domain;

import cn.globalph.common.value.Searchable;

public interface CategoryAttribute extends Searchable<String> {

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public Long getId();

    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(Long id);

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue();

    /**
     * Sets the value.
     * 
     * @param value the new value
     */
    public void setValue(String value);

    /**
     * Gets the {@link Category}.
     * 
     * @return the {@link Category}
     */
    public Category getCategory();

    /**
     * Sets the {@link Category}.
     * 
     * @param category the new {@link Category}
     */
    public void setCategory(Category category);

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName();

    /**
     * Sets the name.
     * 
     * @param name the new name
     */
    public void setName(String name);

}
