package cn.globalph.b2c.search.domain;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extensible enumeration of entities that are used for searching and reporting
 * 
 */
public class FieldEntity implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, FieldEntity> TYPES = new LinkedHashMap<String, FieldEntity>();

    public static final FieldEntity PRODUCT = new FieldEntity("PRODUCT", "product");
    public static final FieldEntity SKU     = new FieldEntity("SKU", "sku");
    public static final FieldEntity CUSTOMER= new FieldEntity("CUSTOMER", "customer");
    public static final FieldEntity ORDER   = new FieldEntity("ORDER", "order");
    public static final FieldEntity ORDERITEM=new FieldEntity("ORDERITEM", "orderItem");
    public static final FieldEntity OFFER   = new FieldEntity("OFFER", "offer");

    public static FieldEntity getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public FieldEntity() {
        //do nothing
    }

    public FieldEntity(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    public String getType() {
        return type;
    }

    public String getFriendlyType() {
        return friendlyType;
    }

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        FieldEntity other = (FieldEntity) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
