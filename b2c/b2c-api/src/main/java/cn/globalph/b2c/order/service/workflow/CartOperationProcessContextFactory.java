package cn.globalph.b2c.order.service.workflow;

import cn.globalph.b2c.workflow.DefaultProcessContextImpl;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.b2c.workflow.ProcessContextFactory;
import cn.globalph.b2c.workflow.WorkflowException;

/**
 * Provides a method that creates the seed ProcessContext object for a cart
 * operation. The same seed object is used for add item, update item, and remove
 * item cart operations.
 * 
 * @author apazzolini
 */
public class CartOperationProcessContextFactory implements ProcessContextFactory<CartOperationRequest, CartOperationRequest> {

    /**
     * Creates the necessary context for cart operations
     */
    public ProcessContext<CartOperationRequest> createContext(CartOperationRequest seedData) throws WorkflowException {
        if (!(seedData instanceof CartOperationRequest)){
            throw new WorkflowException("Seed data instance is incorrect. " +
                    "Required class is " + CartOperationRequest.class.getName() + " " +
                    "but found class: " + seedData.getClass().getName());
        }
        
        ProcessContext<CartOperationRequest> context = new DefaultProcessContextImpl<CartOperationRequest>();
        context.setSeedData((CartOperationRequest) seedData);
        return context;
    }

}
