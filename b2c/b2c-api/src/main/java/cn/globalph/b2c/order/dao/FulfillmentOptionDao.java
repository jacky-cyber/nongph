package cn.globalph.b2c.order.dao;

import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.order.service.type.FulfillmentType;

import java.util.List;

/**
 * 
 * @author Phillip Verheyden
 */
public interface FulfillmentOptionDao {

    public FulfillmentOption readFulfillmentOptionById(final Long fulfillmentOptionId);

    public FulfillmentOption save(FulfillmentOption option);
    
    public List<FulfillmentOption> readAllFulfillmentOptions();

    public List<FulfillmentOption> readAllFulfillmentOptionsByFulfillmentType(FulfillmentType type);

}
