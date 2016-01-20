package cn.globalph.b2c.pricing.service;

import javax.annotation.Resource;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.b2c.workflow.Processor;
import cn.globalph.b2c.workflow.WorkflowException;

import org.springframework.stereotype.Service;

/**
 * @author felix.wu
 *
 */
@Service("blPricingService")
public class PricingServiceImpl implements PricingService {

    @Resource(name="blPricingWorkflow")
    protected Processor pricingWorkflow;

    public Order executePricing(Order order) throws PricingException {
        try {
            ProcessContext<Order> context = (ProcessContext<Order>) pricingWorkflow.doActivities(order);
            Order response = context.getSeedData();

            return response;
        } catch (WorkflowException e) {
            throw new PricingException("Unable to execute pricing for order -- id: " + order.getId(), e);
        }
    }
}
