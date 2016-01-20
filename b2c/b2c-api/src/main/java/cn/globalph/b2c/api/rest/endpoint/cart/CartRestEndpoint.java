package cn.globalph.b2c.api.rest.endpoint.cart;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.globalph.b2c.order.domain.wrap.OrderWrap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * This is a reference REST API endpoint for cart. This can be modified, used as is, or removed. 
 * The purpose is to provide an out of the box RESTful cart service implementation, but also 
 * to allow the implementor to have fine control over the actual API, URIs, and general JAX-RS annotations.
 */
@Component
@Scope("singleton")
@Path("/cart/")
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class CartRestEndpoint extends cn.globalph.b2c.api.endpoint.order.CartEndpoint {

    @GET
    @Path("{customerId}")
    public OrderWrap findCartForCustomer( @Context HttpServletRequest request,
    									  @PathParam("customerId") Long customerId ) {
        return super.findCartForCustomer(customerId);
    }

    @POST
    @Path("create")
    public OrderWrap createNewCartForCustomer( @Context HttpServletRequest request,
    										   @QueryParam("customerId") Long customerId) {
        return super.createNewCartForCustomer(customerId);
    }

    @POST
    @Path("{skuId}")
    public OrderWrap addProductToOrder( @Context HttpServletRequest request,
								        @Context UriInfo uriInfo,
								        @PathParam("skuId") Long skuId,
								        @QueryParam("categoryId") Long categoryId,
								        @QueryParam("quantity") @DefaultValue("1") int quantity,
								        @QueryParam("priceOrder") @DefaultValue("true") boolean priceOrder) {
        return super.addProductToOrder(skuId, categoryId, quantity, priceOrder);
    }

    @DELETE
    @Path("{cartId}/items/{itemId}")
    public OrderWrap removeItemFromOrder( @Context HttpServletRequest request,
    									  @PathParam("cartId") Long cartId,
            							  @PathParam("itemId") Long itemId,
            							  @QueryParam("priceOrder") @DefaultValue("true") boolean priceOrder) {
        return super.removeItemFromOrder(cartId, itemId, priceOrder);
    }

    @PUT
    @Path("{cartId}/items/{itemId}")
    public OrderWrap updateItemQuantity(@Context HttpServletRequest request,
    		@PathParam("cartId") Long cartId,
            @PathParam("itemId") Long itemId,
            @QueryParam("quantity") Integer quantity,
            @QueryParam("priceOrder") @DefaultValue("true") boolean priceOrder) {
        return super.updateItemQuantity(cartId, itemId, quantity, priceOrder);
    }

    @PUT
    @Path("{cartId}/items/{itemId}/options")
    public OrderWrap updateProductOptions(@Context HttpServletRequest request,
            @Context UriInfo uriInfo,
            @PathParam("cartId") Long cartId,
            @PathParam("itemId") Long itemId,
            @QueryParam("priceOrder") @DefaultValue("true") boolean priceOrder) {
        return super.updateProductOptions(cartId, itemId, priceOrder);
    }
}
