package cn.globalph.b2c.pricing.service.workflow.type;

import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.pricing.service.fulfillment.provider.FulfillmentPricingProvider;
import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of shipping service types.
 * 
 * @felix.wu
 * @deprecated Should use the {@link FulfillmentOption} and {@link FulfillmentPricingProvider} paradigm
 * @see {@link FulfillmentOption}, {@link FulfillmentPricingProvider}
 */
@Deprecated
public class ShippingServiceType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, ShippingServiceType> TYPES = new LinkedHashMap<String, ShippingServiceType>();

    public static final ShippingServiceType BANDED_SHIPPING = new ShippingServiceType("BANDED_SHIPPING", "Banded Shipping");
    public static final ShippingServiceType USPS = new ShippingServiceType("USPS", "United States Postal Service");
    public static final ShippingServiceType FED_EX = new ShippingServiceType("FED_EX", "Federal Express");
    public static final ShippingServiceType UPS = new ShippingServiceType("UPS", "United Parcel Service");
    public static final ShippingServiceType DHL = new ShippingServiceType("DHL", "DHL");

    public static ShippingServiceType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public ShippingServiceType() {
        //do nothing
    }

    public ShippingServiceType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
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
        ShippingServiceType other = (ShippingServiceType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
