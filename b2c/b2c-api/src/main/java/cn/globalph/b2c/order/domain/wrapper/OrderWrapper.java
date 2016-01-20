package cn.globalph.b2c.order.domain.wrapper;

import cn.globalph.b2c.offer.domain.OrderAdjustment;
import cn.globalph.b2c.offer.domain.wrap.AdjustmentWrap;
import cn.globalph.b2c.offer.domain.wrapper.AdjustmentWrapper;
import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.wrap.CartMessageWrap;
import cn.globalph.b2c.order.domain.wrap.FulfillmentGroupWrap;
import cn.globalph.b2c.order.domain.wrap.OrderItemWrap;
import cn.globalph.b2c.order.domain.wrap.OrderPaymentWrap;
import cn.globalph.b2c.order.domain.wrap.OrderWrap;
import cn.globalph.b2c.order.service.call.ActivityMessageDTO;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.common.domain.wrapper.APIWrapper;
import cn.globalph.passport.domain.wrapper.CustomerWrapper;

import java.util.ArrayList;
import java.util.List;

public class OrderWrapper implements APIWrapper<Order, OrderWrap> {

	private OrderItemWrapper orderItemWrapper;	
	
	private FulfillmentGroupWrapper fulfillmentGroupWrapper;
	
	private OrderPaymentWrapper paymentWrapper;
	
	private AdjustmentWrapper orderAdjustmentWrapper;
	
	private CustomerWrapper customerWrapper;
	
	private CartMessageWrapper cartMessageWrapper;
	
    @Override
    public OrderWrap wrapDetails(Order model) {
    	OrderWrap wrap = new OrderWrap();
    	wrap.setId( model.getId() );

        if (model.getStatus() != null) {
            wrap.setStatus( model.getStatus().getType() );
        }

        wrap.setTotalShipping( model.getTotalFulfillmentCharges() );
        wrap.setSubTotal( model.getSubTotal() );
        wrap.setTotal( model.getTotal() );

        if (model.getOrderItems() != null && !model.getOrderItems().isEmpty()) {
        	List<OrderItemWrap> orderItems = new ArrayList<OrderItemWrap>();
            for (OrderItem orderItem : model.getOrderItems()) {
                orderItems.add( orderItemWrapper.wrapSummary(orderItem) );
            }
            wrap.setOrderItems( orderItems );
        }

        if (model.getFulfillmentGroups() != null && !model.getFulfillmentGroups().isEmpty()) {
            List<FulfillmentGroupWrap> fulfillmentGroups = new ArrayList<FulfillmentGroupWrap>();
            for (FulfillmentGroup fulfillmentGroup : model.getFulfillmentGroups()) {
                fulfillmentGroups.add( fulfillmentGroupWrapper.wrapSummary(fulfillmentGroup) );
            }
            wrap.setFulfillmentGroups(fulfillmentGroups);
        }

        if (model.getPayments() != null && !model.getPayments().isEmpty()) {
            List<OrderPaymentWrap> payments = new ArrayList<OrderPaymentWrap>();
            for (OrderPayment payment : model.getPayments()) {
                payments.add( paymentWrapper.wrapSummary(payment) );
            }
            wrap.setPayments( payments );
        }

        if (model.getOrderAdjustments() != null && !model.getOrderAdjustments().isEmpty()) {
            List<AdjustmentWrap> orderAdjustments = new ArrayList<AdjustmentWrap>();
            for (OrderAdjustment orderAdjustment : model.getOrderAdjustments()) {
                orderAdjustments.add(orderAdjustmentWrapper.wrapSummary(orderAdjustment));
            }
            wrap.setOrderAdjustments( orderAdjustments );
        }
        
        wrap.setCustomer( customerWrapper.wrapDetails(model.getCustomer() ) );

        if (model.getOrderMessages() != null && !model.getOrderMessages().isEmpty()) {
        	List<CartMessageWrap> cartMessages = new ArrayList<CartMessageWrap>();
        	for (ActivityMessageDTO dto : model.getOrderMessages()) {
                cartMessages.add(cartMessageWrapper.wrapSummary(dto));
        	}
        	wrap.setCartMessages( cartMessages );
        }
        return wrap;
    }

    @Override
    public OrderWrap wrapSummary(Order model) {
        return wrapDetails(model);
    }


}
