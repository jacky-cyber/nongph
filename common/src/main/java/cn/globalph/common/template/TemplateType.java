package cn.globalph.common.template;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of template types
 * 
 * @author bpolster
 */
public class TemplateType implements Serializable, EnumerationType, Comparable<TemplateType> {

    private static final long serialVersionUID = 1L;

    private static final Map<String, TemplateType> TYPES = new LinkedHashMap<String, TemplateType>();

    public static final TemplateType PRODUCT = new TemplateType("PRODUCT", "Product", 1000);
    public static final TemplateType CATEGORY = new TemplateType("CATEGORY", "Category", 2000);
    public static final TemplateType PAGE = new TemplateType("PAGE", "Page", 3000);
    public static final TemplateType OTHER = new TemplateType("OTHER", "Other", Integer.MAX_VALUE);

    public static TemplateType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;
    private int order;

    public TemplateType() {
        //do nothing
    }

    public TemplateType(final String type, final String friendlyType, int order) {
        this.friendlyType = friendlyType;
        setType(type);
        setOrder(order);
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
        } else {
            throw new RuntimeException("Cannot add the type: (" + type + "). It already exists as a type via " + getInstance(type).getClass().getName());
        }
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
        TemplateType other = (TemplateType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    @Override
    public int compareTo(TemplateType arg0) {
        return this.order - arg0.order;
    }
}
