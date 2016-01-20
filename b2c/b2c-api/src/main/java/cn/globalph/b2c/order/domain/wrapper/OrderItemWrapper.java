package cn.globalph.b2c.order.domain.wrapper;

import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.OrderItemPriceDetail;
import cn.globalph.b2c.order.domain.OrderItemQualifier;
import cn.globalph.b2c.order.domain.wrap.OrderItemPriceDetailWrap;
import cn.globalph.b2c.order.domain.wrap.OrderItemQualifierWrap;
import cn.globalph.b2c.order.domain.wrap.OrderItemWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

import java.util.ArrayList;
import java.util.List;

public class OrderItemWrapper implements APIWrapper<OrderItem, OrderItemWrap> {
	
	private OrderItemPriceDetailWrapper orderItemPriceDetailWrapper;
	private OrderItemQualifierWrapper qualifierWrapper;
	
    @Override
    public OrderItemWrap wrapDetails(OrderItem model) {
    	OrderItemWrap wrap = new OrderItemWrap();
    	wrap.setId( model.getId() );
    	wrap.setName( model.getName() );
    	wrap.setQuantity( model.getQuantity() );
    	wrap.setOrderId( model.getOrder().getId() );
    	wrap.setRetailPrice( model.getRetailPrice() );
    	wrap.setSalePrice( model.getSalePrice() );

        if (model.getOrderItemPriceDetails() != null && !model.getOrderItemPriceDetails().isEmpty()) {
            List<OrderItemPriceDetailWrap> orderItemPriceDetails = new ArrayList<OrderItemPriceDetailWrap>();
            for (OrderItemPriceDetail orderItemPriceDetail : model.getOrderItemPriceDetails()) {
                orderItemPriceDetails.add( orderItemPriceDetailWrapper.wrapSummary(orderItemPriceDetail) );
            }
            wrap.setOrderItemPriceDetails( orderItemPriceDetails );
        }
        
        wrap.setSkuId( model.getSku().getId() );
        wrap.setProductId( model.getSku().getProduct().getId() );
        wrap.setIsDiscountingAllowed( model.isDiscountingAllowed() );

        if (model.getOrderItemQualifiers() != null && !model.getOrderItemQualifiers().isEmpty()) {
            List<OrderItemQualifierWrap> qualifiers = new ArrayList<OrderItemQualifierWrap>();
            for (OrderItemQualifier qualifier : model.getOrderItemQualifiers()) {
                qualifiers.add( qualifierWrapper.wrapSummary(qualifier) );
            }
            wrap.setQualifiers( qualifiers );
        }
        return wrap;
    }

    @Override
    public OrderItemWrap wrapSummary(OrderItem model) {
        return wrapDetails(model);
    }
}
