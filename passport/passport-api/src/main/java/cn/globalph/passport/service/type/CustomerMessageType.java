package cn.globalph.passport.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomerMessageType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

        private static final Map<String, CustomerMessageType> TYPES = new LinkedHashMap<String, CustomerMessageType>();

        public static final CustomerMessageType NEW_COUPON = new CustomerMessageType("NEW_COUPON", "新优惠券");
        
        public static final CustomerMessageType COUPOH_OVERTIME = new CustomerMessageType("COUPOH_OVERTIME", "优惠券过期提醒");

        public static CustomerMessageType getInstance(final String type) {
            return TYPES.get(type);
        }

        private String type;
        private String friendlyType;

        public CustomerMessageType() {
            //do nothing
        }

        public CustomerMessageType(final String type, final String friendlyType) {
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
            CustomerMessageType other = (CustomerMessageType) obj;
            if (type == null) {
                if (other.type != null)
                    return false;
            } else if (!type.equals(other.type))
                return false;
            return true;
        }
}
