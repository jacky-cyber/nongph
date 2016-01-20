package cn.globalph.passport.web.core.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.web.AbstractRequestProcessor;
import cn.globalph.common.web.RequestCustomerResolver;
import cn.globalph.common.web.RequestCustomerResolverImpl;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;
import cn.globalph.passport.web.core.CustomerState;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * <P>
 * 当用户第一次访问站点时，一个 {@link Customer} 被创建并保存在session中，但是不存入数据库。
 * 然而，一旦用户添加SKU到购物车，这个匿名{@link Customer}被保存到数据库，此时再将此匿名{@link Customer}对象放入Session已无意义。
 * 因为相对于Hibernate (specifically with lists)，任何基于session的{@link Customer}将会过期。
 * 因此，一旦检测到基于session的{@link Customer}被持久，应该将其从session中删除。
 * 然后将匿名{@link Customer}放入session中，供以后获取。
 * </p>
 * @see {@link CustomerStateFilter}
 */
@Component("blCustomerStateRequestProcessor")
public class CustomerStateRequestProcessor extends AbstractRequestProcessor implements ApplicationEventPublisherAware {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    public static final String BLC_RULE_MAP_PARAM = "blRuleMap";

    @Resource(name="blCustomerService")
    protected CustomerService customerService;
    
    @Resource(name="blRequestCustomerResolver")
    protected RequestCustomerResolver requestCustomerResolver;
    
    protected ApplicationEventPublisher eventPublisher;

    //匿名客户名,在匿名客户持久到数据库之前引用
    public static final String SESSION_ATTRIBUTE_NAME_ANONYMOUS_CUSTOMER = "_nph_anonymousCustomer";
    
    //匿名客户ID，这个属性用来追踪匿名Customer，这些匿名客户未注册但是已经有状态在数据库中。
    public static final String SESSION_ATTRIBUTE_NAME_ANONYMOUS_CUSTOMER_ID = "_nph_anonymousCustomerId";
    private static final String SESSION_ATTRIBUTE_NAME_LAST_PUBLISHED_EVENT = "_nph_lastPublishedEvent";
    public static final String SESSION_ATTRIBUTE_NAME_OVERRIDE_CUSTOMER = "_nph_overrideCustomerId";

    @Override
    public void process(WebRequest request) {
        Customer customer = null;

        Long overrideId = (Long) request.getAttribute(SESSION_ATTRIBUTE_NAME_OVERRIDE_CUSTOMER, WebRequest.SCOPE_GLOBAL_SESSION);
        if (overrideId != null) {
            customer = customerService.getCustomerById(overrideId);
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if ((authentication != null) && !(authentication instanceof AnonymousAuthenticationToken)) {
                String userName = authentication.getName();
                customer = (Customer)requestCustomerResolver.getCustomer(request);
                if (userName != null && (customer == null || !userName.equals(customer.getLoginName()))) {
                    // can only get here if the authenticated user does not match the user in session
                    customer = customerService.readCustomerByUsername(userName);
                    if (logger.isDebugEnabled() && customer != null) {
                        logger.debug("Customer found by username " + userName);
                    }
                }
                if (customer != null) {
                    ApplicationEvent lastPublishedEvent = (ApplicationEvent) request.getAttribute(SESSION_ATTRIBUTE_NAME_LAST_PUBLISHED_EVENT, WebRequest.SCOPE_REQUEST);
                    if (authentication instanceof RememberMeAuthenticationToken) {
                        // set transient property of customer
                        customer.setCookied(true);
                        boolean publishRememberMeEvent = true;
                        if (lastPublishedEvent != null && lastPublishedEvent instanceof CustomerAuthenticatedFromCookieEvent) {
                            CustomerAuthenticatedFromCookieEvent cookieEvent = (CustomerAuthenticatedFromCookieEvent) lastPublishedEvent;
                            if (userName.equals(cookieEvent.getCustomer().getLoginName())) {
                                publishRememberMeEvent = false;
                            }
                        }
                        if (publishRememberMeEvent) {
                            CustomerAuthenticatedFromCookieEvent cookieEvent = new CustomerAuthenticatedFromCookieEvent(customer, this.getClass().getName()); 
                            eventPublisher.publishEvent(cookieEvent);
                            request.setAttribute(SESSION_ATTRIBUTE_NAME_LAST_PUBLISHED_EVENT, cookieEvent, WebRequest.SCOPE_REQUEST);
                        }                       
                    } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
                        customer.setLoggedIn(true);
                        boolean publishLoggedInEvent = true;
                        if (lastPublishedEvent != null && lastPublishedEvent instanceof CustomerLoggedInEvent) {
                            CustomerLoggedInEvent loggedInEvent = (CustomerLoggedInEvent) lastPublishedEvent;
                            if (userName.equals(loggedInEvent.getCustomer().getLoginName())) {
                                publishLoggedInEvent= false;
                            }
                        }
                        if (publishLoggedInEvent) {
                            CustomerLoggedInEvent loggedInEvent = new CustomerLoggedInEvent(customer, this.getClass().getName()); 
                            eventPublisher.publishEvent(loggedInEvent);
                            request.setAttribute(SESSION_ATTRIBUTE_NAME_LAST_PUBLISHED_EVENT, loggedInEvent, WebRequest.SCOPE_REQUEST);
                        }                        
                    } else {
                        customer = resolveAuthenticatedCustomer(authentication);
                    }
                }
            }
        }

        if (customer == null) {
            // This is an anonymous customer.
            // TODO: Handle a custom cookie (different than remember me) that is just for anonymous users.  
            // This can be used to remember their cart from a previous visit.
            // Cookie logic probably needs to be configurable - with TCS as the exception.
            customer = resolveAnonymousCustomer(request);
        }
        CustomerState.setCustomer(customer);

        // Setup customer for content rule processing
        Map<String,Object> ruleMap = (Map<String, Object>) request.getAttribute(BLC_RULE_MAP_PARAM, WebRequest.SCOPE_REQUEST);
        if (ruleMap == null) {
            ruleMap = new HashMap<String,Object>();
        }
        ruleMap.put("customer", customer);
        request.setAttribute(BLC_RULE_MAP_PARAM, ruleMap, WebRequest.SCOPE_REQUEST);
    }
    
    /**
     * Subclasses can extend to resolve other types of Authentication tokens
     * @param authentication
     * @return
     */
    public Customer resolveAuthenticatedCustomer(Authentication authentication) {
        return null;
    }

    /**
     * <p>Implementors can subclass to change how anonymous customers are created. Note that this method is intended to actually create the anonymous
     * customer if one does not exist. If you are looking to just get the current anonymous customer (if it exists) then instead use the
     * {@link #getAnonymousCustomer(WebRequest)} method.<p>
     * 
     * <p>The intended behavior of this method is as follows:</p>
     * <ul>
     *  <li>Look for a {@link Customer} on the session</li>
     *  <ul>
     *      <li>If a customer is found in session, keep using the session-based customer</li>
     *      <li>If a customer is not found in session</li>
     *      <ul>
     *          <li>Look for a customer ID in session</li>
     *          <li>If a customer ID is found in session:</li>
     *          <ul><li>Look up the customer in the database</ul></li>
     *      </ul>
     *      <li>If no there is no customer ID in session (and thus no {@link Customer})</li>
     *      <ol>
     *          <li>Create a new customer</li>
     *          <li>Put the newly-created {@link Customer} in session</li>
     *      </ol>
     *  </ul>
     * </ul>
     * 
     * @param request
     * @return
     * @see {@link #getAnonymousCustomer(WebRequest)}
     */
    public Customer resolveAnonymousCustomer(WebRequest request) {
        Customer customer;
        customer = getAnonymousCustomer(request);
        
        //If there is no Customer object in session, AND no customer id in session, create a new customer
        //and store the entire customer in session (don't persist to DB just yet)
        if (customer == null) {
            customer = customerService.createNewCustomer();
            request.setAttribute( SESSION_ATTRIBUTE_NAME_ANONYMOUS_CUSTOMER, customer, WebRequest.SCOPE_GLOBAL_SESSION);
        }
        customer.setAnonymous(true);

        return customer;
    }
    
    /**
     * Returns the anonymous customer that was saved in session. This first checks for a full customer in session (meaning
     * that the customer has not already been persisted) and returns that. If there is no full customer in session (and
     * there is instead just an anonymous customer ID) then this will look up the customer from the database using that and
     * return it.
     * 
     * @param request the current request
     * @return the anonymous customer in session or null if there is no anonymous customer represented in session
     * @see {@link #getAnonymousCustomerSessionAttributeName()} 
     * @see {@link #getAnonymousCustomerIdSessionAttributeName()}
     */
    public Customer getAnonymousCustomer(WebRequest request) {
        Customer anonymousCustomer = (Customer) request.getAttribute(SESSION_ATTRIBUTE_NAME_ANONYMOUS_CUSTOMER, WebRequest.SCOPE_GLOBAL_SESSION);
        if (anonymousCustomer == null) {
            //Customer is not in session, see if we have just a customer ID in session (the anonymous customer might have
            //already been persisted)
            Long customerId = (Long) request.getAttribute(SESSION_ATTRIBUTE_NAME_ANONYMOUS_CUSTOMER_ID, WebRequest.SCOPE_GLOBAL_SESSION);
            if (customerId != null) {
                //we have a customer ID in session, look up the customer from the database to ensure we have an up-to-date
                //customer to store in CustomerState
                anonymousCustomer = customerService.getCustomerById(customerId);
            }
        }
        return anonymousCustomer;
    }
    
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * The request-scoped attribute that should store the {@link Customer}.
     * 
     * <pre>
     * Customer customer = (Customer) request.getAttribute(CustomerStateRequestProcessor.getCustomerRequestAttributeName());
     * //this is equivalent to the above invocation
     * Customer customer = CustomerState.getCustomer();
     * </pre>
     * @return
     * @see {@link CustomerState}
     */
    public static String getCustomerRequestAttributeName() {
        return RequestCustomerResolverImpl.getInstance().getCustomerRequestAttributeName();
    } 
}