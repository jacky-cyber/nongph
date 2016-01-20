package cn.globalph.b2c.order.domain.wrapper;

import cn.globalph.b2c.offer.domain.FulfillmentGroupAdjustment;
import cn.globalph.b2c.offer.domain.wrap.AdjustmentWrap;
import cn.globalph.b2c.offer.domain.wrapper.AdjustmentWrapper;
import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.wrap.FulfillmentGroupItemWrap;
import cn.globalph.b2c.order.domain.wrap.FulfillmentGroupWrap;
import cn.globalph.b2c.order.service.FulfillmentOptionService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.call.FulfillmentGroupItemRequest;
import cn.globalph.b2c.order.service.call.FulfillmentGroupRequest;
import cn.globalph.common.domain.wrapper.APIUnwrapper;
import cn.globalph.common.domain.wrapper.APIWrapper;
import cn.globalph.passport.domain.wrapper.AddressWrapper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

public class FulfillmentGroupWrapper implements APIWrapper<FulfillmentGroup, FulfillmentGroupWrap>, APIUnwrapper<FulfillmentGroupRequest, FulfillmentGroupWrap> {
	
	private EnumerationTypeWrapper enumerationTypeWrapper;
	
	private FulfillmentOptionWrapper fulfillmentOptionWrapper;
	
	private FulfillmentGroupItemWrapper fulfillmentGroupItemWrapper;
	
	private AdjustmentWrapper adjustmentWrapper;
	
	private AddressWrapper addressWrapper;
	
	@Resource(name="blOrderService")
	private OrderService orderService;
	
	@Resource(name="blFulfillmentOptionService")
	private FulfillmentOptionService fulfillmentOptionService;
	 
    @Override
    public FulfillmentGroupWrap wrapDetails(FulfillmentGroup model) {
    	FulfillmentGroupWrap wrap = new FulfillmentGroupWrap();
    	wrap.setId( model.getId() );
    	wrap.setTotal( model.getTotal() );

        if (model.getType() != null) {
        	wrap.setFulfillmentType( enumerationTypeWrapper.wrapDetails(model.getType() ) );
        }

        if (model.getFulfillmentOption() != null) {
        	wrap.setFulfillmentOption( fulfillmentOptionWrapper.wrapDetails(model.getFulfillmentOption()) );
        }

        if (model.getOrder() != null) {
            wrap.setOrderId( model.getOrder().getId() );
        }

        if (model.getAddress() != null) {
            wrap.setAddress( addressWrapper.wrapDetails(model.getAddress() ) );
        }

        List<FulfillmentGroupItem> fgs = model.getFulfillmentGroupItems();
        if (fgs != null && !fgs.isEmpty()) {
            List<FulfillmentGroupItemWrap> fulfillmentGroupItemWraps = new ArrayList<FulfillmentGroupItemWrap>();
            for (FulfillmentGroupItem fgi : fgs) {
                fulfillmentGroupItemWraps.add(fulfillmentGroupItemWrapper.wrapSummary(fgi));
            }
            wrap.setFulfillmentGroupItems( fulfillmentGroupItemWraps );
        }

        List<FulfillmentGroupAdjustment> adjustments = model.getFulfillmentGroupAdjustments();
        if (adjustments != null && !adjustments.isEmpty()) {
            List<AdjustmentWrap> fulfillmentGroupAdjustments = new ArrayList<AdjustmentWrap>();
            for (FulfillmentGroupAdjustment adj : adjustments) {
                fulfillmentGroupAdjustments.add( adjustmentWrapper.wrapSummary(adj) );
            }
            wrap.setFulfillmentGroupAdjustments(fulfillmentGroupAdjustments);
        }
        return wrap;
    }

    @Override
    public FulfillmentGroupWrap wrapSummary(FulfillmentGroup model) {
        return wrapDetails(model);
    }

    @Override
    public FulfillmentGroupRequest unwrap(FulfillmentGroupWrap wrap) {
        FulfillmentGroupRequest fulfillmentGroupRequest = new FulfillmentGroupRequest();

        List<FulfillmentGroupItemRequest> fulfillmentGroupItemRequests = new ArrayList<FulfillmentGroupItemRequest>();
        for (FulfillmentGroupItemWrap itemWrap : wrap.getFulfillmentGroupItems()) {
            fulfillmentGroupItemRequests.add( fulfillmentGroupItemWrapper.unwrap(itemWrap));
        }

        fulfillmentGroupRequest.setFulfillmentGroupItemRequests(fulfillmentGroupItemRequests);

        
        Order order = orderService.getOrderById(wrap.getOrderId());
        if (order != null) {
            fulfillmentGroupRequest.setOrder(order);
        }

        if (wrap.getAddress() != null) {
            fulfillmentGroupRequest.setAddress(addressWrapper.unwrap(wrap.getAddress()));
        }

        if (wrap.getFulfillmentOption() != null && wrap.getFulfillmentOption().getId() != null) {
            FulfillmentOption option = fulfillmentOptionService.readFulfillmentOptionById(wrap.getFulfillmentOption().getId());
            fulfillmentGroupRequest.setOption(option);
        }

        return fulfillmentGroupRequest;
    }
}
