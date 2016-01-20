package cn.globalph.passport.web.core;

import cn.globalph.common.web.RequestCustomerResolverImpl;
import cn.globalph.passport.domain.Customer;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Convenient class to get the active customer from the current request. This state is kept up-to-date in regards to the database
 * throughout the lifetime of the request via the {@link CustomerStateRefresher}.
 */
@Component("blCustomerState")
public class CustomerState {
    
    public static Customer getCustomer(HttpServletRequest request) {
        return (Customer) RequestCustomerResolverImpl.getInstance().getCustomer(request);
    }
    
    public static Customer getCustomer(WebRequest request) {
        return (Customer) RequestCustomerResolverImpl.getInstance().getCustomer(request);
    }
    
    public static Customer getCustomer() {
        return (Customer) RequestCustomerResolverImpl.getInstance().getCustomer();
    }
    
    public static void setCustomer(Customer customer) {
        RequestCustomerResolverImpl.getInstance().setCustomer(customer);
    }

}
