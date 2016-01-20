package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.workflow.DefaultProcessContextImpl;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.b2c.workflow.ProcessContextFactory;
import cn.globalph.b2c.workflow.WorkflowException;

public class CheckoutProcessContextFactory implements ProcessContextFactory<CheckoutSeed, CheckoutSeed> {

    @Override
    public ProcessContext<CheckoutSeed> createContext(CheckoutSeed seedData) throws WorkflowException {
        ProcessContext<CheckoutSeed> context = new DefaultProcessContextImpl<CheckoutSeed>();
        context.setSeedData(seedData);

        return context;
    }

}
