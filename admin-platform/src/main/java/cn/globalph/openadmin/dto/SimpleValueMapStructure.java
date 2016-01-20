package cn.globalph.openadmin.dto;

import cn.globalph.openadmin.dto.visitor.PersistenceAssociationItemVisitor;

/**
 * 
 * @felix.wu
 *
 */
public class SimpleValueMapStructure extends MapStructure {

    private static final long serialVersionUID = 1L;
    
    private String valuePropertyName;
    private String valuePropertyFriendlyName;
    
    public SimpleValueMapStructure() {
        super();
    }
    
    /**
     * @param keyClassName
     * @param keyPropertyName
     * @param keyPropertyFriendlyName
     * @param valueClassName
     * @param mapProperty
     */
    public SimpleValueMapStructure(String keyClassName, String keyPropertyName, String keyPropertyFriendlyName, String valueClassName, String valuePropertyName, String valuePropertyFriendlyName, String mapProperty, String mapKeyValueProperty) {
        super(keyClassName, keyPropertyName, keyPropertyFriendlyName, valueClassName, mapProperty, false, mapKeyValueProperty);
        this.valuePropertyFriendlyName = valuePropertyFriendlyName;
        this.valuePropertyName = valuePropertyName;
    }

    public String getValuePropertyName() {
        return valuePropertyName;
    }
    
    public void setValuePropertyName(String valuePropertyName) {
        this.valuePropertyName = valuePropertyName;
    }
    
    public String getValuePropertyFriendlyName() {
        return valuePropertyFriendlyName;
    }
    
    public void setValuePropertyFriendlyName(String valuePropertyFriendlyName) {
        this.valuePropertyFriendlyName = valuePropertyFriendlyName;
    }
    
    public void accept(PersistenceAssociationItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SimpleValueMapStructure{");
        sb.append(super.toString());
        sb.append("valuePropertyName='").append(valuePropertyName).append('\'');
        sb.append(", valuePropertyFriendlyName='").append(valuePropertyFriendlyName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public PersistenceAssociationItem clonePersistenceAssociationItem() {
        SimpleValueMapStructure mapStructure = new SimpleValueMapStructure();
        mapStructure.setKeyClassName(getKeyClassName());
        mapStructure.setKeyPropertyName(getKeyPropertyName());
        mapStructure.setValuePropertyFriendlyName(getKeyPropertyFriendlyName());
        mapStructure.setValueClassName(getValueClassName());
        mapStructure.setMapProperty(getMapProperty());
        mapStructure.setDeleteValueEntity(getDeleteValueEntity());
        mapStructure.valuePropertyName = valuePropertyName;
        mapStructure.valuePropertyFriendlyName = valuePropertyFriendlyName;

        return mapStructure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleValueMapStructure)) return false;
        if (!super.equals(o)) return false;

        SimpleValueMapStructure that = (SimpleValueMapStructure) o;

        if (valuePropertyFriendlyName != null ? !valuePropertyFriendlyName.equals(that.valuePropertyFriendlyName) : that.valuePropertyFriendlyName != null)
            return false;
        if (valuePropertyName != null ? !valuePropertyName.equals(that.valuePropertyName) : that.valuePropertyName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (valuePropertyName != null ? valuePropertyName.hashCode() : 0);
        result = 31 * result + (valuePropertyFriendlyName != null ? valuePropertyFriendlyName.hashCode() : 0);
        return result;
    }
}
