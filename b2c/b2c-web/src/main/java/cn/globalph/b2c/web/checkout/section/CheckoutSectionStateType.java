package cn.globalph.b2c.web.checkout.section;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public class CheckoutSectionStateType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, CheckoutSectionStateType> TYPES = new LinkedHashMap<String, CheckoutSectionStateType>();

    public static final CheckoutSectionStateType FORM  = new CheckoutSectionStateType("FORM", "Show Form State");
    public static final CheckoutSectionStateType SAVED  = new CheckoutSectionStateType("SAVED", "Show Saved State");
    public static final CheckoutSectionStateType INACTIVE  = new CheckoutSectionStateType("INACTIVE", "Show Inactive State");

    public static CheckoutSectionStateType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public CheckoutSectionStateType() {
        //do nothing
    }

    public CheckoutSectionStateType(final String type, final String friendlyType) {
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
        CheckoutSectionStateType other = (CheckoutSectionStateType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

