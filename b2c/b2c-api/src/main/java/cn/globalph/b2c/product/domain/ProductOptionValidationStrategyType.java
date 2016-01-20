package cn.globalph.b2c.product.domain;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of product option validation strategy types.
 * 
 * @author ppatel
 *
 */
public class ProductOptionValidationStrategyType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, ProductOptionValidationStrategyType> TYPES = new LinkedHashMap<String, ProductOptionValidationStrategyType>();

    public static final ProductOptionValidationStrategyType ADD_ITEM = new ProductOptionValidationStrategyType("ADD_ITEM", 1000, "Validate On Add Item");
    public static final ProductOptionValidationStrategyType SUBMIT_ORDER = new ProductOptionValidationStrategyType("SUBMIT_ORDER", 2000, "Validate On Submit");

    public static ProductOptionValidationStrategyType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;
    private Integer rank;

    public Integer getRank() {
        return rank;
    }

    public ProductOptionValidationStrategyType() {
        //do nothing
    }

    public ProductOptionValidationStrategyType(final String type, final Integer rank, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
        this.rank = rank;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
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
        ProductOptionValidationStrategyType other = (ProductOptionValidationStrategyType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
