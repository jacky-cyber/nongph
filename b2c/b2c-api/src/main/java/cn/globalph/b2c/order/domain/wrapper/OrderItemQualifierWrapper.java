package cn.globalph.b2c.order.domain.wrapper;

import cn.globalph.b2c.order.domain.OrderItemQualifier;
import cn.globalph.b2c.order.domain.wrap.OrderItemQualifierWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class OrderItemQualifierWrapper implements APIWrapper<OrderItemQualifier, OrderItemQualifierWrap> {

    @Override
    public OrderItemQualifierWrap wrapDetails(OrderItemQualifier model) {
    	OrderItemQualifierWrap wrap = new OrderItemQualifierWrap();
    	wrap.setOfferId( model.getOffer().getId() );
    	wrap.setQuantity( model.getQuantity() );
    	return wrap;
    }

    @Override
    public OrderItemQualifierWrap wrapSummary(OrderItemQualifier model) {
        return wrapDetails(model);
    }

}
