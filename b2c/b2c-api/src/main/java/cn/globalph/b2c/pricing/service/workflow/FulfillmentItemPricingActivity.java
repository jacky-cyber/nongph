package cn.globalph.b2c.pricing.service.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.common.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Called during the pricing workflow to set each item's merchandise total and taxable total
 * 
 * @author felix.wu 
 */
public class FulfillmentItemPricingActivity extends BaseActivity<ProcessContext<Order>> {
    
    private static final Log LOG = LogFactory.getLog(FulfillmentItemPricingActivity.class);

    /**
     * Returns the order adjustment value or zero if none exists
     * @param order
     * @return
     */
    protected Money getOrderSavingsToDistribute(Order order) {
        if (order.getOrderAdjustmentsValue() == null) {
            return Money.ZERO;
        } else {
            Money adjustmentValue = order.getOrderAdjustmentsValue();
            
            Money orderSubTotal = order.getSubTotal();
            if (orderSubTotal == null || orderSubTotal.lessThan(adjustmentValue)) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Subtotal is null or less than orderSavings in DistributeOrderSavingsActivity.java.  " +
                            "No distribution is taking place.");
                }
                return Money.ZERO;
            } 
            return adjustmentValue;
        }
    }
    
    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();
        Map<OrderItem,List<FulfillmentGroupItem>> partialOrderItemMap = new HashMap<OrderItem,List<FulfillmentGroupItem>>();

        // Calculate the fulfillmentGroupItem total
        populateItemTotalAmount(order, partialOrderItemMap);
        fixItemTotalRoundingIssues(order, partialOrderItemMap);
        
        // Calculate the fulfillmentGroupItem prorated orderSavings
        Money totalAllItemsAmount = calculateTotalPriceForAllFulfillmentItems(order);
        Money totalOrderAdjustmentDistributed = distributeOrderSavingsToItems(order, totalAllItemsAmount.getAmount());
        fixOrderSavingsRoundingIssues(order, totalOrderAdjustmentDistributed);
        
        context.setSeedData(order);

        return context;
    }

    /**
     * Sets the fulfillment amount which includes the relative portion of the total price for 
     * the corresponding order item.
     * 
     * @param order
     * @param partialOrderItemMap
     */
    protected void populateItemTotalAmount(Order order, Map<OrderItem, List<FulfillmentGroupItem>> partialOrderItemMap) {
        for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            for (FulfillmentGroupItem fgItem : fulfillmentGroup.getFulfillmentGroupItems()) {
                OrderItem orderItem = fgItem.getOrderItem();
                int fgItemQty = fgItem.getQuantity();
                int orderItemQty = orderItem.getQuantity();
                Money totalItemAmount = orderItem.getTotalPrice();

                if (fgItemQty != orderItemQty) {
                    // We need to keep track of all of these items in case we need to distribute a remainder 
                    // to one or more of the items.
                    List<FulfillmentGroupItem> fgItemList = partialOrderItemMap.get(orderItem);
                    if (fgItemList == null) {
                        fgItemList = new ArrayList<FulfillmentGroupItem>();
                        partialOrderItemMap.put(orderItem, fgItemList);
                    }
                    fgItemList.add(fgItem);
                    fgItem.setTotalItemAmount(totalItemAmount.multiply(fgItemQty).divide(orderItemQty));
                } else {
                    fgItem.setTotalItemAmount(totalItemAmount);
                }
            }
        }
    }

    /**
     * Because an item may have multiple price details that don't round cleanly, we may have pennies
     * left over that need to be distributed.
     * 
     * @param order
     * @param partialOrderItemMap
     */
    protected void fixItemTotalRoundingIssues(Order order, Map<OrderItem, List<FulfillmentGroupItem>> partialOrderItemMap) {
        for (OrderItem orderItem : partialOrderItemMap.keySet()) {
            Money totalItemAmount = orderItem.getTotalPrice();
            Money totalFGItemAmount = sumItemAmount(partialOrderItemMap.get(orderItem), order);
            Money amountDiff = totalItemAmount.subtract(totalFGItemAmount);

            if (!(amountDiff.getAmount().compareTo(BigDecimal.ZERO) == 0)) {
                long numApplicationsNeeded = countNumberOfUnits(amountDiff);
                Money unitAmount = getUnitAmount(amountDiff);
                for (FulfillmentGroupItem fgItem : partialOrderItemMap.get(orderItem)) {
                    numApplicationsNeeded = numApplicationsNeeded -
                            applyDifferenceToAmount(fgItem, numApplicationsNeeded, unitAmount);
                    if (numApplicationsNeeded == 0) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Returns the total price for all fulfillment items.
     * @param order
     * @return
     */
    protected Money calculateTotalPriceForAllFulfillmentItems(Order order) {
        Money totalAllItemsAmount = Money.ZERO;
        for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            for (FulfillmentGroupItem fgItem : fulfillmentGroup.getFulfillmentGroupItems()) {
                totalAllItemsAmount = totalAllItemsAmount.add(fgItem.getTotalItemAmount());
            }
        }
        return totalAllItemsAmount;
    }

    /**
     * Distributes the order adjustments (if any) to the individual fulfillment group items.
     * @param order
     * @param totalAllItems
     * @return
     */
    protected Money distributeOrderSavingsToItems(Order order, BigDecimal totalAllItems) {
        Money returnAmount = Money.ZERO;

        BigDecimal orderAdjAmt = order.getOrderAdjustmentsValue().getAmount();

        for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            for (FulfillmentGroupItem fgItem : fulfillmentGroup.getFulfillmentGroupItems()) {
                BigDecimal fgItemAmount = fgItem.getTotalItemAmount().getAmount();
                BigDecimal proratedAdjAmt = totalAllItems.compareTo(BigDecimal.ZERO) == 0 ? totalAllItems : orderAdjAmt.multiply(fgItemAmount).divide(totalAllItems, RoundingMode.FLOOR);
                fgItem.setProratedOrderAdjustmentAmount(new Money(proratedAdjAmt));
                returnAmount = returnAmount.add(fgItem.getProratedOrderAdjustmentAmount());
            }
        }
        return returnAmount;
    }

    /**
     * It is possible due to rounding that the order adjustments do not match the 
     * total.   This method fixes by adding or removing the pennies.
     * @param order
     * @param partialOrderItemMap
     */
    protected void fixOrderSavingsRoundingIssues(Order order, Money totalOrderAdjustmentDistributed) {
        if (!order.getHasOrderAdjustments()) {
            return;
        }

        Money orderAdjustmentTotal = order.getOrderAdjustmentsValue();
        Money amountDiff = totalOrderAdjustmentDistributed.subtract(orderAdjustmentTotal);

        if (!(amountDiff.getAmount().compareTo(BigDecimal.ZERO) == 0)) {
            long numApplicationsNeeded = countNumberOfUnits(amountDiff);
            Money unitAmount = getUnitAmount(amountDiff);

            for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
                for (FulfillmentGroupItem fgItem : fulfillmentGroup.getFulfillmentGroupItems()) {
                    numApplicationsNeeded = numApplicationsNeeded -
                            applyDifferenceToProratedAdj(fgItem, numApplicationsNeeded, unitAmount);
                    if (numApplicationsNeeded == 0) {
                        break;
                    }
                }
            }
        }
    }

    protected Money sumItemAmount(List<FulfillmentGroupItem> items, Order order) {
        Money totalAmount = Money.ZERO;
        for (FulfillmentGroupItem fgItem : items) {
            totalAmount = totalAmount.add(fgItem.getTotalItemAmount());
        }
        return totalAmount;
    }

    public long countNumberOfUnits(Money difference) {
        double numUnits = difference.multiply( Math.pow(10, Money.currency.getDefaultFractionDigits())).doubleValue();
        return Math.round(numUnits);
    }

    /**
     * Returns the unit amount (e.g. .01 for US)
     * @param currency
     * @return
     */
    public Money getUnitAmount(Money difference) {
        BigDecimal divisor = new BigDecimal(Math.pow(10, Money.currency.getDefaultFractionDigits()));
        BigDecimal unitAmount = new BigDecimal("1").divide(divisor);

        if (difference.lessThan(BigDecimal.ZERO)) {
            unitAmount = unitAmount.negate();
        }
        return new Money(unitAmount);
    }

    public long applyDifferenceToAmount(FulfillmentGroupItem fgItem, long numApplicationsNeeded, Money unitAmount) {
        BigDecimal numTimesToApply = new BigDecimal(Math.min(numApplicationsNeeded, fgItem.getQuantity()));

        Money oldAmount = fgItem.getTotalItemAmount();
        Money changeToAmount = unitAmount.multiply(numTimesToApply);

        fgItem.setTotalItemAmount(oldAmount.add(changeToAmount));
        return numTimesToApply.longValue();
    }

    public long applyDifferenceToProratedAdj(FulfillmentGroupItem fgItem, long numApplicationsNeeded, Money unitAmount) {
        BigDecimal numTimesToApply = new BigDecimal(Math.min(numApplicationsNeeded, fgItem.getQuantity()));

        Money oldAmount = fgItem.getProratedOrderAdjustmentAmount();
        Money changeToAmount = unitAmount.multiply(numTimesToApply);

        fgItem.setProratedOrderAdjustmentAmount(oldAmount.add(changeToAmount));
        return numTimesToApply.longValue();
    }
}
