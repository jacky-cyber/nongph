package cn.globalph.b2c.workflow.state;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.checkout.service.workflow.CheckoutSeed;
import cn.globalph.b2c.workflow.Activity;
import cn.globalph.b2c.workflow.ProcessContext;

import org.springframework.stereotype.Component;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Component("blNullCheckoutRollbackHandler")
public class NullCheckoutRollbackHandler implements RollbackHandler<CheckoutSeed> {

    private static final Log LOG = LogFactory.getLog(NullCheckoutRollbackHandler.class);

    @Override
    public void rollbackState(Activity<? extends ProcessContext<CheckoutSeed>> activity, ProcessContext<CheckoutSeed> processContext,
                              Map<String, Object> stateConfiguration) throws RollbackFailureException {

        LOG.warn("NullCheckoutRollbackHandler invoked - Override to provide a " +
                "mechanism to save any compensating transactions when an error occurs during checkout.");
        LOG.warn("******************* Activity: " + activity.getBeanName() + " *********************");
        RollbackStateLocal rollbackStateLocal = RollbackStateLocal.getRollbackStateLocal();
        LOG.warn("******************* Workflow: " + rollbackStateLocal.getWorkflowId() + " *********************");
        LOG.warn("******************* Thread: " + rollbackStateLocal.getThreadId() + " *********************");

    }
}
