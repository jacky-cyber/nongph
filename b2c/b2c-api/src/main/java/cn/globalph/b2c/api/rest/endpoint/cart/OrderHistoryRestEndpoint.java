package cn.globalph.b2c.api.rest.endpoint.cart;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.globalph.b2c.order.domain.wrap.OrderWrap;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * 
 * @author felix.wu
 *
 */
@Component
@Scope("singleton")
@Path("/orders/")
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class OrderHistoryRestEndpoint extends cn.globalph.b2c.api.endpoint.order.OrderHistoryEndpoint {

    @GET
    public List<OrderWrap> findOrdersForCustomer(@Context HttpServletRequest request,
    		 @QueryParam("customerId") Long customerId,
                                                 @QueryParam("orderStatus") @DefaultValue("CONFIRMED") String orderStatus) {
        return super.findOrdersForCustomer(customerId, orderStatus);
    }

}
