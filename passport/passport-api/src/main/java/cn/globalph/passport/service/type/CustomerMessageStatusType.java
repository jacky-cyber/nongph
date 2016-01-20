package cn.globalph.passport.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomerMessageStatusType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

        private static final Map<String, CustomerMessageStatusType> TYPES = new LinkedHashMap<String, CustomerMessageStatusType>();

        public static final CustomerMessageStatusType ACTIVE = new CustomerMessageStatusType("ACTIVE", "未读");
        
        public static final CustomerMessageStatusType ARCHIVE = new CustomerMessageStatusType("ARCHIVE", "已读");

        public static CustomerMessageStatusType getInstance(final String type) {
            return TYPES.get(type);
        }

        private String type;
        private String friendlyType;

        public CustomerMessageStatusType() {
            //do nothing
        }

        public CustomerMessageStatusType(final String type, final String friendlyType) {
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
            CustomerMessageStatusType other = (CustomerMessageStatusType) obj;
            if (type == null) {
                if (other.type != null)
                    return false;
            } else if (!type.equals(other.type))
                return false;
            return true;
        }
}
