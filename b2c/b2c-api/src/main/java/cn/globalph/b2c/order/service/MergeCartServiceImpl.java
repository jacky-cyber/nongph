package cn.globalph.b2c.order.service;

import org.apache.commons.lang.StringUtils;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.call.MergeCartResponse;
import cn.globalph.b2c.order.service.call.ReconstructCartResponse;
import cn.globalph.b2c.order.service.exception.RemoveFromCartException;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.passport.domain.Customer;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 * The 2.0 implementation of merge cart service. Instead of merging items directly from one cart to another, we will
 * convert the previous cart to a named order that the customer is able to interact with as they see fit.
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Service("blMergeCartService")
public class MergeCartServiceImpl implements MergeCartService {

    @Resource(name = "blOrderService")
    protected OrderService orderService;

    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;

    @Resource(name = "blFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;

    @Resource(name = "blMergeCartServiceExtensionManager")
    protected MergeCartServiceExtensionManager extensionManager;

    @Override
    public MergeCartResponse mergeCart(Customer customer, Order anonymousCart)
            throws PricingException, RemoveFromCartException {
        return mergeCart(customer, anonymousCart, true);
    }

    @Override
    public ReconstructCartResponse reconstructCart(Customer customer) throws PricingException, RemoveFromCartException {
        return reconstructCart(customer, true);
    }

    @Override
    public MergeCartResponse mergeCart(Customer customer, Order anonymousCart, boolean priceOrder)
            throws PricingException, RemoveFromCartException {
        MergeCartResponse mergeCartResponse = new MergeCartResponse();
        mergeCartResponse.setMerged(false); // We no longer merge items, only transition cart states

        // We need to make sure that the old, saved customer cart is reconstructed with availability concerns in mind
        ReconstructCartResponse reconstructCartResponse = reconstructCart(customer, false);
        mergeCartResponse.setRemovedItems(reconstructCartResponse.getRemovedItems());
        Order customerCart = reconstructCartResponse.getOrder();
        
        if (anonymousCart != null && customerCart != null && anonymousCart.equals(customerCart)) {
            // The carts are the same, use either ensuring it's owned by the current customer
            setNewCartOwnership(anonymousCart, customer);
            mergeCartResponse.setOrder(anonymousCart);
        } else if (anonymousCart == null || anonymousCart.getOrderItems().size() == 0) {
            // The anonymous cart is of no use, use the customer cart
            mergeCartResponse.setOrder(customerCart);
            
            // The anonymous cart is owned by a different customer, so there is no chance for a single customer to have
            // multiple IN_PROCESS carts. We can go ahead and clean up this empty cart anyway since it's empty
            if (anonymousCart != null) {
                orderService.cancelOrder(anonymousCart);
            }
            
        } else if (customerCart == null || customerCart.getOrderItems().size() == 0) {
            // Delete the saved customer order since it is completely empty anyway. We do not want 2 IN_PROCESS orders
            // hanging around
            if (customerCart != null) {
                orderService.cancelOrder(customerCart);
            }
            
            // The customer cart is of no use, use the anonymous cart
            setNewCartOwnership(anonymousCart, customer);
            mergeCartResponse.setOrder(anonymousCart);
        } else {
            // Both carts have some items. The anonymous cart will always be the more recent one by definition
            // Save off the old customer cart and use the anonymous cart
            setSavedCartAttributes(customerCart);
            orderService.save(customerCart, false);

            setNewCartOwnership(anonymousCart, customer);
            mergeCartResponse.setOrder(anonymousCart);
        }
        
        if (mergeCartResponse.getOrder() != null) {
            Order savedCart = orderService.save(mergeCartResponse.getOrder(), priceOrder);
            mergeCartResponse.setOrder(savedCart);
        }
        
        return mergeCartResponse;
    }
    
    @Override
    public ReconstructCartResponse reconstructCart(Customer customer, boolean priceOrder)
            throws PricingException, RemoveFromCartException {
        ReconstructCartResponse reconstructCartResponse = new ReconstructCartResponse();
        Order customerCart = orderService.findCartForCustomer(customer);

        if (customerCart != null) {
            List<OrderItem> itemsToRemove = new ArrayList<OrderItem>();

            for (OrderItem orderItem : customerCart.getOrderItems()) {
                if (!checkActive(orderItem) || !checkInventory(orderItem) || !checkOtherValidity(orderItem)) {
                    itemsToRemove.add(orderItem);
                	  }
            	}
	         for (OrderItem item : itemsToRemove) {
	            orderService.removeItem(customerCart.getId(), item.getId(), false);
	            }

            reconstructCartResponse.setRemovedItems(itemsToRemove);
            customerCart = orderService.save(customerCart, priceOrder);
           }

        reconstructCartResponse.setOrder(customerCart);
        return reconstructCartResponse;
     }
    
    protected void setSavedCartAttributes(Order cart) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, ''yy");
        Date cartLastUpdated = cart.getAuditable().getDateUpdated();
        
        cart.setName("Previously saved cart - " + sdf.format(cartLastUpdated));
        cart.setStatus(OrderStatus.NAMED);
    }

    protected void setNewCartOwnership(Order cart, Customer customer) {
        cart.setCustomer(customer);
        extensionManager.getHandlerProxy().setNewCartOwnership(cart, customer);
    }

    /**
     * @param orderItem
     * @return whether or not the discrete order item's sku is active
     */
    protected boolean checkActive(OrderItem orderItem) {
        return orderItem.getSku().isActive(null, null);
    }

    /**
     * By default, Broadleaf does not provide an inventory check. This is set up as an extension point if your
     * application needs it.
     * 
     * @param orderItem
     * @return whether or not the item is in stock
     */
    protected boolean checkInventory(OrderItem orderItem) {
        return true;
    }

    /**
     * By default, Broadleaf does not provide additional validity checks. This is set up as an extension point if your
     * application needs it.
     * 
     * @param orderItem
     * @return whether or not the orderItem is valid
     */
    protected boolean checkOtherValidity(OrderItem orderItem) {
        return true;
    }

}
