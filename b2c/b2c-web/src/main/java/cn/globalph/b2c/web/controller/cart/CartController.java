package cn.globalph.b2c.web.controller.cart;

import cn.globalph.b2c.offer.service.exception.OfferMaxUseExceededException;
import cn.globalph.b2c.order.domain.NullOrderImpl;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.exception.AddToCartException;
import cn.globalph.b2c.order.service.exception.IllegalCartOperationException;
import cn.globalph.b2c.order.service.exception.ItemNotFoundException;
import cn.globalph.b2c.order.service.exception.RemoveFromCartException;
import cn.globalph.b2c.order.service.exception.UnuseFromCartException;
import cn.globalph.b2c.order.service.exception.UpdateCartException;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.web.order.CartState;
import cn.globalph.b2c.web.order.model.AddToCartItem;
import cn.globalph.common.util.BLCMessageUtils;
import cn.globalph.passport.web.core.CustomerState;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * In charge of performing the various modify cart operations
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class CartController extends AbstractCartController {
    
    protected static String cartView = "cart/cart";
    protected static String cartPageRedirect = "redirect:/cart";
    
    /**
    * Renders the cart page.
      * 
    * If the method was invoked via an AJAX call, it will render the "ajax/cart" template.
    * Otherwise, it will render the "cart" template.
     * 
    * Will reprice the order if the currency has been changed.
       * 
     * @param request
     * @param response
     * @param model
     * @throws PricingException
       */
    public String cart(HttpServletRequest request, HttpServletResponse response, Model model) throws PricingException {
        Order cart = CartState.getCart();
        if (cart != null && !(cart instanceof NullOrderImpl)) {
            Collections.sort(cart.getOrderItems());
            model.addAttribute("paymentRequestDTO", dtoTranslationService.translateOrder(CartState.getCart()));
        }
        return getCartView();
     }
    
    /**
     * Takes in an item request, adds the item to the customer's current cart, and returns.
     * If the method was invoked via an AJAX call, it will render the "ajax/cart" template.
     * Otherwise, it will perform a 302 redirect to "/cart"
     * @param request
     * @param response
     * @param model
     * @param itemRequest
     * @throws IOException
     * @throws AddToCartException 
     * @throws PricingException
       */
    public String add( HttpServletRequest request, 
    		           HttpServletResponse response, 
    		           Model model,
                       AddToCartItem itemRequest ) throws IOException, AddToCartException, PricingException  {
        Order cart = CartState.getCart();
           
        // If the cart is currently empty, it will be the shared, "null" cart. We must detect this
        // and provision a fresh cart for the current customer upon the first cart add
        if (cart == null || cart instanceof NullOrderImpl) {
            cart = orderService.createNewCartForCustomer( CustomerState.getCustomer(request) );
        }

        updateCartService.validateCart(cart);

        cart = orderService.addItem(cart.getId(), itemRequest, false);
        cart = orderService.save(cart,  true);
        
        return isAjaxRequest(request) ? getCartView() : getCartPageRedirect();
    }
    
    public void buyNow( HttpServletRequest request, 
	           HttpServletResponse response, 
	           Model model,
            AddToCartItem itemRequest ) throws IOException, AddToCartException, PricingException, UnuseFromCartException{
			Order cart = CartState.getCart();
			
			// If the cart is currently empty, it will be the shared, "null" cart. We must detect this
			// and provision a fresh cart for the current customer upon the first cart add
			if (cart == null || cart instanceof NullOrderImpl) {
			 cart = orderService.createNewCartForCustomer( CustomerState.getCustomer(request) );
			}else{
		        List<OrderItem> orderItems = cart.getOrderItems();
		        
		        if(orderItems != null){
		        	for(int i = 0;i < orderItems.size();i++){
		        		if(orderItems.get(i).isUsed()){
			        		cart = orderService.unuseItem(cart.getId(), orderItems.get(i).getId(), false, false);	        		
		        		}
		        	}
                    for(OrderItem orderItem : cart.getOrderItems()){
                        if(orderItem.getSku().getId().equals(itemRequest.getSkuId())){
                            orderItem.setQuantity(itemRequest.getQuantity());
                        }
                    }
		        	cart = orderService.save(cart, true);
		        }
			}
			
			updateCartService.validateCart(cart);

			cart = orderService.addItem(cart.getId(), itemRequest, false);
			cart = orderService.save(cart,  true);
	}
    
    
    /**
     * Takes in an item request, adds the item to the customer's current cart, and returns.
     * 
     * Calls the addWithOverrides method on the orderService which will honor the override
     * prices on the AddToCartItem request object.
     * 
     * Implementors must secure this method to avoid accidentally exposing the ability for 
     * malicious clients to override prices by hacking the post parameters.
     * 
     * @param request
     * @param response
     * @param model
     * @param itemRequest
     * @throws IOException
     * @throws AddToCartException 
     * @throws PricingException
     */
    public String addWithPriceOverride(HttpServletRequest request, HttpServletResponse response, Model model,
            AddToCartItem itemRequest) throws IOException, AddToCartException, PricingException {
        Order cart = CartState.getCart();

        // If the cart is currently empty, it will be the shared, "null" cart. We must detect this
        // and provision a fresh cart for the current customer upon the first cart add
        if (cart == null || cart instanceof NullOrderImpl) {
            cart = orderService.createNewCartForCustomer(CustomerState.getCustomer(request));
        }

        updateCartService.validateCart(cart);

        cart = orderService.addItemWithPriceOverrides(cart.getId(), itemRequest, false);
        cart = orderService.save(cart, true);

        return isAjaxRequest(request) ? getCartView() : getCartPageRedirect();
    }

    /**
     * Takes in an item request and updates the quantity of that item in the cart. If the quantity
     * was passed in as 0, it will remove the item.
     * 
     * If the method was invoked via an AJAX call, it will render the "ajax/cart" template.
     * Otherwise, it will perform a 302 redirect to "/cart"
     * 
     * @param request
     * @param response
     * @param model
     * @param itemRequest
     * @throws IOException
     * @throws PricingException
     * @throws UpdateCartException
     * @throws RemoveFromCartException 
     */
    public String updateQuantity(HttpServletRequest request, HttpServletResponse response, Model model,
            AddToCartItem itemRequest) throws IOException, UpdateCartException, PricingException, RemoveFromCartException {
        Order cart = CartState.getCart();

        cart = orderService.updateItemQuantity(cart.getId(), itemRequest, true);
        cart = orderService.save(cart, false);
        
        if (isAjaxRequest(request)) {
            Map<String, Object> extraData = new HashMap<String, Object>();
            extraData.put("skuId", itemRequest.getSkuId());
            extraData.put("cartItemCount", cart.getItemCount());
            model.addAttribute("blcextradata", new ObjectMapper().writeValueAsString(extraData));
            return getCartView();
        } else {
            return getCartPageRedirect();
        }
    }
    
    /**
     * Takes in an item request, updates the quantity of that item in the cart, and returns
     * 
     * If the method was invoked via an AJAX call, it will render the "ajax/cart" template.
     * Otherwise, it will perform a 302 redirect to "/cart"
     * 
     * @param request
     * @param response
     * @param model
     * @param itemRequest
     * @throws IOException
     * @throws PricingException
     * @throws RemoveFromCartException 
     */
    public String remove(HttpServletRequest request, HttpServletResponse response, Model model,
            AddToCartItem itemRequest) throws IOException, PricingException, RemoveFromCartException {
        Order cart = CartState.getCart();
        
        cart = orderService.removeItem(cart.getId(), itemRequest.getOrderItemId(), false);
        cart = orderService.save(cart, true);
        
        if (isAjaxRequest(request)) {
            Map<String, Object> extraData = new HashMap<String, Object>();
            extraData.put("cartItemCount", cart.getItemCount());
            extraData.put("skuId", itemRequest.getSkuId());
            model.addAttribute("blcextradata", new ObjectMapper().writeValueAsString(extraData));
            return getCartView();
        } else {
            return getCartPageRedirect();
        }
    }
    
    /**
     * 将一商品项从购物车中移除，但不是彻底的删除，只是在结算时不包括此商品项
     * @param request
     * @param response
     * @param model
     * @param itemRequest
     * @return
     * @throws IOException
     * @throws PricingException
     * @throws RemoveFromCartException
     */
    public String unuse(HttpServletRequest request, HttpServletResponse response, Model model,
            AddToCartItem itemRequest) throws IOException, PricingException, UnuseFromCartException {
        Order cart = CartState.getCart();
        
        cart = orderService.unuseItem(cart.getId(), itemRequest.getOrderItemId(), false, false);
        cart = orderService.save(cart, true);
        
        if (isAjaxRequest(request)) {
            Map<String, Object> extraData = new HashMap<String, Object>();
            extraData.put("cartItemCount", cart.getItemCount());
            extraData.put("skuId", itemRequest.getSkuId());
            model.addAttribute("blcextradata", new ObjectMapper().writeValueAsString(extraData));
            return getCartView();
        } else {
            return getCartPageRedirect();
        }
    }
    
    public String useAll(HttpServletRequest request, HttpServletResponse response, Model model) 
    		throws IOException, PricingException, AddToCartException {
    	  Order cart = CartState.getCart();
          
          if (cart == null || cart instanceof NullOrderImpl) {
              cart = orderService.createNewCartForCustomer( CustomerState.getCustomer(request) );
          }

          updateCartService.validateCart(cart);

          List<OrderItem> orderItems = cart.getOrderItems();
          if(orderItems != null){
        	  List<OrderItem> toUseOrderItems = new ArrayList<OrderItem>();
        	  for(OrderItem orderItem : orderItems){
        		  if(!orderItem.isUsed()){
        			  toUseOrderItems.add(orderItem);
        		  }
        	  }
        	  for(OrderItem orderItem : toUseOrderItems){
    			  AddToCartItem itemRequest = new AddToCartItem();
    			  itemRequest.setOrderItemId(orderItem.getId());
    			  itemRequest.setSkuId(orderItem.getSku().getId());
    			  itemRequest.setQuantity(0);
    			  cart = orderService.addItem(cart.getId(), itemRequest, false);
    			  cart = orderService.save(cart,  true);
        	  } 
          }  
          return getCartPageRedirect();
    }
    
    public String unuseAll(HttpServletRequest request, HttpServletResponse response, Model model) 
    		throws IOException, PricingException, UnuseFromCartException {
        Order cart = CartState.getCart();
        
        List<OrderItem> orderItems = cart.getOrderItems();
        
        if(orderItems != null){
        	for(int i = 0;i < orderItems.size();i++){
        		if(orderItems.get(i).isUsed()){
	        		cart = orderService.unuseItem(cart.getId(), orderItems.get(i).getId(), false, false);	        		
        		}
        	}
        	cart = orderService.save(cart, true);
        }
        
        return getCartPageRedirect();
    }
    
    
    /**
     * Cancels the current cart and redirects to the homepage
     * 
     * @param request
     * @param response
     * @param model
     * @throws PricingException
     */
    public String empty(HttpServletRequest request, HttpServletResponse response, Model model) throws PricingException {
        Order cart = CartState.getCart();
        orderService.cancelOrder(cart);
        CartState.setCart(null);
        return "redirect:/";
    }
    
    /** Attempts to add provided Offer to Cart
       * 
     * @param request
     * @param response
     * @param model
     * @param customerOffer
     * @return the return view
     * @throws IOException
     * @throws PricingException
     * @throws ItemNotFoundException
     * @throws OfferMaxUseExceededException 
     */
    public String addPromo(HttpServletRequest request, HttpServletResponse response, Model model,
            String customerOffer) throws IOException, PricingException {
        Order cart = CartState.getCart();
        
        Boolean promoAdded = false;
        String exception = "";
        
        if (cart != null && !(cart instanceof NullOrderImpl)) {
            exception = "Invalid Code";
        } else {
            exception = "Invalid cart";
           }
        
        if (isAjaxRequest(request)) {
            Map<String, Object> extraData = new HashMap<String, Object>();
            extraData.put("promoAdded", promoAdded);
            extraData.put("exception" , exception);
            model.addAttribute("blcextradata", new ObjectMapper().writeValueAsString(extraData));
            return getCartView();
        } else {
            model.addAttribute("exception", exception);
            return getCartView();
        }
        
    }
    
    /** 
      *删除代金拳，以后实现 
     * @param request
     * @param response
     * @param model
     * @return the return view
     * @throws IOException
     * @throws PricingException
     * @throws ItemNotFoundException
     * @throws OfferMaxUseExceededException 
     */
    public String removePromo(HttpServletRequest request, HttpServletResponse response, Model model,
            Long offerCodeId) throws IOException, PricingException {
        Order cart = CartState.getCart();
        return isAjaxRequest(request) ? getCartView() : getCartPageRedirect();
     }

    public String getCartView() {
        return cartView;
    }

    public String getCartPageRedirect() {
        return cartPageRedirect;
    }
    
    public Map<String, String> handleIllegalCartOpException(IllegalCartOperationException ex) {
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("error", "illegalCartOperation");
        returnMap.put("exception", BLCMessageUtils.getMessage(ex.getType()));
        return returnMap;
    }
    

}
