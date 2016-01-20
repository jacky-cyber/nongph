package cn.globalph.b2c.order.dao;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;

import java.util.List;

public interface FulfillmentGroupItemDao {

    FulfillmentGroupItem readFulfillmentGroupItemById(Long fulfillmentGroupItemId);

    FulfillmentGroupItem save(FulfillmentGroupItem fulfillmentGroupItem);

    List<FulfillmentGroupItem> readFulfillmentGroupItemsForFulfillmentGroup(FulfillmentGroup fulfillmentGroup);

    void delete(FulfillmentGroupItem fulfillmentGroupItem);

    FulfillmentGroupItem create();
}
