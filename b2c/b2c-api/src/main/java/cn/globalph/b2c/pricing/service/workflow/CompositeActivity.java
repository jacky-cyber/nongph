package cn.globalph.b2c.pricing.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.b2c.workflow.Processor;

public class CompositeActivity extends BaseActivity<ProcessContext<Order>> {

    private Processor workflow;

    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        ProcessContext<Order> subContext = (ProcessContext<Order>) workflow.doActivities(context.getSeedData());
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
