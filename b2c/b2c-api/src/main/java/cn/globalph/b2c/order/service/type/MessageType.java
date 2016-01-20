package cn.globalph.b2c.order.service.type;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of type of messages.
 *  <li><b>CART</b> - Represents a  wishlist</li>
 *  <li><b>PRODUCT_OPTION</b> - Represents a cart (non-submitted {@link Order}s)</li>
 * 
 * @felix.wu
 */
public class MessageType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, MessageType> TYPES = new LinkedHashMap<String, MessageType>();

    public static final MessageType CART = new MessageType("CART", "CART");
    public static final MessageType PRODUCT_OPTION = new MessageType("PRODUCT_OPTION", "PRODUCT_OPTION");

    public static MessageType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public MessageType() {
        //do nothing
    }

    public MessageType(final String type, final String friendlyType) {
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
        MessageType other = (MessageType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
