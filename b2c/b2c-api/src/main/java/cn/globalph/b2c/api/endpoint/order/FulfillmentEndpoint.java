package cn.globalph.b2c.api.endpoint.order;

import cn.globalph.b2c.api.WebServicesException;
import cn.globalph.b2c.api.endpoint.BaseEndpoint;
import cn.globalph.b2c.checkout.service.CheckoutService;
import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.wrap.FulfillmentGroupItemWrap;
import cn.globalph.b2c.order.domain.wrap.FulfillmentGroupWrap;
import cn.globalph.b2c.order.domain.wrap.FulfillmentOptionWrap;
import cn.globalph.b2c.order.domain.wrap.OrderWrap;
import cn.globalph.b2c.order.domain.wrapper.FulfillmentGroupItemWrapper;
import cn.globalph.b2c.order.domain.wrapper.FulfillmentGroupWrapper;
import cn.globalph.b2c.order.domain.wrapper.FulfillmentOptionWrapper;
import cn.globalph.b2c.order.domain.wrapper.OrderWrapper;
import cn.globalph.b2c.order.service.FulfillmentGroupService;
import cn.globalph.b2c.order.service.FulfillmentOptionService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.call.FulfillmentGroupItemRequest;
import cn.globalph.b2c.order.service.call.FulfillmentGroupRequest;
import cn.globalph.b2c.order.service.type.FulfillmentType;
import cn.globalph.b2c.pricing.service.exception.PricingException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

public abstract class FulfillmentEndpoint extends BaseEndpoint {

    @Resource(name="blCheckoutService")
    protected CheckoutService checkoutService;

    @Resource(name="blOrderService")
    protected OrderService orderService;
    
    @Resource(name="blFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;

    @Resource(name = "blFulfillmentOptionService")
    protected FulfillmentOptionService fulfillmentOptionService;
    
    protected FulfillmentGroupWrapper fulfillmentGroupWrapper;
    
    protected FulfillmentGroupItemWrapper fulfillmentGroupItemWrapper;
    
    protected FulfillmentOptionWrapper fulfillmentOptionWrapper;
    
    protected OrderWrapper orderWrapper;
    
    public List<FulfillmentGroupWrap> findFulfillmentGroupsForOrder(Long cartId) {
        Order cart = orderService.getOrderById( cartId );
        if(cart != null && cart.getFulfillmentGroups() != null && !cart.getFulfillmentGroups().isEmpty()) {
            List<FulfillmentGroup> fulfillmentGroups = cart.getFulfillmentGroups();
            List<FulfillmentGroupWrap> fulfillmentGroupWraps = new ArrayList<FulfillmentGroupWrap>();
            for (FulfillmentGroup fulfillmentGroup : fulfillmentGroups) {
                fulfillmentGroupWraps.add( fulfillmentGroupWrapper.wrapSummary(fulfillmentGroup) );
            }
            return fulfillmentGroupWraps;
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CART_NOT_FOUND);
    }

    public OrderWrap removeAllFulfillmentGroupsFromOrder(Long cartId, boolean priceOrder) {
        Order cart = orderService.getOrderById( cartId );
        if (cart != null) {
            try {
                fulfillmentGroupService.removeAllFulfillmentGroupsFromOrder(cart, priceOrder);
                return orderWrapper.wrapDetails(cart);
            } catch (PricingException e) {
                throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, e)
                        .addMessage(WebServicesException.CART_PRICING_ERROR);
            }
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CART_NOT_FOUND);
    }

    public FulfillmentGroupWrap addFulfillmentGroupToOrder( Long cartId, FulfillmentGroupWrap wrap, boolean priceOrder ) {
        Order cart = orderService.getOrderById( cartId );
        if (cart != null) {
            FulfillmentGroupRequest fulfillmentGroupRequest = fulfillmentGroupWrapper.unwrap(wrap);

            if (fulfillmentGroupRequest.getOrder() != null && fulfillmentGroupRequest.getOrder().getId().equals(cart.getId())) {
                try {
                    fulfillmentGroupRequest.setOrder(cart);
                    FulfillmentGroup fulfillmentGroup = fulfillmentGroupService.addFulfillmentGroupToOrder(fulfillmentGroupRequest, priceOrder);
                    return fulfillmentGroupWrapper.wrapDetails(fulfillmentGroup);
                } catch (PricingException e) {
                    throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, e)
                            .addMessage(WebServicesException.CART_PRICING_ERROR);
                }
            }
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CART_NOT_FOUND);

    }

    public FulfillmentGroupWrap addItemToFulfillmentGroup( Long cartId,
            												  Long fulfillmentGroupId,
            												  FulfillmentGroupItemWrap wrap,
            												  boolean priceOrder) {
        Order cart = orderService.getOrderById( cartId );
        if (cart != null) {
            FulfillmentGroupItemRequest fulfillmentGroupItemRequest = fulfillmentGroupItemWrapper.unwrap(wrap);
            if (fulfillmentGroupItemRequest.getOrderItem() != null) {
                FulfillmentGroup fulfillmentGroup = null;
                OrderItem orderItem = null;

                for (FulfillmentGroup fg : cart.getFulfillmentGroups()) {
                    if (fg.getId().equals(fulfillmentGroupId)) {
                        fulfillmentGroup = fg;
                    }
                }
                fulfillmentGroupItemRequest.setFulfillmentGroup(fulfillmentGroup);

                for (OrderItem oi : cart.getOrderItems()) {
                    if (oi.getId().equals(fulfillmentGroupItemRequest.getOrderItem().getId())) {
                        orderItem = oi;
                    }
                }
                fulfillmentGroupItemRequest.setOrderItem(orderItem);

                if (fulfillmentGroup != null && orderItem != null) {
                    try {
                        FulfillmentGroup fg = fulfillmentGroupService.addItemToFulfillmentGroup(fulfillmentGroupItemRequest, priceOrder);
                        return fulfillmentGroupWrapper.wrapDetails(fg);
                    } catch (PricingException e) {
                        throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, e)
                                .addMessage(WebServicesException.CART_PRICING_ERROR);
                    }
                }
            }
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CART_NOT_FOUND);

    }

    public FulfillmentGroupWrap addFulfillmentOptionToFulfillmentGroup(Long cartId,
            Long fulfillmentGroupId,
            Long fulfillmentOptionId,
            boolean priceOrder) {

        FulfillmentOption option = fulfillmentOptionService.readFulfillmentOptionById(fulfillmentOptionId);
        if (option == null) {
            throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                    .addMessage(WebServicesException.FULFILLMENT_OPTION_NOT_FOUND, fulfillmentOptionId);
        }

        Order cart = orderService.getOrderById( cartId );
        if (cart != null) {
            boolean found = false;
            List<FulfillmentGroup> groups = cart.getFulfillmentGroups();
            if (groups != null && !groups.isEmpty()) {
                for (FulfillmentGroup group : groups) {
                    if (group.getId().equals(fulfillmentGroupId)) {
                        group.setFulfillmentOption(option);
                        found = true;
                        break;
                    }
                }
            }
            try {
                if (found) {
                    cart = orderService.save(cart, priceOrder);
                    for (FulfillmentGroup fg : groups) {
                        if (fg.getId().equals(fulfillmentGroupId)) {
                            return fulfillmentGroupWrapper.wrapDetails(fg);
                        }
                    }
                } else {
                    throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                            .addMessage(WebServicesException.FULFILLMENT_GROUP_NOT_FOUND, fulfillmentGroupId);
                }
            } catch (PricingException e) {
                throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, e)
                        .addMessage(WebServicesException.CART_PRICING_ERROR);
            }
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CART_NOT_FOUND);
    }

    public List<FulfillmentOptionWrap> findFulfillmentOptions(String fulfillmentType) {
        ArrayList<FulfillmentOptionWrap> out = new ArrayList<FulfillmentOptionWrap>();
        List<FulfillmentOption> options = null;
        FulfillmentType type = FulfillmentType.getInstance(fulfillmentType);
        if (type != null) {
            options = fulfillmentOptionService.readAllFulfillmentOptionsByFulfillmentType(type);
        } else {
            options = fulfillmentOptionService.readAllFulfillmentOptions();
        }
        
        for (FulfillmentOption option : options) {
            out.add( fulfillmentOptionWrapper.wrapDetails(option) );
        }
        
        return out;
    }
}
