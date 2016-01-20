package cn.globalph.b2c.order.service.call;

import cn.globalph.b2c.order.domain.OrderItem;

public class DiscreteOrderItemRequest extends AbstractOrderItemRequest {

    protected OrderItem orderItem;

    public DiscreteOrderItemRequest() {
        super();
     }

    public DiscreteOrderItemRequest(AbstractOrderItemRequest request) {
        setCategory(request.getCategory());
        setItemAttributes(request.getItemAttributes());
        setProduct(request.getProduct());
        setQuantity(request.getQuantity());
        setSku(request.getSku());
        setOrder(request.getOrder());
        setSalePriceOverride(request.getSalePriceOverride());
        setRetailPriceOverride(request.getRetailPriceOverride());
    }


    @Override
    public DiscreteOrderItemRequest clone() {
        DiscreteOrderItemRequest returnRequest = new DiscreteOrderItemRequest();
        copyProperties(returnRequest);
        return returnRequest;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }
    
    public void setOrderItem(OrderItem bundleOrderItem) {
        this.orderItem = bundleOrderItem;
    }
}
