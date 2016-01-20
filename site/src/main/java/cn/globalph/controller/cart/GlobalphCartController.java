package cn.globalph.controller.cart;

import cn.globalph.b2c.inventory.service.InventoryUnavailableException;
import cn.globalph.b2c.order.service.exception.AddToCartException;
import cn.globalph.b2c.order.service.exception.ProductOptionValidationException;
import cn.globalph.b2c.order.service.exception.RemoveFromCartException;
import cn.globalph.b2c.order.service.exception.RequiredAttributeNotProvidedException;
import cn.globalph.b2c.order.service.exception.UpdateCartException;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.web.controller.cart.CartController;
import cn.globalph.b2c.web.order.CartState;
import cn.globalph.b2c.web.order.model.AddToCartItem;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 购物车
 * @author fwu
 */
@Controller
@RequestMapping("/cart")
public class GlobalphCartController extends CartController {
    
    @Override
    @RequestMapping("")
    public String cart(HttpServletRequest request, HttpServletResponse response, Model model) throws PricingException {
        return super.cart(request, response, model);
    }
    
    /*
    * Does not show the cart until a product is added. Instead, when the product is added via an AJAX
    * POST that requests JSON, we only need to return a few attributes to update the state of the page. The most
    * efficient way to do this is to call the regular add controller method, but instead return a map that contains
    * the necessary attributes. By using the @ResposeBody tag, Spring will automatically use Jackson to convert the
    * returned object into JSON for easy processing via JavaScript.
     */
    @RequestMapping(value = "/add", produces = "application/json")
    public @ResponseBody Map<String, Object> addJson( HttpServletRequest request, 
    		                                          HttpServletResponse response, 
    		                                          Model model,
                                                      @ModelAttribute("addToCartItem") AddToCartItem addToCartItem ) throws IOException, PricingException, AddToCartException {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        try {
            super.add(request, response, model, addToCartItem);
            	
            Sku sku = catalogService.findSkuById( addToCartItem.getSkuId() );
            responseMap.put("skuId", addToCartItem.getSkuId());
            responseMap.put("skuName", sku.getName());
            responseMap.put("quantityAdded", addToCartItem.getQuantity());
            responseMap.put("cartItemCount", String.valueOf(CartState.getCart().getItemCount()));
        } catch (AddToCartException e) {
            if (e.getCause() instanceof RequiredAttributeNotProvidedException) {
                responseMap.put("error", "allOptionsRequired");
            } else if (e.getCause() instanceof ProductOptionValidationException) {
                ProductOptionValidationException exception = (ProductOptionValidationException) e.getCause();
                responseMap.put("error", "productOptionValidationError");
                responseMap.put("errorCode", exception.getErrorCode());
                responseMap.put("errorMessage", exception.getMessage());
                //blMessages.getMessage(exception.get, lfocale))
            } else if (e.getCause() instanceof InventoryUnavailableException) {
                responseMap.put("error", "inventoryUnavailable");
            } else {
                throw e;
            }
        }
        
        return responseMap;
    }
    
      /*
     * Does not support adding products with required product options from a category browse page
     * when JavaScript is disabled. When this occurs, we will redirect the user to the full product details page 
     * for the given product so that the required options may be chosen.
       */
    @RequestMapping(value = "/add", produces = { "text/html", "*/*" })
    public String add( HttpServletRequest request, 
    		           HttpServletResponse response, 
    		           Model model, 
    		           RedirectAttributes redirectAttributes,
                       @ModelAttribute("addToCartItem") AddToCartItem addToCartItem) throws IOException, PricingException, AddToCartException {
        try {
            return super.add(request, response, model, addToCartItem);
        } catch (AddToCartException e) {
            Product product = catalogService.findSkuById(addToCartItem.getSkuId()).getProduct();
            return "redirect:" + product.getUrl();
        }
    }
    
    @RequestMapping("/updateQuantity")
    public String updateQuantity( HttpServletRequest request, 
    		                      HttpServletResponse response, 
    		                      Model model, 
    		                      RedirectAttributes redirectAttributes,
                                  @ModelAttribute("addToCartItem") AddToCartItem addToCartItem) throws IOException, PricingException, UpdateCartException, RemoveFromCartException {
        try {
            return super.updateQuantity(request, response, model, addToCartItem);
        } catch (UpdateCartException e) {
            if (e.getCause() instanceof InventoryUnavailableException) {
                // Since there was an exception, the order gets detached from the Hibernate session. This re-attaches it
                CartState.setCart(orderService.getOrderById(CartState.getCart().getId()));
                if (isAjaxRequest(request)) {
                    model.addAttribute("errorMessage", "Not enough inventory to fulfill your requested amount of " + addToCartItem.getQuantity());
                    return getCartView();
                } else {
                    redirectAttributes.addAttribute("errorMessage", "Not enough inventory to fulfill your requested amount of " + addToCartItem.getQuantity());
                    return getCartPageRedirect();
                }
            } else {
                throw e;
            }
        }
    }
    
    @Override
    @RequestMapping("/remove")
    public String remove( HttpServletRequest request, 
    		              HttpServletResponse response, 
    		              Model model,
                          @ModelAttribute("addToCartItem") AddToCartItem addToCartItem) throws IOException, PricingException, RemoveFromCartException {
        return super.remove(request, response, model, addToCartItem);
    }
    
    @Override
    @RequestMapping("/empty")
    public String empty(HttpServletRequest request, HttpServletResponse response, Model model) throws PricingException {
        //return super.empty(request, response, model);
        return "ajaxredirect:/";
        
    }
    
    /**
     * 添加代金券，以后实现
     */
    @Override
    @RequestMapping("/promo")
    public String addPromo( HttpServletRequest request, 
    		                HttpServletResponse response, Model model,
                            @RequestParam("promoCode") String customerOffer ) throws IOException, PricingException {
        return super.addPromo(request, response, model, customerOffer);
    }
    
    /**
     * 删除代金券，以后实现
     */
    @Override
    @RequestMapping("/promo/remove")
    public String removePromo( HttpServletRequest request, 
    		                   HttpServletResponse response, Model model,
                               @RequestParam("offerCodeId") Long offerCodeId ) throws IOException, PricingException {
        return super.removePromo(request, response, model, offerCodeId);
    }
}