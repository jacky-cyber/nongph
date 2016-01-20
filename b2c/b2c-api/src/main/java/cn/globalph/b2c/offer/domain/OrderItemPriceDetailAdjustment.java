package cn.globalph.b2c.offer.domain;

import cn.globalph.b2c.order.domain.OrderItemPriceDetail;
import cn.globalph.common.money.Money;

/**
 * Records the actual adjustments that were made to an OrderItemPriceDetail.
 * 
 * @author bpolster
 *
 */
public interface OrderItemPriceDetailAdjustment extends Adjustment {

    /**
     * Stores the offer name at the time the adjustment was made.   Primarily to simplify display 
     * within the admin.
     * 
     * @return
     */
    public String getOfferName();

    /**
     * Returns the name of the offer at the time the adjustment was made.
     * @param offerName
     */
    public void setOfferName(String offerName);

    public OrderItemPriceDetail getOrderItemPriceDetail();

    public void init(OrderItemPriceDetail orderItemPriceDetail, Offer offer, String reason);

    public void setOrderItemPriceDetail(OrderItemPriceDetail orderItemPriceDetail);

    /**
     * Even for items that are on sale, it is possible that an adjustment was made
     * to the retail price that gave the customer a better offer.
     *
     * Since some offers can be applied to the sale price and some only to the
     * retail price, this setting provides the required value.
     *
     * @return true if this adjustment was applied to the sale price
     */
    public boolean isAppliedToSalePrice();

    public void setAppliedToSalePrice(boolean appliedToSalePrice);

    /**
     * Value of this adjustment relative to the retail price.
     * @return
     */
    public Money getRetailPriceValue();

    public void setRetailPriceValue(Money retailPriceValue);

    /**
     * Value of this adjustment relative to the sale price.
     *
     * @return
     */
    public Money getSalesPriceValue();

    public void setSalesPriceValue(Money salesPriceValue);
}
