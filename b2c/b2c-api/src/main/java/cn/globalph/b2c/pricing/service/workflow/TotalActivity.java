package cn.globalph.b2c.pricing.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.common.money.Money;

/**
 * The TotalActivity is responsible for calculating and setting totals for a given order.
 * It must set the sum of the the taxes in the appropriate places as well as fulfillment
 * group subtotals / totals and order subtotals / totals.
 * 
 * @author felix.wu
 *
 */
public class TotalActivity extends BaseActivity<ProcessContext<Order>> {

    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();
        
        Money total = Money.ZERO;
        total = total.add(order.getSubTotal());
        total = total.subtract(order.getOrderAdjustmentsValue());
        total = total.subtract(order.getCouponDiscount());
        total = total.subtract(order.getCouponCodeDiscount());
        total = total.subtract(new Money(order.getDeductionBonusPoint()*0.01));
//        total = total.add(order.getTotalFulfillmentCharges());

        /*Money fees = Money.ZERO;
        for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            Money fgTotal = Money.ZERO;
            fgTotal = fgTotal.add(fulfillmentGroup.getMerchandiseTotal());
            fgTotal = fgTotal.add(fulfillmentGroup.getFulfillmentPrice());
            
            for (FulfillmentGroupFee fulfillmentGroupFee : fulfillmentGroup.getFulfillmentGroupFees()) {
                fgTotal = fgTotal.add(fulfillmentGroupFee.getAmount());
                fees = fees.add(fulfillmentGroupFee.getAmount());
            }
            
            fulfillmentGroup.setTotal(fgTotal);
        }

        total = total.add(fees);*/


        //calculate pickup off
        /*Money pickupOff = Money.ZERO;
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            if (DeliveryType.PICKUP.getType().equals(orderItem.getDeliveryType())) {
                pickupOff = pickupOff.add(new Money(5));
            }
        }
        order.setPickupOff(pickupOff);
        total = total.subtract(pickupOff);*/
        if (total.lessThan(new Money(0))) {
            total = new Money(0);
        }

        order.setTotal(total);
        
        context.setSeedData(order);
        return context;
    }
}
