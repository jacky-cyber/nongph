package cn.globalph.passport.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * An extendible enumeration of customer address types.
 * 
 * @felix.wu
 */
public class CustomerAddressType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

        private static final Map<String, CustomerAddressType> TYPES = new LinkedHashMap<String, CustomerAddressType>();

        public static final CustomerAddressType SHIPPING  = new CustomerAddressType("SHIPPING", "Shipping");
        public static final CustomerAddressType BILLING  = new CustomerAddressType("BILLING", "Billing");
        public static final CustomerAddressType OTHER  = new CustomerAddressType("OTHER", "Other");

        public static CustomerAddressType getInstance(final String type) {
            return TYPES.get(type);
        }

        private String type;
        private String friendlyType;

        public CustomerAddressType() {
            //do nothing
        }

        public CustomerAddressType(final String type, final String friendlyType) {
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
            CustomerAddressType other = (CustomerAddressType) obj;
            if (type == null) {
                if (other.type != null)
                    return false;
            } else if (!type.equals(other.type))
                return false;
            return true;
        }
}
