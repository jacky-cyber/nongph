package cn.globalph.b2c.web.order.security;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.MergeCartService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.call.MergeCartResponse;
import cn.globalph.b2c.order.service.exception.RemoveFromCartException;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.common.security.MergeCartProcessor;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;
import cn.globalph.passport.web.core.security.CustomerStateRequestProcessor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @deprecated this has been replaced by invoking {@link MergeCartService} explicitly within the
 * {@link CartStateRequestProcessor}
 */
@Deprecated
@Component("blMergeCartProcessor")
public class MergeCartProcessorImpl implements MergeCartProcessor {

    protected String mergeCartResponseKey = "bl_merge_cart_response";

    @Resource(name="blCustomerService")
    protected CustomerService customerService;

    @Resource(name="blOrderService")
    protected OrderService orderService;
    
    @Resource(name="blMergeCartService")
    protected MergeCartService mergeCartService;
    
    @Resource(name = "blCustomerStateRequestProcessor")
    protected CustomerStateRequestProcessor customerStateRequestProcessor;
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        execute(new ServletWebRequest(request, response), authResult);
    }
    
    public void execute(WebRequest request, Authentication authResult) {
        Customer loggedInCustomer = customerService.readCustomerByUsername(authResult.getName());
        Customer anonymousCustomer = customerStateRequestProcessor.getAnonymousCustomer(request);
        
        Order cart = null;
        if (anonymousCustomer != null) {
            cart = orderService.findCartForCustomer(anonymousCustomer);
        }
        MergeCartResponse mergeCartResponse;
        try {
            mergeCartResponse = mergeCartService.mergeCart(loggedInCustomer, cart);
        } catch (PricingException e) {
            throw new RuntimeException(e);
        } catch (RemoveFromCartException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute(mergeCartResponseKey, mergeCartResponse, WebRequest.SCOPE_GLOBAL_SESSION);
    }

    public String getMergeCartResponseKey() {
        return mergeCartResponseKey;
    }

    public void setMergeCartResponseKey(String mergeCartResponseKey) {
        this.mergeCartResponseKey = mergeCartResponseKey;
    }

}
