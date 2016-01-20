package cn.globalph.b2c.api.endpoint.order;

import org.apache.commons.lang.StringUtils;

import cn.globalph.b2c.api.WebServicesException;
import cn.globalph.b2c.api.endpoint.BaseEndpoint;
import cn.globalph.b2c.api.endpoint.catalog.BasicCatalogEndpoint;
import cn.globalph.b2c.offer.service.OfferService;
import cn.globalph.b2c.order.domain.NullOrderImpl;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.wrap.OrderWrap;
import cn.globalph.b2c.order.domain.wrapper.OrderWrapper;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.call.OrderItemRequestDTO;
import cn.globalph.b2c.order.service.exception.AddToCartException;
import cn.globalph.b2c.order.service.exception.ItemNotFoundException;
import cn.globalph.b2c.order.service.exception.RemoveFromCartException;
import cn.globalph.b2c.order.service.exception.UpdateCartException;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerImpl;
import cn.globalph.passport.service.CustomerService;

import java.util.HashMap;
import java.util.Set;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * This endpoint depends on JAX-RS to provide cart services.  It should be extended by components that actually wish 
 * to provide an endpoint.  The annotations such as @Path, @Scope, @Context, @PathParam, @QueryParam, 
 * @GET, @POST, @PUT, and @DELETE are purposely not provided here to allow implementors finer control over 
 * the details of the endpoint.
 */
public abstract class CartEndpoint extends BaseEndpoint {

    @Resource(name="blOrderService")
    protected OrderService orderService;

    @Resource(name="blOfferService")
    protected OfferService offerService;

    @Resource(name="blCustomerService")
    protected CustomerService customerService;
    
    protected OrderWrapper orderWrapper;
   /**
     * Search for {@code Order} by {@code Customer}
     *
     * @return the cart for the customer
     */
    public OrderWrap findCartForCustomer(Long customerId) {
    	Customer c = new CustomerImpl();
    	c.setId( customerId );
    	Order cart = orderService.findCartForCustomer( c );
    	return orderWrapper.wrapDetails(cart);
    }

   /**
     * Create a new {@code Order} for {@code Customer}
     *
     * @return the cart for the customer
     */
    public OrderWrap createNewCartForCustomer(Long customerId) {
    	Customer customer = null;
        if (customerId == null) {
            customer = customerService.createCustomerFromId(null);
        } else {
        	customer = customerService.getCustomerById( customerId );
        }

        Order cart = orderService.findCartForCustomer(customer);
        if (cart == null) {
            cart = orderService.createNewCartForCustomer(customer);
        }

        return orderWrapper.wrapDetails(cart);
    }

    /**
     * This method takes in a categoryId and productId as path parameters.  In addition, query parameters can be supplied including:
     * 
     * <li>skuId</li>
     * <li>quantity</li>
     * <li>priceOrder</li>
     * 
     * You must provide a ProductId OR ProductId with product options. Product options can be posted as form or querystring parameters. 
     * You must pass in the ProductOption attributeName as the key and the 
     * ProductOptionValue attributeValue as the value.  See {@link BasicCatalogEndpoint}.
     * 
     * @param request
     * @param categoryId
     * @param productId
     * @param quantity
     * @param priceOrder
     * @return OrderWrapper
     */
    public OrderWrap addProductToOrder( Long skuId, Long cartId, int quantity, boolean priceOrder) {
        Order cart = orderService.getOrderById( cartId );
        try {
            OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO();
            orderItemRequestDTO.setSkuId(skuId);
            orderItemRequestDTO.setQuantity(quantity);
            Order order = orderService.addItem(cart.getId(), orderItemRequestDTO, priceOrder);
            order = orderService.save(order, priceOrder);
            return orderWrapper.wrapDetails(order);
        } catch (PricingException e) {
            throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, e);
        } catch (AddToCartException e) {
            throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, e);
        }
    }

    protected HashMap<String, String> getOptions(UriInfo uriInfo) {
        MultivaluedMap<String, String> multiValuedMap = uriInfo.getQueryParameters();
        HashMap<String, String> productOptions = new HashMap<String, String>();

        //Fill up a map of key values that will represent product options
        Set<String> keySet = multiValuedMap.keySet();
        for (String key : keySet) {
            if (multiValuedMap.getFirst(key) != null) {
                //Product options should be returned with "productOption." as a prefix. We'll look for those, and 
                //remove the prefix.
                if (key.startsWith("productOption.")) {
                    productOptions.put(StringUtils.removeStart(key, "productOption."), multiValuedMap.getFirst(key));
                }
            }
        }
        return productOptions;
    }

    public OrderWrap removeItemFromOrder( Long cartId, Long itemId, boolean priceOrder) {
    	Order cart = orderService.getOrderById( cartId );

        try {
            Order order = orderService.removeItem(cart.getId(), itemId, priceOrder);
            order = orderService.save(order, priceOrder);
            return orderWrapper.wrapDetails(order);
        } catch (PricingException e) {
            throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, e)
                    .addMessage(WebServicesException.CART_PRICING_ERROR);
        } catch (RemoveFromCartException e) {
            if (e.getCause() instanceof ItemNotFoundException) {
                throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode(), null, null, e.getCause())
                        .addMessage(WebServicesException.CART_ITEM_NOT_FOUND, itemId);
            } else {
                throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, e);
            }
        }
    }

    public OrderWrap updateItemQuantity( Long cartId, Long itemId, Integer quantity,
            							    boolean priceOrder) {

    	Order cart = orderService.getOrderById( cartId );
        if (cart == null || cart instanceof NullOrderImpl) {
            throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CART_NOT_FOUND);
        }
        
        try {
            OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO();
            orderItemRequestDTO.setOrderItemId(itemId);
            orderItemRequestDTO.setQuantity(quantity);
            Order order = orderService.updateItemQuantity(cart.getId(), orderItemRequestDTO, priceOrder);
            order = orderService.save(order, priceOrder);
            return orderWrapper.wrapDetails(order);
        } catch (UpdateCartException e) {
            if (e.getCause() instanceof ItemNotFoundException) {
                throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode(), null, null, e.getCause())
                        .addMessage(WebServicesException.CART_ITEM_NOT_FOUND, itemId);
            } else {
                throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, e)
                        .addMessage(WebServicesException.UPDATE_CART_ERROR);
            }
        } catch (RemoveFromCartException e) {
            if (e.getCause() instanceof ItemNotFoundException) {
                throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode(), null, null, e.getCause())
                        .addMessage(WebServicesException.CART_ITEM_NOT_FOUND, itemId);
            } else {
                throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, e)
                        .addMessage(WebServicesException.UPDATE_CART_ERROR);
            }
        } catch (PricingException pe) {
           throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, pe)
                    .addMessage(WebServicesException.CART_PRICING_ERROR);
        }
    }

    public OrderWrap updateProductOptions( Long cartId, Long itemId, boolean priceOrder ) {
    	Order cart = orderService.getOrderById( cartId );
        if (cart != null) {
            try {
                OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO();
                orderItemRequestDTO.setOrderItemId(itemId);
                //If we have product options set them on the DTO
                Order order = orderService.updateProductOptionsForItem(cart.getId(), orderItemRequestDTO, priceOrder);
                order = orderService.save(order, priceOrder);
                return orderWrapper.wrapDetails(cart);
            } catch (UpdateCartException e) {
                if (e.getCause() instanceof ItemNotFoundException) {
                    throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode(), null, null, e.getCause())
                    .addMessage(WebServicesException.CART_ITEM_NOT_FOUND, itemId);
                } else {
                    throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, e)
                            .addMessage(WebServicesException.UPDATE_CART_ERROR);
                }
            } catch (PricingException pe) {
                throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, pe)
                        .addMessage(WebServicesException.CART_PRICING_ERROR);
            }
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CART_NOT_FOUND);
    }
}
