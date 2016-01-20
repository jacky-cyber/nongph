package cn.globalph.b2c.pricing.service.workflow;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.common.money.Money;

/**
 * Called during the pricing workflow to set the merchandise total for each FulfillmentGroup
 * in an Order. This activity should come before any activity dealing with pricing FulfillmentGroups
 * 
 * @author felix.wu
 * @see {@link FulfillmentGroup#setMerchandiseTotal(Money)}, {@link FulfillmentGroup#getMerchandiseTotal()}
 */
public class FulfillmentGroupMerchandiseTotalActivity extends BaseActivity<ProcessContext<Order>> {

    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();

        for(FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            Money merchandiseTotal = Money.ZERO;
            for(FulfillmentGroupItem fulfillmentGroupItem : fulfillmentGroup.getFulfillmentGroupItems()) {
                OrderItem item = fulfillmentGroupItem.getOrderItem();
                merchandiseTotal = merchandiseTotal.add(item.getTotalPrice());
            }
            fulfillmentGroup.setMerchandiseTotal(merchandiseTotal);
        }
        context.setSeedData(order);

        return context;
    }
}