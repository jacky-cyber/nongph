package cn.globalph.b2c.web.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.call.UpdateCartResponse;
import cn.globalph.b2c.order.service.exception.AddToCartException;
import cn.globalph.b2c.order.service.exception.RemoveFromCartException;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.web.order.model.AddToCartItem;
import cn.globalph.common.extension.ExtensionResultHolder;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

@Service("blUpdateCartService")
public class UpdateCartServiceImpl implements UpdateCartService {
    protected static final Log LOG = LogFactory.getLog(UpdateCartServiceImpl.class);

    @Resource(name="blOrderService")
    protected OrderService orderService;
    
    @Resource(name = "blUpdateCartServiceExtensionManager")
    protected UpdateCartServiceExtensionManager extensionManager;


    @Override
    public UpdateCartResponse copyCartToCurrentContext(Order currentCart) {
        if(currentCart.getOrderItems() == null){
            return null;
           }

        //Reprice order logic
        List<AddToCartItem> itemsToReprice = new ArrayList<AddToCartItem>();
        List<OrderItem> itemsToRemove = new ArrayList<OrderItem>();
        List<OrderItem> itemsToReset = new ArrayList<OrderItem>();
        boolean repriceOrder = true;

        for(OrderItem orderItem: currentCart.getOrderItems()){
            //Lookup price in price list, if null, then add to itemsToRemove
                if(checkAvailability(orderItem)){
                    AddToCartItem itemRequest = new AddToCartItem();
                    itemRequest.setSkuId(orderItem.getSku().getId());
                    itemRequest.setQuantity(orderItem.getQuantity());
                    itemsToReprice.add(itemRequest);
                    itemsToReset.add(orderItem);
                } else {
                    itemsToRemove.add(orderItem);
                	 }
           }

        for(OrderItem orderItem: itemsToReset){
            try {
                currentCart = orderService.removeItem(currentCart.getId(), orderItem.getId(), false);
            } catch (RemoveFromCartException e) {
                e.printStackTrace();
            }
        }

        for(AddToCartItem itemRequest: itemsToReprice){
            try {
                currentCart = orderService.addItem(currentCart.getId(), itemRequest, false);
            } catch (AddToCartException e) {
                e.printStackTrace();
            }
        }

        // Reprice and save the cart
        try {
         currentCart = orderService.save(currentCart, repriceOrder);
        } catch (PricingException e) {
         e.printStackTrace();
           }

        UpdateCartResponse updateCartResponse = new UpdateCartResponse();
        updateCartResponse.setRemovedItems(itemsToRemove);
        updateCartResponse.setOrder(currentCart);

        return updateCartResponse;
    }

    @Override
    public void validateCart(Order cart) {
        // hook to allow override
    }

    @Override
    public void updateAndValidateCart(Order cart) {
        if (extensionManager != null) {
            ExtensionResultHolder erh = new ExtensionResultHolder();
            extensionManager.getHandlerProxy().updateAndValidateCart(cart, erh);
            Boolean clearCart = (Boolean) erh.getContextMap().get("clearCart");
            Boolean repriceCart = (Boolean) erh.getContextMap().get("repriceCart");
            Boolean saveCart = (Boolean) erh.getContextMap().get("saveCart");
            if (clearCart != null && clearCart.booleanValue()) {
                orderService.cancelOrder(cart);
                cart = orderService.createNewCartForCustomer(cart.getCustomer());
            } else {
                try {
                    if (repriceCart != null && repriceCart.booleanValue()) {
                        cart.updatePrices();
                        orderService.save(cart, true);
                    } else if (saveCart != null && saveCart.booleanValue()) {
                        orderService.save(cart, false);
                    }
                } catch (PricingException pe) {
                    LOG.error("Pricing Exception while validating cart.   Clearing cart.", pe);
                    orderService.cancelOrder(cart);
                    cart = orderService.createNewCartForCustomer(cart.getCustomer());
                }
            }
        }
    }

    protected boolean checkAvailability(OrderItem doi) {
        if (doi.getSku() != null && extensionManager != null) {
            Sku sku = doi.getSku();
            return sku.isAvailable();
          }
        return false;
    }
}
