package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.pricing.service.PricingService;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import javax.annotation.Resource;

public class PricingServiceActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    @Resource(name="blPricingService")
    private PricingService pricingService;

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        CheckoutSeed seed = context.getSeedData();
        Order order = pricingService.executePricing(seed.getOrder());
        seed.setOrder(order);

        return context;
    }

}
