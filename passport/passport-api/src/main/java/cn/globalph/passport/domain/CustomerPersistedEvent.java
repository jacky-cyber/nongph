package cn.globalph.passport.domain;

import org.springframework.context.ApplicationEvent;


/**
 * An event for whenever a {@link CustomerImpl} has been persisted
 *
 * @see {@link CustomerPersistedEntityListener}
 */
public class CustomerPersistedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    /**
     * @param customer the newly persisted customer
     */
    public CustomerPersistedEvent(Customer customer) {
        super(customer);
    }
    
    /**
     * Gets the newly-persisted {@link Customer} set by the {@link CustomerPersistedEntityListener}
     * 
     * @return
     */
    public Customer getCustomer() {
        return (Customer)source;
    }

}
