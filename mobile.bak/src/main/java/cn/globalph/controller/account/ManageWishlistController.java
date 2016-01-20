package cn.globalph.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.globalph.b2c.order.service.exception.AddToCartException;
import cn.globalph.b2c.order.service.exception.RemoveFromCartException;
import cn.globalph.b2c.order.service.exception.RequiredAttributeNotProvidedException;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.web.order.model.AddToCartItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/account/wishlist")
public class ManageWishlistController extends cn.globalph.b2c.web.controller.cart.ManageWishlistController {

    public static final String WISHLIST_ORDER_NAME = "wishlist";

    @RequestMapping(method = RequestMethod.GET)
    public String viewAccountWishlist(HttpServletRequest request, HttpServletResponse response, Model model) {
        return super.viewWishlist(request, response, model, WISHLIST_ORDER_NAME);
    }
    
    @RequestMapping(value = "/add", produces = "application/json")
    public @ResponseBody Map<String, Object> addJson(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute("addToCartItem") AddToCartItem addToCartItem) throws IOException, PricingException, AddToCartException {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        try {
            super.add(request, response, model, addToCartItem, WISHLIST_ORDER_NAME);
            
            responseMap.put("skuName", catalogService.findSkuById(addToCartItem.getSkuId()).getName());
            responseMap.put("quantityAdded", addToCartItem.getQuantity());
            responseMap.put("skuId", addToCartItem.getSkuId());
        } catch (AddToCartException e) {
            if (e.getCause() instanceof RequiredAttributeNotProvidedException) {
                responseMap.put("error", "allOptionsRequired");
            } else {
                throw e;
            	}
        }
        
        return responseMap;
    }
    
    /**
    * The Heat Clinic does not support adding products with required product options from a category browse page
    * when JavaScript is disabled. When this occurs, we will redirect the user to the full product details page 
    * for the given product so that the required options may be chosen.
     */
    @RequestMapping(value = "/add", produces = "text/html")
    public String add(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute("addToCartItem") AddToCartItem addToCartItem) throws IOException, PricingException, AddToCartException {
        try {
            return super.add(request, response, model, addToCartItem, WISHLIST_ORDER_NAME);
        } catch (AddToCartException e) {
            if (e.getCause() instanceof RequiredAttributeNotProvidedException) {
                Product product = catalogService.findSkuById(addToCartItem.getSkuId()).getProduct();
                return "redirect:" + product.getUrl();
            } else {
                throw e;
            }
        }
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String removeItemFromWishlist(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute("orderItemId") Long itemId) throws RemoveFromCartException {
        return super.removeItemFromWishlist(request, response, model, WISHLIST_ORDER_NAME, itemId);
    }

    @RequestMapping(value = "/moveItemToCart", method = RequestMethod.POST)
    public String moveItemToCart(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute("itemId") Long itemId) throws IOException, PricingException, AddToCartException, RemoveFromCartException {
        return super.moveItemToCart(request, response, model, WISHLIST_ORDER_NAME, itemId);   
    }

    @RequestMapping(value = "/moveListToCart", method = RequestMethod.POST)
    public String moveListToCart(HttpServletRequest request, HttpServletResponse response, Model model)
            throws IOException, PricingException, AddToCartException, RemoveFromCartException {
        return super.moveListToCart(request, response, model, WISHLIST_ORDER_NAME);  
    }

}
