package cn.globalph.b2c.pricing.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.workflow.DefaultProcessContextImpl;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.b2c.workflow.ProcessContextFactory;
import cn.globalph.b2c.workflow.WorkflowException;

public class PricingProcessContextFactory implements ProcessContextFactory<Order, Order> {

    @Override
    public ProcessContext<Order> createContext(Order seedData) throws WorkflowException {
        ProcessContext<Order> context = new DefaultProcessContextImpl<Order>();
        context.setSeedData(seedData);

        return context;
    }

}
