package cn.globalph.b2c.rating.service.type;

import java.util.HashMap;
import java.util.Map;

public class RatingType {

    private static final Map<String, RatingType> TYPES = new HashMap<String, RatingType>();

    public static final RatingType PRODUCT = new RatingType("PRODUCT");

    public static RatingType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;

    public RatingType() {
    }

    public RatingType(final String type) {
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
        RatingType other = (RatingType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
