package cn.globalph.b2c.pricing.service.workflow;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.pricing.service.FulfillmentPricingService;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.common.money.Money;

import javax.annotation.Resource;

/**
 * Called during the pricing workflow to compute all of the fulfillment costs
 * for all of the FulfillmentGroups on an Order and updates Order with the
 * total price of all of the FufillmentGroups
 * 
 * @author felix.wu
 * @see {@link FulfillmentGroup}, {@link Order}
 */
public class FulfillmentGroupPricingActivity extends BaseActivity<ProcessContext<Order>> {

    @Resource(name = "blFulfillmentPricingService")
    private FulfillmentPricingService fulfillmentPricingService;

    public void setFulfillmentPricingService(FulfillmentPricingService fulfillmentPricingService) {
        this.fulfillmentPricingService = fulfillmentPricingService;
    }

    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();

        /*
         * 1. Get FGs from Order
         * 2. take each FG and call shipping module with the shipping svc
         * 3. add FG back to order
         */
        Money totalFulfillmentCharges = Money.ZERO;
        for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            if (fulfillmentGroup != null) {
                if (!fulfillmentGroup.getShippingOverride()) {
                    fulfillmentGroup = fulfillmentPricingService.calculateCostForFulfillmentGroup(fulfillmentGroup);
                }
                if (fulfillmentGroup.getFulfillmentPrice() != null) {
                    totalFulfillmentCharges = totalFulfillmentCharges.add(fulfillmentGroup.getFulfillmentPrice());
                }
            }
        }
        order.setTotalFulfillmentCharges(totalFulfillmentCharges);
        context.setSeedData(order);

        return context;
    }

}
