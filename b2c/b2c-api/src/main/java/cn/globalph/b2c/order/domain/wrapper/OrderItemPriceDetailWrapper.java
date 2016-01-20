package cn.globalph.b2c.order.domain.wrapper;

import cn.globalph.b2c.offer.domain.OrderItemPriceDetailAdjustment;
import cn.globalph.b2c.offer.domain.wrap.AdjustmentWrap;
import cn.globalph.b2c.offer.domain.wrapper.AdjustmentWrapper;
import cn.globalph.b2c.order.domain.OrderItemPriceDetail;
import cn.globalph.b2c.order.domain.wrap.OrderItemPriceDetailWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

import java.util.ArrayList;
import java.util.List;

public class OrderItemPriceDetailWrapper implements APIWrapper<OrderItemPriceDetail, OrderItemPriceDetailWrap> {
	
	private AdjustmentWrapper adjustmentWrapper;
	
    @Override
    public OrderItemPriceDetailWrap wrapDetails(OrderItemPriceDetail model) {
    	OrderItemPriceDetailWrap wrap = new OrderItemPriceDetailWrap();
    	wrap.setId( model.getId() );
    	wrap.setQuantity( model.getQuantity() );
    	wrap.setTotalAdjustmentValue( model.getTotalAdjustmentValue() );
    	wrap.setTotalAdjustedPrice( model.getTotalAdjustedPrice() );
        if (!model.getOrderItemPriceDetailAdjustments().isEmpty()) {
            List<AdjustmentWrap> adjustmentWrapList = new ArrayList<AdjustmentWrap>();
            for (OrderItemPriceDetailAdjustment orderItemPriceDetail : model.getOrderItemPriceDetailAdjustments()) {
            	adjustmentWrapList.add( adjustmentWrapper.wrapSummary(orderItemPriceDetail));
            }
            wrap.setOrderItemPriceDetailAdjustments(adjustmentWrapList);
        }
        return wrap;
    }
    
    @Override
    public OrderItemPriceDetailWrap wrapSummary(OrderItemPriceDetail model) {
        return wrapDetails(model);
    }
}
