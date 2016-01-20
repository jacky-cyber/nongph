package cn.globalph.b2c.checkout.service.workflow;

import java.util.Map;

import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.workflow.Activity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.b2c.workflow.state.RollbackFailureException;
import cn.globalph.b2c.workflow.state.RollbackHandler;

import org.springframework.stereotype.Component;


/**
 * Rollback handler to execute after an order has been marked as 'CONFIRMED' and there is an exception.
 *
 *  1. Change the status back to SUBMITTED
 *  2. Change the order number back to null
 *  3. Change the submit date back to null
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("phConfirmOrderRollbackHandler")
public class ConfirmOrderRollbackHandler implements RollbackHandler<CheckoutSeed> {

    @Override
    public void rollbackState(Activity<? extends ProcessContext<CheckoutSeed>> activity, ProcessContext<CheckoutSeed> processContext, Map<String, Object> stateConfiguration) throws RollbackFailureException {
        CheckoutSeed seed = processContext.getSeedData();
        seed.getOrder().setStatus(OrderStatus.SUBMITTED);
    }

}
