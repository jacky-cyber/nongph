package cn.globalph.passport.web.core;

import cn.globalph.common.web.WebRequestContext;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerPersistedEvent;
import cn.globalph.passport.web.core.security.CustomerStateRequestProcessor;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

/**
 * {@link ApplicationListener} responsible for updating {@link CustomerState} as well as invalidating the session-based
 * customer, if one existed previously. For instance, when originally browsing the catalog a Customer is created but only
 * stored in session and retrieved from session on subsequent requests. However, once the Customer has been persisted
 * (like when they add something to the cart) then this component is responsible for updating {@link CustomerState} as well
 * as invalidating the session-based customer.
 *
 */
@Component("blCustomerStateRefresher")
public class CustomerStateRefresher implements ApplicationListener<CustomerPersistedEvent> {

    /**
     * Removes the complete {@link Customer} stored in session and adds a new session variable for just the customer ID. This
     * should occur once the session-based {@link Customer} (all anonymous Customers start out this way) has been persisted.
     * 
     * <p>Also updates {@link CustomerState} with the persisted {@link Customer} so that it will always represent the most
     * up-to-date version that is in the database</p>
     * 
     * @param request
     * @param databaseCustomer
     */
    @Override
    public void onApplicationEvent(final CustomerPersistedEvent event) {
        Customer dbCustomer = event.getCustomer();

        //if there is an active request, remove the session-based customer if it exists and update CustomerState
        WebRequest request = WebRequestContext.getWebRequestContext().getWebRequest();
        if (request != null) {
            String customerAttribute = CustomerStateRequestProcessor.SESSION_ATTRIBUTE_NAME_ANONYMOUS_CUSTOMER;
            String customerIdAttribute = CustomerStateRequestProcessor.SESSION_ATTRIBUTE_NAME_ANONYMOUS_CUSTOMER_ID;
            Customer sessionCustomer = (Customer) request.getAttribute(customerAttribute, WebRequest.SCOPE_GLOBAL_SESSION);
            //invalidate the session-based customer if it's there and the ID is the same as the Customer that has been
            //persisted
            if (sessionCustomer != null && sessionCustomer.getId() != null && sessionCustomer.getId().equals(dbCustomer.getId())) {
                request.removeAttribute(customerAttribute, WebRequest.SCOPE_GLOBAL_SESSION);
                request.setAttribute(customerIdAttribute, dbCustomer.getId(), WebRequest.SCOPE_GLOBAL_SESSION);
            }
            
            //Update CustomerState if the persisted Customer ID is the same
            if (CustomerState.getCustomer() != null && CustomerState.getCustomer().getId() != null  && CustomerState.getCustomer().getId().equals(dbCustomer.getId())) {
                //Copy transient fields from the customer that existed in CustomerState, prior to the DB refresh, 
                //to the customer that has been saved (merged) in the DB....
                Customer preMergedCustomer = CustomerState.getCustomer();
                resetTransientFields(preMergedCustomer, dbCustomer);

                CustomerState.setCustomer(dbCustomer);
            }
        }
    }
    
    /**
     * After a JPA merge occurs, there is a new object created representing the merged changes.  The new object does 
     * not reflect the state of transient fields that may have been set on the object that was merged.
     * 
     * This method, by default, resets the state of transient properties. 
     * and allows the user to override this method to set additional (or different) transient values.
     * 
     * @param preMergedCustome
     * @param postMergedCustomer
     */
    protected void resetTransientFields(Customer preMergedCustomer, Customer postMergedCustomer) {
        postMergedCustomer.setUnencodedPassword(preMergedCustomer.getUnencodedPassword());
        postMergedCustomer.setUnencodedChallengeAnswer(preMergedCustomer.getUnencodedChallengeAnswer());
        postMergedCustomer.setAnonymous(preMergedCustomer.isAnonymous());
        postMergedCustomer.setCookied(preMergedCustomer.isCookied());
        postMergedCustomer.setLoggedIn(preMergedCustomer.isLoggedIn());
    }

}
