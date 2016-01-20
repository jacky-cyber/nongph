package cn.globalph.b2c.api.rest.endpoint.checkout;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.globalph.b2c.order.domain.wrap.OrderPaymentWrap;
import cn.globalph.b2c.order.domain.wrap.OrderWrap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.util.List;

/**
 * This is a reference REST API endpoint for checkout. This can be modified, used as is, or removed. 
 * The purpose is to provide an out of the box RESTful checkout service implementation, but also 
 * to allow the implementor to have fine control over the actual API, URIs, and general JAX-RS annotations.
 * 
 * @author felix.wu
 *
 */
@Component
@Scope("singleton")
@Path("/cart/{cartId}")
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class CheckoutEndpoint extends cn.globalph.b2c.api.endpoint.checkout.CheckoutEndpoint {

    @GET
    @Path("payments")
    public List<OrderPaymentWrap> findPaymentsForOrder(@Context HttpServletRequest request,
    		@PathParam("cartId") Long cartId) {
        return super.findPaymentsForOrder(cartId);
    }

    @POST
    @Path("payments")
    public OrderPaymentWrap addPaymentToOrder(@Context HttpServletRequest request,
    											 @PathParam("cartId") Long cartId,
                                                 OrderPaymentWrap wrap) {
        return super.addPaymentToOrder(cartId, wrap);
    }

    @DELETE
    @Path("payments")
    public OrderWrap removePaymentFromOrder( @Context HttpServletRequest request,
    										 @PathParam("cartId") Long cartId,
                                             OrderPaymentWrap wrap ) {
        return super.removePaymentFromOrder(cartId, wrap);
    }

    @POST
    @Path("checkout")
    public OrderWrap performCheckout(@Context HttpServletRequest request,
    								 @PathParam("cartId") Long cartId) {
        return super.performCheckout(cartId);
    }
}
