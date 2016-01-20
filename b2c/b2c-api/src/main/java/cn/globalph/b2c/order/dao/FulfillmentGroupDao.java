package cn.globalph.b2c.order.dao;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupFee;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.type.FulfillmentGroupStatusType;

import java.util.List;

public interface FulfillmentGroupDao {

    public FulfillmentGroup readFulfillmentGroupById(Long fulfillmentGroupId);

    public FulfillmentGroup save(FulfillmentGroup fulfillmentGroup);

    public FulfillmentGroup readDefaultFulfillmentGroupForOrder(Order order);

    public void delete(FulfillmentGroup fulfillmentGroup);

    public FulfillmentGroup createDefault();

    public FulfillmentGroup create();

    public FulfillmentGroupFee createFulfillmentGroupFee();
    
    /**
     * Reads FulfillmentGroups whose status is not FULFILLED or DELIVERED.
     * @param start
     * @param maxResults
     * @return
     */
    public List<FulfillmentGroup> readUnfulfilledFulfillmentGroups(int start, int maxResults);
    
    /**
     * Reads FulfillmentGroups whose status is PARTIALLY_FULFILLED or PARTIALLY_DELIVERED.
     * 
     * @param start
     * @param maxResults
     * @return
     */
    public List<FulfillmentGroup> readPartiallyFulfilledFulfillmentGroups(int start, int maxResults);
    
    /**
     * Returns FulfillmentGroups whose status is null, or where no processing has yet occured. 
     * Default returns in ascending order according to date that the order was created.
     * @param start
     * @param maxResults
     * @return
     */
    public List<FulfillmentGroup> readUnprocessedFulfillmentGroups(int start, int maxResults);
    
    /**
     * Reads FulfillmentGroups by status, either ascending or descending according to the date that 
     * the order was created.
     * @param status
     * @param start
     * @param maxResults
     * @param ascending
     * @return
     */
    public List<FulfillmentGroup> readFulfillmentGroupsByStatus(FulfillmentGroupStatusType status, int start, int maxResults, boolean ascending);
    
    /**
     * Reads FulfillmentGroups by status, ascending according to the date that 
     * the order was created.
     * @param status
     * @param start
     * @param maxResults
     * @return
     */
    public List<FulfillmentGroup> readFulfillmentGroupsByStatus(FulfillmentGroupStatusType status, int start, int maxResults);

    /**
     * Reads the max sequnce of fulfillment groups for a particular order and increments by 1.
     * @param order
     * @return
     */
    public Integer readNextFulfillmentGroupSequnceForOrder(Order order);
}
