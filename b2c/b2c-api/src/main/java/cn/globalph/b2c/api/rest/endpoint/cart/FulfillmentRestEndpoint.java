package cn.globalph.b2c.api.rest.endpoint.cart;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.globalph.b2c.order.domain.wrap.FulfillmentGroupItemWrap;
import cn.globalph.b2c.order.domain.wrap.FulfillmentGroupWrap;
import cn.globalph.b2c.order.domain.wrap.FulfillmentOptionWrap;
import cn.globalph.b2c.order.domain.wrap.OrderWrap;

import java.util.List;

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

/**
 * This is a reference REST API endpoint for fulfillment groups. This can be modified, used as is, or removed. 
 * The purpose is to provide an out of the box RESTful fulfillment group service implementation, but also 
 * to allow the implementor to have fine control over the actual API, URIs, and general JAX-RS annotations.
 * 
 * @author felix.wu
 *
 */
@Component
@Scope("singleton")
@Path("/order")
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class FulfillmentRestEndpoint extends cn.globalph.b2c.api.endpoint.order.FulfillmentEndpoint {

    @GET
    @Path("{cartId}/fulfillment/groups")
    public List<FulfillmentGroupWrap> findFulfillmentGroupsForOrder(@Context HttpServletRequest request,
    		                                                   @PathParam("cartId") Long cartId) {
        return super.findFulfillmentGroupsForOrder(cartId);
    }

    @DELETE
    @Path("{cartId}/fulfillment/groups")
    public OrderWrap removeAllFulfillmentGroupsFromOrder(@Context HttpServletRequest request,
            @QueryParam("priceOrder") @DefaultValue("true") boolean priceOrder,
            @PathParam("cartId") Long cartId) {
        return super.removeAllFulfillmentGroupsFromOrder(cartId, priceOrder);
    }

    @POST
    @Path("{cartId}/fulfillment/group")
    public FulfillmentGroupWrap addFulfillmentGroupToOrder(@Context HttpServletRequest request,
    		@PathParam("cartId") Long cartId,
            FulfillmentGroupWrap wrapper,
            @QueryParam("priceOrder") @DefaultValue("true") boolean priceOrder) {
        return super.addFulfillmentGroupToOrder(cartId, wrapper, priceOrder);
    }

    @PUT
    @Path("{cartId}/fulfillment/group/{fulfillmentGroupId}")
    public FulfillmentGroupWrap addItemToFulfillmentGroup(@Context HttpServletRequest request,
    		@PathParam("cartId") Long cartId,
            @PathParam("fulfillmentGroupId") Long fulfillmentGroupId,
            FulfillmentGroupItemWrap wrapper,
            @QueryParam("priceOrder") @DefaultValue("true") boolean priceOrder) {
        return super.addItemToFulfillmentGroup(cartId, fulfillmentGroupId, wrapper, priceOrder);
    }

	@PUT
	@Path("{cartId}/fulfillment/group/{fulfillmentGroupId}/option/{fulfillmentOptionId}")
	public FulfillmentGroupWrap addFulfillmentOptionToFulfillmentGroup(
			@Context HttpServletRequest request, 
			@PathParam("cartId") Long cartId,
			@PathParam("fulfillmentGroupId") Long fulfillmentGroupId,
			@PathParam("fulfillmentOptionId") Long fulfillmentOptionId, 
			@QueryParam("priceOrder") @DefaultValue("true") boolean priceOrder) {
		return super.addFulfillmentOptionToFulfillmentGroup(cartId,fulfillmentGroupId, fulfillmentOptionId, priceOrder);
	}

	@GET
	@Path("options")
	public List<FulfillmentOptionWrap> findFulfillmentOptions(
			@Context HttpServletRequest request, 
			@QueryParam("fulfillmentType") String fulfillmentType) {
		return super.findFulfillmentOptions(fulfillmentType);
	}

    
}
