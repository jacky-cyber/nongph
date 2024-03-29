package cn.globalph.openadmin.dto;

import cn.globalph.openadmin.dto.visitor.PersistenceAssociationItemVisitor;

import java.io.Serializable;

/**
 * 
 * @felix.wu
 *
 */
public class MapStructure implements Serializable, PersistenceAssociationItem {

    private static final long serialVersionUID = 1L;
    
    private String keyClassName;
    private String mapKeyValueProperty;
    private String keyPropertyName;
    private String keyPropertyFriendlyName;
    private String valueClassName;
    private String mapProperty;
    private Boolean deleteValueEntity = Boolean.FALSE;
    private String manyToField;
    private Boolean mutable = true;
    
    public MapStructure() {
        //do nothing - support serialization requirements
    }
    
    public MapStructure(String keyClassName, String keyPropertyName, String keyPropertyFriendlyName, String valueClassName, 
            String mapProperty, Boolean deleteValueEntity, String mapKeyValueProperty) {
        if (!keyClassName.equals(String.class.getName())) {
            throw new RuntimeException("keyClass of java.lang.String is currently the only type supported");
        }
        this.keyClassName = keyClassName;
        this.valueClassName = valueClassName;
        this.mapProperty = mapProperty;
        this.keyPropertyName = keyPropertyName;
        this.keyPropertyFriendlyName = keyPropertyFriendlyName;
        this.deleteValueEntity = deleteValueEntity;
        this.mapKeyValueProperty = mapKeyValueProperty;
    }
    
    public String getKeyClassName() {
        return keyClassName;
    }
    
    public void setKeyClassName(String keyClassName) {
        if (!keyClassName.equals(String.class.getName())) {
            throw new RuntimeException("keyClass of java.lang.String is currently the only type supported");
        }
        this.keyClassName = keyClassName;
    }
    
    public String getValueClassName() {
        return valueClassName;
    }
    
    public void setValueClassName(String valueClassName) {
        this.valueClassName = valueClassName;
    }
    
    public String getMapProperty() {
        return mapProperty;
    }
    
    public void setMapProperty(String mapProperty) {
        this.mapProperty = mapProperty;
    }

    public String getKeyPropertyName() {
        return keyPropertyName;
    }

    public void setKeyPropertyName(String keyPropertyName) {
        this.keyPropertyName = keyPropertyName;
    }

    public String getKeyPropertyFriendlyName() {
        return keyPropertyFriendlyName;
    }

    public void setKeyPropertyFriendlyName(String keyPropertyFriendlyName) {
        this.keyPropertyFriendlyName = keyPropertyFriendlyName;
    }

    public Boolean getDeleteValueEntity() {
        return deleteValueEntity;
    }

    public void setDeleteValueEntity(Boolean deleteValueEntity) {
        this.deleteValueEntity = deleteValueEntity;
    }

    public String getManyToField() {
        return manyToField;
    }

    public void setManyToField(String manyToField) {
        this.manyToField = manyToField;
    }

    public Boolean getMutable() {
        return mutable;
    }

    public void setMutable(Boolean mutable) {
        this.mutable = mutable;
    }
    
    public String getMapKeyValueProperty() {
        return mapKeyValueProperty;
    }
    
    public void setMapKeyValueProperty(String mapKeyValueProperty) {
        this.mapKeyValueProperty = mapKeyValueProperty;
    }

    public void accept(PersistenceAssociationItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MapStructure{");
        sb.append("keyClassName='").append(keyClassName).append('\'');
        sb.append(", mapKeyValueProperty='").append(mapKeyValueProperty).append('\'');
        sb.append(", keyPropertyName='").append(keyPropertyName).append('\'');
        sb.append(", keyPropertyFriendlyName='").append(keyPropertyFriendlyName).append('\'');
        sb.append(", valueClassName='").append(valueClassName).append('\'');
        sb.append(", mapProperty='").append(mapProperty).append('\'');
        sb.append(", deleteValueEntity=").append(deleteValueEntity);
        sb.append(", manyToField='").append(manyToField).append('\'');
        sb.append(", mutable=").append(mutable);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public PersistenceAssociationItem clonePersistenceAssociationItem() {
        MapStructure mapStructure = new MapStructure();
        mapStructure.keyClassName = keyClassName;
        mapStructure.keyPropertyName = keyPropertyName;
        mapStructure.keyPropertyFriendlyName = keyPropertyFriendlyName;
        mapStructure.valueClassName = valueClassName;
        mapStructure.mapProperty = mapProperty;
        mapStructure.deleteValueEntity = deleteValueEntity;
        mapStructure.manyToField = manyToField;
        mapStructure.mutable = mutable;
        mapStructure.mapKeyValueProperty = mapKeyValueProperty;

        return mapStructure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapStructure)) return false;

        MapStructure that = (MapStructure) o;

        if (deleteValueEntity != null ? !deleteValueEntity.equals(that.deleteValueEntity) : that.deleteValueEntity != null)
            return false;
        if (mapKeyValueProperty != null ? !mapKeyValueProperty.equals(that.mapKeyValueProperty) : that.mapKeyValueProperty != null)
            return false;
        if (keyClassName != null ? !keyClassName.equals(that.keyClassName) : that.keyClassName != null) return false;
        if (keyPropertyFriendlyName != null ? !keyPropertyFriendlyName.equals(that.keyPropertyFriendlyName) : that.keyPropertyFriendlyName != null)
            return false;
        if (keyPropertyName != null ? !keyPropertyName.equals(that.keyPropertyName) : that.keyPropertyName != null)
            return false;
        if (mapProperty != null ? !mapProperty.equals(that.mapProperty) : that.mapProperty != null) return false;
        if (valueClassName != null ? !valueClassName.equals(that.valueClassName) : that.valueClassName != null)
            return false;
        if (manyToField != null ? !manyToField.equals(that.manyToField) : that.manyToField != null) return false;
        if (mutable != null ? !mutable.equals(that.mutable) : that.mutable != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = keyClassName != null ? keyClassName.hashCode() : 0;
        result = 31 * result + (keyPropertyName != null ? keyPropertyName.hashCode() : 0);
        result = 31 * result + (keyPropertyFriendlyName != null ? keyPropertyFriendlyName.hashCode() : 0);
        result = 31 * result + (mapKeyValueProperty != null ? mapKeyValueProperty.hashCode() : 0);
        result = 31 * result + (valueClassName != null ? valueClassName.hashCode() : 0);
        result = 31 * result + (mapProperty != null ? mapProperty.hashCode() : 0);
        result = 31 * result + (deleteValueEntity != null ? deleteValueEntity.hashCode() : 0);
        result = 31 * result + (manyToField != null ? manyToField.hashCode() : 0);
        result = 31 * result + (mutable != null ? mutable.hashCode() : 0);
        return result;
    }
}
