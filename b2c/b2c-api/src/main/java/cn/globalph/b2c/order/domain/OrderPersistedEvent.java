package cn.globalph.b2c.order.domain;

import org.springframework.context.ApplicationEvent;


/**
 * An event for whenever an {@link OrderImpl} has been persisted
 *
 * @author Phillip Verheyden (phillipuniverse)
 * 
 * @see {@link OrderPersistedEntityListener}
 */
public class OrderPersistedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    /**
     * @param order the newly persisted customer
     */
    public OrderPersistedEvent(Order order) {
        super(order);
    }
    
    /**
     * Gets the newly-persisted {@link Order} set by the {@link OrderPersistedEntityListener}
     * 
     * @return
     */
    public Order getOrder() {
        return (Order)source;
    }

}
