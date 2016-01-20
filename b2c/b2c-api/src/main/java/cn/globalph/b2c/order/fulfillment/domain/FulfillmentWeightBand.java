package cn.globalph.b2c.order.fulfillment.domain;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.type.FulfillmentBandResultAmountType;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.common.util.WeightUnitOfMeasureType;

import java.math.BigDecimal;

/**
 * <p>This entity defines the bands that can be specified for {@link BandedWeightFulfillmentOption}. Bands
 * work on the cumulated weight of an {@link Order} and should be calculated as follows:</p>
 * <ol>
 *  <li>The weight of all of the {@link OrderItem}s (via the relationship to {@link Sku}) in a {@link FulfillmentGroup} (which
 *  is obtained through their relationship with {@link FulfillmentGroupItem} are summed together</li>
 *  <li>The {@link FulfillmentWeightBand} should be looked up by getting the closest band less
 *  than the sum of the weights</li>
 *  <li>If {@link #getResultAmountType()} returns {@link FulfillmentBandResultAmountType#RATE}, then
 *  the cost for the fulfillment group is whatever is defined in {@link #getResultAmount()}</li>
 *  <li>If {@link #getResultAmountType()} returns {@link FulfillmentBandResultAmountType#PERCENTAGE}, then
 *  the fulfillment cost is the percentage obtained by {@link #getResultAmount()} * retailPriceTotal</li>
 *  <li>If two bands have the same weight, the cheapest resulting amount is used</li>
 * </ol>
 * <p>Note: this implementation assumes that units of measurement (lb, kg, etc) are the same across the site implementation</p>
 *
 * @author Phillip Verheyden
 * 
 */
public interface FulfillmentWeightBand extends FulfillmentBand {

    public BigDecimal getMinimumWeight();
    
    public void setMinimumWeight(BigDecimal weight);
    
    public BandedWeightFulfillmentOption getOption();

    public void setOption(BandedWeightFulfillmentOption option);
    
    public WeightUnitOfMeasureType getWeightUnitOfMeasure();
    
    public void setWeightUnitOfMeasure(WeightUnitOfMeasureType weightUnitOfMeasure);

}
