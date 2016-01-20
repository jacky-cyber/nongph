package cn.globalph.b2c.order.service.type;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * An extendible enumeration of order status types.
 * 
 * @felix.wu
 */
public class OrderStatus implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final LinkedHashMap<String, OrderStatus> TYPES = new LinkedHashMap<String, OrderStatus>();

    /**
     * Represents a wishlist. This also usually means that the {@link Order} has its {@link Order#getName()} set although
     * not required
     */
    public static final OrderStatus NAMED = new OrderStatus("NAMED", "Named");
    public static final OrderStatus QUOTE = new OrderStatus("QUOTE", "Quote");

    /**
     * Represents a suborder
     */
    public static final OrderStatus SUBORDER = new OrderStatus("SUBORDER", "子订单");
    
    /**
     * Represents a cart (non-submitted {@link Order}s)
     */
    public static final OrderStatus IN_PROCESS = new OrderStatus("IN_PROCESS", "购物车");
    
    /**
     * Used to represent a completed {@link Order}. Note that this also means that the {@link Order}
     * should have its {@link Order#getOrderNumber} set
     */
    public static final OrderStatus CONFIRMED = new OrderStatus("CONFIRMED", "已付款");
    public static final OrderStatus CANCELLED = new OrderStatus("CANCELLED", "已取消");
    public static final OrderStatus SUBMITTED = new OrderStatus("SUBMITTED", "待付款");
    public static final OrderStatus COMPLETED = new OrderStatus("COMPLETED", "完成");
    public static final OrderStatus ORDER_REFUND = new OrderStatus("ORDER_REFUND", "订单退款");
    public static final OrderStatus ITEM_REFUND = new OrderStatus("ORDER_REFUND", "订单项退货");
    
    public static final OrderStatus GROUP_ON = new OrderStatus("GROUP_ON", "团购");
    
    /**
     * Used when a CSR has locked a cart to act on behalf of a customer
     */
    public static final OrderStatus CSR_OWNED = new OrderStatus("CSR_OWNED", "Owned by CSR");


    public static OrderStatus getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public OrderStatus() {
        //do nothing
    }

    public OrderStatus(final String type, final String friendlyType) {
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
        OrderStatus other = (OrderStatus) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
