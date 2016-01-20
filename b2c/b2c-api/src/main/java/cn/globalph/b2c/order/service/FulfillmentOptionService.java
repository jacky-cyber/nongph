package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.order.service.type.FulfillmentType;

import java.util.List;

/**
 * 
 * @author Phillip Verheyden
 */
public interface FulfillmentOptionService {

    public FulfillmentOption readFulfillmentOptionById(Long fulfillmentOptionId);

    public FulfillmentOption save(FulfillmentOption option);

    public List<FulfillmentOption> readAllFulfillmentOptions();

    public List<FulfillmentOption> readAllFulfillmentOptionsByFulfillmentType(FulfillmentType type);

}
