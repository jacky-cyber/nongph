package cn.globalph.b2c.order.fulfillment.domain;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.type.FulfillmentBandResultAmountType;

import java.math.BigDecimal;

/**
 * <p>This entity defines the bands that can be specified for {@link BandedPriceFulfillmentOption}. Bands
 * work on the retail price of an {@link Order} and should be calculated as follows:</p>
 * <ol>
 *  <li>The prices of all of the {@link OrderItems} in a {@link FulfillmentGroup} (which
 *  is obtained through their relationship with {@link FulfillmentGroupItem} are summed together</li>
 *  <li>The {@link FulfillmentPriceBand} should be looked up by getting the closest band less
 *  than the sum of the price</li>
 *  <li>If {@link #getResultAmountType()} returns {@link FulfillmentBandResultAmountType#RATE}, then
 *  the cost for the fulfillment group is whatever is defined in {@link #getResultAmount()}</li>
 *  <li>If {@link #getResultAmountType()} returns {@link FulfillmentBandResultAmountType#PERCENTAGE}, then
 *  the fulfillment cost is the percentage obtained by {@link #getResultAmount()} * retailPriceTotal</li>
 *  <li>If two bands have the same retail price minimum amount, the cheapest resulting amount is used</li>
 * </ol>
 * 
 * @author Phillip Verheyden
 * @see {@link BandedPriceFulfillmentOption}
 */
public interface FulfillmentPriceBand extends FulfillmentBand {

    /**
     * Gets the minimum amount that this band is valid for. If the addition
     * of all of the retail prices on all the {@link OrderItem}s in a {@link FulfillmentGroup}
     * comes to at least this amount, this band result amount will be applied to the
     * fulfillment cost.
     * 
     * @return the minimum retail price amount of the sum of the {@link OrderItem}s in a
     * {@link FulfillmentGroup} that this band qualifies for
     */
    public BigDecimal getRetailPriceMinimumAmount();

    /**
     * Set the minimum amount that this band is valid for. If the addition
     * of all of the retail prices on all the {@link OrderItem}s in a {@link FulfillmentGroup}
     * comes to at least this amount, this band result amount will be applied to the
     * fulfillment cost.
     * 
     * @param minimumRetailPriceAmount - the minimum retail price amount from adding up
     * the {@link OrderItem}s in a {@link FulfillmentGroup}
     */
    public void setRetailPriceMinimumAmount(BigDecimal retailPriceMinimumAmount);

    /**
     * Gets the {@link BandedPriceFulfillmentOption} that this band is associated to
     * 
     * @return the associated {@link BandedPriceFulfillmentOption}
     */
    public BandedPriceFulfillmentOption getOption();

    /**
     * Sets the {@link BandedPriceFulfillmentOption} to associate with this band
     * 
     * @param option
     */
    public void setOption(BandedPriceFulfillmentOption option);

}
