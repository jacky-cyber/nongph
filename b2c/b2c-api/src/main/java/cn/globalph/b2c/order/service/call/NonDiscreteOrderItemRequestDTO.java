package cn.globalph.b2c.order.service.call;

import cn.globalph.common.money.Money;

public class NonDiscreteOrderItemRequestDTO extends OrderItemRequestDTO {

    protected String itemName;

    public NonDiscreteOrderItemRequestDTO() {
    }

    public NonDiscreteOrderItemRequestDTO(String itemName, Integer quantity, Money overrideRetailPrice) {
        setItemName(itemName);
        setQuantity(quantity);
        setOverrideRetailPrice(overrideRetailPrice);
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
