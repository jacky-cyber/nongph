package cn.globalph.b2c.order.fulfillment.domain;

import cn.globalph.b2c.order.domain.FulfillmentOption;

import java.util.List;

/**
 * 
 * @author Phillip Verheyden
 */
public interface BandedPriceFulfillmentOption extends FulfillmentOption {
    
    public List<FulfillmentPriceBand> getBands();

    public void setBands(List<FulfillmentPriceBand> bands);

}
