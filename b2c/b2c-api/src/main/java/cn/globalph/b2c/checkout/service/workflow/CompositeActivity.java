package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.b2c.workflow.Processor;

public class CompositeActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    private Processor workflow;

    /* (non-Javadoc)
     * @see cn.globalph.b2c.workflow.Activity#execute(cn.globalph.b2c.workflow.ProcessContext)
     */
    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        ProcessContext<CheckoutSeed> subContext = (ProcessContext<CheckoutSeed>) workflow.doActivities(context.getSeedData());
        if (subContext.isStopped()) {
            context.stopProcess();
        }

        return context;
    }

    public Processor getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Processor workflow) {
        this.workflow = workflow;
    }
}
