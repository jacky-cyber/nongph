package cn.globalph.b2c.product.domain;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of product types.
 * 
 */
public class ProductType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, ProductType> TYPES = new LinkedHashMap<String, ProductType>();

    public static final ProductType PRODUCT  = new ProductType("cn.globalph.b2c.product.domain.Product", "Normal Product");
    public static final ProductType BUNDLE  = new ProductType("cn.globalph.b2c.product.domain.ProductBundle", "Product Bundle");

    public static ProductType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public ProductType() {
        //do nothing
    }

    public ProductType(final String type, final String friendlyType) {
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
        ProductType other = (ProductType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
