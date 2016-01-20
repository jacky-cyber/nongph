package cn.globalph.openadmin.dto;

import java.io.Serializable;
import java.util.Date;

public class Property implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    protected String name;
    protected String value;
    protected String displayValue;
    protected String originalDisplayValue;
    protected FieldMetadata fieldMetadata = new BasicFieldMetadata();
    protected boolean isAdvancedCollection = false;
    protected Boolean isDirty = false;
    protected String unHtmlEncodedValue;
    protected String rawValue;
    protected String originalValue;
    protected Date deployDate;

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FieldMetadata getFieldMetadata() {
        return fieldMetadata;
    }

    public void setFieldMetadata(FieldMetadata metadata) {
        this.fieldMetadata = metadata;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public Boolean getIsDirty() {
        return isDirty;
    }

    public void setIsDirty(Boolean isDirty) {
        this.isDirty = isDirty;
    }

    public String getUnHtmlEncodedValue() {
        return unHtmlEncodedValue;
    }

    public void setUnHtmlEncodedValue(String unHtmlEncodedValue) {
        this.unHtmlEncodedValue = unHtmlEncodedValue;
    }

    public String getRawValue() {
        return rawValue;
    }

    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }

    public boolean isAdvancedCollection() {
        return isAdvancedCollection;
    }

    public void setAdvancedCollection(boolean advancedCollection) {
        isAdvancedCollection = advancedCollection;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public String getOriginalDisplayValue() {
        return originalDisplayValue;
    }

    public void setOriginalDisplayValue(String originalDisplayValue) {
        this.originalDisplayValue = originalDisplayValue;
    }

    public Date getDeployDate() {
        return deployDate;
    }

    public void setDeployDate(Date deployDate) {
        this.deployDate = deployDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Property{");
        sb.append("name='").append(name).append('\'');
        String temp = value;
        if (temp != null && temp.length() > 200) {
            temp = temp.substring(0,199) + "...";
        }
        sb.append(", value='").append(temp).append('\'');
        sb.append(", isDirty=").append(isDirty);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fieldMetadata == null || fieldMetadata instanceof CollectionMetadata || ((BasicFieldMetadata) fieldMetadata).getMergedPropertyType() == null) ? 0 : ((BasicFieldMetadata) fieldMetadata).getMergedPropertyType().hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Property other = (Property) obj;
        if (fieldMetadata == null || fieldMetadata instanceof CollectionMetadata || ((BasicFieldMetadata) fieldMetadata).getMergedPropertyType() == null) {
            if (other.fieldMetadata != null && other.fieldMetadata instanceof BasicFieldMetadata && ((BasicFieldMetadata) other.fieldMetadata).getMergedPropertyType() != null)
                return false;
        } else if (fieldMetadata instanceof BasicFieldMetadata && other.fieldMetadata instanceof BasicFieldMetadata && !((BasicFieldMetadata) fieldMetadata).getMergedPropertyType().equals(((BasicFieldMetadata) other.fieldMetadata).getMergedPropertyType()))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
