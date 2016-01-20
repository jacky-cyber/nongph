package cn.globalph.b2c.order.domain;

import cn.globalph.b2c.order.service.type.FulfillmentGroupStatusType;
import cn.globalph.common.money.Money;

import java.io.Serializable;

public interface FulfillmentGroupItem extends Serializable {

    public Long getId();

    public void setId(Long id);

    public FulfillmentGroup getFulfillmentGroup();

    public void setFulfillmentGroup(FulfillmentGroup fulfillmentGroup);

    public OrderItem getOrderItem();

    public void setOrderItem(OrderItem orderItem);

    public int getQuantity();

    public void setQuantity(int quantity);

    public Money getRetailPrice();

    public Money getSalePrice();
    
    public Money getTotalItemAmount();

    public void setTotalItemAmount(Money amount);
    
    public Money getProratedOrderAdjustmentAmount();

    public void setProratedOrderAdjustmentAmount(Money amount);

    public FulfillmentGroupStatusType getStatus();

    public void setStatus(FulfillmentGroupStatusType status);
    
    public void removeAssociations();

    public FulfillmentGroupItem clone();

    /**
     * Returns true if this item has pro-rated order adjustments.
     * @return
     */
    boolean getHasProratedOrderAdjustments();

}
