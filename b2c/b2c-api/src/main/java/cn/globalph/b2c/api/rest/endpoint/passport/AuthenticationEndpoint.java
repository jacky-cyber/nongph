package cn.globalph.b2c.api.rest.endpoint.passport;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.globalph.passort.domain.wrap.CustomerWrap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Rest api，对用户进行授权
 */
@Component
@Scope("singleton")
@Path("/passport/authentication")
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class AuthenticationEndpoint{
    
    @POST
    public CustomerWrap createNewCartForCustomer(@Context HttpServletRequest request,
    		@QueryParam("loginName") String productId,
            @QueryParam("passord") Long categoryId) {
        return null;
    }
   
}
