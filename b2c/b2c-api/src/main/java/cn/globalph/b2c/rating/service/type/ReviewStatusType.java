package cn.globalph.b2c.rating.service.type;

import java.util.HashMap;
import java.util.Map;

public class ReviewStatusType {
    private static final long serialVersionUID = 1L;

    private static final Map<String, ReviewStatusType> TYPES = new HashMap<String, ReviewStatusType>();

    public static final ReviewStatusType PENDING = new ReviewStatusType("PENDING");
    public static final ReviewStatusType APPROVED = new ReviewStatusType("APPROVED");
    public static final ReviewStatusType REJECTED = new ReviewStatusType("REJECTED");

    public static ReviewStatusType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;

    public ReviewStatusType() {
    }

    public ReviewStatusType(final String type) {
        setType(type);
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
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
        ReviewStatusType other = (ReviewStatusType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
