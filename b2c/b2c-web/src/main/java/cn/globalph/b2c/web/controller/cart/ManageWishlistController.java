package cn.globalph.b2c.web.controller.cart;

import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.exception.AddToCartException;
import cn.globalph.b2c.order.service.exception.RemoveFromCartException;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.web.order.model.AddToCartItem;
import cn.globalph.common.web.controller.BasicController;
import cn.globalph.passport.web.core.CustomerState;

import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * The controller responsible for wishlist management activities, including
 * viewing a wishlist, moving items from the wishlist to the cart, and
 * removing items from the wishlist
 */
public class ManageWishlistController extends BasicController {
    
    protected static String accountWishlistView = "account/manageWishlist";
    protected static String accountWishlistRedirect = "redirect:/account/wishlist";

    @Resource(name="blOrderService")
    protected OrderService orderService;

    @Resource(name="blCatalogService")
    protected CatalogService catalogService;
    
    public String add(HttpServletRequest request, HttpServletResponse response, Model model,
            AddToCartItem itemRequest, String wishlistName) throws IOException, AddToCartException, PricingException  {
        Order wishlist = orderService.findNamedOrderForCustomer(wishlistName, CustomerState.getCustomer(request));

        if (wishlist == null) {
            wishlist = orderService.createNamedOrderForCustomer(wishlistName, CustomerState.getCustomer(request));
        }
        
        wishlist = orderService.addItem(wishlist.getId(), itemRequest, true);
        
        return getAccountWishlistRedirect();
    }
    
    public String viewWishlist(HttpServletRequest request, HttpServletResponse response, Model model,
            String wishlistName) {
        Order wishlist = orderService.findNamedOrderForCustomer(wishlistName, CustomerState.getCustomer());
        model.addAttribute("wishlist", wishlist);
        return getAccountWishlistView();
    }

    public String removeItemFromWishlist(HttpServletRequest request, HttpServletResponse response, Model model,
            String wishlistName, Long itemId) throws RemoveFromCartException {
        Order wishlist = orderService.findNamedOrderForCustomer(wishlistName, CustomerState.getCustomer());
        
        orderService.removeItem(wishlist.getId(), itemId, false);

        model.addAttribute("wishlist", wishlist);
        return getAccountWishlistRedirect();
    }

    public String moveItemToCart(HttpServletRequest request, HttpServletResponse response, Model model, 
            String wishlistName, Long orderItemId) throws RemoveFromCartException, AddToCartException {
        Order wishlist = orderService.findNamedOrderForCustomer(wishlistName, CustomerState.getCustomer());
        List<OrderItem> orderItems = wishlist.getOrderItems();

        OrderItem orderItem = null;
        for (OrderItem item : orderItems) {
            if (orderItemId.equals(item.getId())) {
                orderItem = item;
                break;
            }
        }

        if (orderItem != null) {
            orderService.addItemFromNamedOrder(wishlist, orderItem, true);
        } else {
            throw new IllegalArgumentException("The item id provided was not found in the wishlist");
        }

        model.addAttribute("wishlist", wishlist);

        return getAccountWishlistRedirect();
    }
    
    public String moveListToCart(HttpServletRequest request, HttpServletResponse response, Model model, 
            String wishlistName) throws RemoveFromCartException, AddToCartException {
        Order wishlist = orderService.findNamedOrderForCustomer(wishlistName, CustomerState.getCustomer());
        
        orderService.addAllItemsFromNamedOrder(wishlist, true);
        
        model.addAttribute("wishlist", wishlist);
        return getAccountWishlistRedirect();
    }

    public String getAccountWishlistView() {
        return accountWishlistView;
    }
    
    public String getAccountWishlistRedirect() {
        return accountWishlistRedirect;
    }

}
