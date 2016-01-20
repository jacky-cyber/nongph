package cn.globalph.b2c.order.fulfillment.domain;

import cn.globalph.b2c.order.domain.FulfillmentOption;

import java.util.List;

/**
 * 
 * @author Phillip Verheyden
 */
public interface BandedWeightFulfillmentOption extends FulfillmentOption {

    public List<FulfillmentWeightBand> getBands();

    public void setBands(List<FulfillmentWeightBand> bands);

}
