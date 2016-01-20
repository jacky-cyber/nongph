package cn.globalph.b2c.order.service.call;


public class OrderItemRequest extends AbstractOrderItemRequest {

    protected String itemName;

    public OrderItemRequest() {
        super();
    }

    public OrderItemRequest(AbstractOrderItemRequest request) {
        setQuantity(request.getQuantity());
        setOrder(request.getOrder());
        setSalePriceOverride(request.getSalePriceOverride());
        setRetailPriceOverride(request.getRetailPriceOverride());
    }

    @Override
    public OrderItemRequest clone() {
        OrderItemRequest returnRequest = new OrderItemRequest();
        copyProperties(returnRequest);
        returnRequest.setItemName(itemName);
        return returnRequest;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
