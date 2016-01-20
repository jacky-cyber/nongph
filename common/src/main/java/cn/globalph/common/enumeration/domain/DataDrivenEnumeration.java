package cn.globalph.common.enumeration.domain;

import java.io.Serializable;
import java.util.List;

public interface DataDrivenEnumeration extends Serializable {
    
    public Long getId();

    public void setId(Long id);

    public String getKey();

    public void setKey(String key);

    public Boolean getModifiable();

    public void setModifiable(Boolean modifiable);

    /**
     * Gets list of values associated with this enumeration.
     */
    public List<DataDrivenEnumerationValue> getEnumValues();

    /**
     * Sets list of values associated with this enumeration. 
     */
    public void setEnumValues(List<DataDrivenEnumerationValue> enumValues);

    /**
     * Incorrectly named, kept purely for API consistency
     * @deprecated use {@link #getEnumValues()} instead
     */
    @Deprecated
    public List<DataDrivenEnumerationValue> getOrderItems();

    /**
     * Incorrectly named, kept purely for API consistency
     * @deprecated use {@link #setEnumValues()} instead
     */
    @Deprecated
    public void setOrderItems(List<DataDrivenEnumerationValue> orderItems);

}
