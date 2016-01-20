package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.dao.FulfillmentOptionDao;
import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.order.service.type.FulfillmentType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

/**
 * 
 * @author Phillip Verheyden
 */
@Service("blFulfillmentOptionService")
@Transactional("blTransactionManager")
public class FulfillmentOptionServiceImpl implements FulfillmentOptionService {

    @Resource(name = "blFulfillmentOptionDao")
    FulfillmentOptionDao fulfillmentOptionDao;

    @Override
    public FulfillmentOption readFulfillmentOptionById(Long fulfillmentOptionId) {
        return fulfillmentOptionDao.readFulfillmentOptionById(fulfillmentOptionId);
    }

    @Override
    public FulfillmentOption save(FulfillmentOption option) {
        return fulfillmentOptionDao.save(option);
    }

    @Override
    public List<FulfillmentOption> readAllFulfillmentOptions() {
        return fulfillmentOptionDao.readAllFulfillmentOptions();
    }

    @Override
    public List<FulfillmentOption> readAllFulfillmentOptionsByFulfillmentType(FulfillmentType type) {
        return fulfillmentOptionDao.readAllFulfillmentOptionsByFulfillmentType(type);
    }
}
