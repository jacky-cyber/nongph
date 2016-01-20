package cn.globalph.controller.cart;

import cn.globalph.b2c.inventory.service.type.InventoryType;
import cn.globalph.b2c.order.domain.OrderItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.globalph.b2c.inventory.service.InventoryUnavailableException;
import cn.globalph.b2c.order.service.exception.AddToCartException;
import cn.globalph.b2c.order.service.exception.ProductOptionValidationException;
import cn.globalph.b2c.order.service.exception.RemoveFromCartException;
import cn.globalph.b2c.order.service.exception.RequiredAttributeNotProvidedException;
import cn.globalph.b2c.order.service.exception.UnuseFromCartException;
import cn.globalph.b2c.order.service.exception.UpdateCartException;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.web.controller.cart.CartController;
import cn.globalph.b2c.web.order.CartState;
import cn.globalph.b2c.web.order.model.AddToCartItem;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
            Sku sku = catalogService.findSkuById(addToCartItem.getSkuId());
            List<OrderItem> orderItems = CartState.getCart().getOrderItems();
            Integer cartQuantity = 0;
            if (orderItems != null && orderItems.size() != 0) {
                for (OrderItem orderItem : orderItems) {
                    if (orderItem.getSku().getId().equals(addToCartItem.getSkuId())) {
                        cartQuantity = orderItem.getQuantity();
                        break;
                    }
                }
            }

            if (InventoryType.CHECK_QUANTITY.equals(sku.getInventoryType()) && cartQuantity + addToCartItem.getQuantity() > sku.getQuantityAvailable()) {
                responseMap.put("notEnoughQuantity", "notEnough");
            } else {
                super.add(request, response, model, addToCartItem);
            }
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
            }else {
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
                    model.addAttribute("errorMessage", "库存不足 " + addToCartItem.getQuantity() + "件");
                    return getCartView();
                } else {
                    redirectAttributes.addAttribute("errorMessage", "库存不足" + addToCartItem.getQuantity() + "件");
                    return getCartPageRedirect();
                }
            } else {
                throw e;
            }
        }
    }
    
    @RequestMapping(value = "/buyNow/{skuId}/{quantity}")
    public String buyNow( HttpServletRequest request, 
    		           HttpServletResponse response, 
    		           Model model, 
    		           RedirectAttributes redirectAttributes,
                       @PathVariable(value = "skuId") Long skuId,@PathVariable(value = "quantity") Integer quantity) throws IOException, PricingException, AddToCartException {
    	AddToCartItem addToCartItem = new AddToCartItem();
    	addToCartItem.setQuantity(quantity);
    	addToCartItem.setSkuId(skuId);
    	try {
            super.buyNow(request, response, model, addToCartItem);
        } catch (AddToCartException e) {
            Product product = catalogService.findSkuById(addToCartItem.getSkuId()).getProduct();
            return "redirect:" + product.getUrl();
        } catch(UnuseFromCartException e){
            Product product = catalogService.findSkuById(addToCartItem.getSkuId()).getProduct();
            return "redirect:" + product.getUrl();
        }
    	
    	return "redirect:/checkout";
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
    @RequestMapping("/unuse")
    public String unuse( HttpServletRequest request, 
    		              HttpServletResponse response, 
    		              Model model,
                          @ModelAttribute("addToCartItem") AddToCartItem addToCartItem) throws IOException, PricingException, UnuseFromCartException {
        return super.unuse(request, response, model, addToCartItem);
    }
    
    @Override
    @RequestMapping("/useAll")
    public String useAll( HttpServletRequest request, 
    		              HttpServletResponse response, 
    		              Model model) throws IOException, PricingException, AddToCartException {
        return super.useAll(request, response, model);
    }
    
    @Override
    @RequestMapping("/unuseAll")
    public String unuseAll( HttpServletRequest request, 
    		              HttpServletResponse response, 
    		              Model model) throws IOException, PricingException, UnuseFromCartException {
        return super.unuseAll(request, response, model);
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