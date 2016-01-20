package cn.globalph.b2c.rating.service.type;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class RatingSortType implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Map<String, RatingSortType> TYPES = new HashMap<String, RatingSortType>();

    public static final RatingSortType MOST_HELPFUL = new RatingSortType("MOST_HELPFUL");
    public static final RatingSortType MOST_RECENT = new RatingSortType("MOST_RECENT");
    public static final RatingSortType DEFAULT = new RatingSortType("DEFAULT");

    public static RatingSortType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;

    public RatingSortType() {
    }

    public RatingSortType(final String type) {
        setType(type);
    }

    public String getType() {
        return type;
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
        RatingSortType other = (RatingSortType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
