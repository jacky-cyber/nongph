package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupFee;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.call.FulfillmentGroupItemRequest;
import cn.globalph.b2c.order.service.call.FulfillmentGroupRequest;
import cn.globalph.b2c.order.service.type.FulfillmentGroupStatusType;
import cn.globalph.b2c.order.service.type.FulfillmentType;
import cn.globalph.b2c.pricing.service.exception.PricingException;

import java.util.List;

public interface FulfillmentGroupService {

    public FulfillmentGroup save(FulfillmentGroup fulfillmentGroup);

    public FulfillmentGroup createEmptyFulfillmentGroup();

    public FulfillmentGroup findFulfillmentGroupById(Long fulfillmentGroupId);

    public void delete(FulfillmentGroup fulfillmentGroup);
    
    public FulfillmentGroup addFulfillmentGroupToOrder(FulfillmentGroupRequest fulfillmentGroupRequest, boolean priceOrder) throws PricingException;
    
    public FulfillmentGroup addItemToFulfillmentGroup(FulfillmentGroupItemRequest fulfillmentGroupItemRequest, boolean priceOrder) throws PricingException;

    public FulfillmentGroup addItemToFulfillmentGroup(FulfillmentGroupItemRequest fulfillmentGroupItemRequest, boolean priceOrder, boolean save) throws PricingException;
    
    public Order removeAllFulfillmentGroupsFromOrder(Order order, boolean priceOrder) throws PricingException;

    /**
     * Removes every fulfillment group item in every fulfillment group in the order
     * that is associated with the given orderItem. Note that it does not save the changes
     * made - instead, the caller is responsible for saving the order further down.
     * 
     * @param order
     * @param orderItem
     */
    public void removeOrderItemFromFullfillmentGroups(Order order, OrderItem orderItem);
    
    public FulfillmentGroupFee createFulfillmentGroupFee();

    /**
     * Associates shippable FulfillmentGroupItems in the given Order such that they match the structure
     * of the OrderMultishipOptions associated with the given Order. 
     * 
     * @see OrderMultishipOption
     * 
     * @param order
     * @return the saved order
     * @throws PricingException 
     */
    //public Order matchFulfillmentGroupsToMultishipOptions(Order order, boolean priceOrder) throws PricingException;

    /**
     * Collapses all of the shippable fulfillment groups in the given order to the first shippable fulfillment group
     * in the order.
     * 
     * @see #matchFulfillmentGroupsToMultishipOptions(Order, boolean)
     * 
     * @param order
     * @param priceOrder
     * @return the saved order
     * @throws PricingException 
     */
    public Order collapseToOneShippableFulfillmentGroup(Order order, boolean priceOrder) throws PricingException;


    /**
     * Reads FulfillmentGroups whose status is not FULFILLED or DELIVERED.
     * @param start
     * @param maxResults
     * @return
     */
    public List<FulfillmentGroup> findUnfulfilledFulfillmentGroups(int start, int maxResults);
    
    /**
     * Reads FulfillmentGroups whose status is PARTIALLY_FULFILLED or PARTIALLY_DELIVERED.
     * 
     * @param start
     * @param maxResults
     * @return
     */
    public List<FulfillmentGroup> findPartiallyFulfilledFulfillmentGroups(int start, int maxResults);
    
    /**
     * Returns FulfillmentGroups whose status is null, or where no processing has yet occured. 
     * Default returns in ascending order according to date that the order was created.
     * @param start
     * @param maxResults
     * @return
     */
    public List<FulfillmentGroup> findUnprocessedFulfillmentGroups(int start, int maxResults);
    
    /**
     * Reads FulfillmentGroups by status, either ascending or descending according to the date that 
     * the order was created.
     * @param status
     * @param start
     * @param maxResults
     * @param ascending
     * @return
     */
    public List<FulfillmentGroup> findFulfillmentGroupsByStatus(FulfillmentGroupStatusType status, int start, int maxResults, boolean ascending);
    
    /**
     * Reads FulfillmentGroups by status, ascending according to the date that 
     * the order was created.
     * @param status
     * @param start
     * @param maxResults
     * @return
     */
    public List<FulfillmentGroup> findFulfillmentGroupsByStatus(FulfillmentGroupStatusType status, int start, int maxResults);

    /**
     * Determines if a fulfillment group is shippable based on its fulfillment type.
     * 
     * @param fulfillmentType
     * @return
     */
    public boolean isShippable(FulfillmentType fulfillmentType);
    
    /**
     * Returns the first shippable fulfillment group from an order.
     *
     * @param order
     */
    public FulfillmentGroup getFirstShippableFulfillmentGroup(Order order);

    /**
     * Finds all FulfillmentGroupItems in the given Order that reference the given OrderItem.
     * 
     * @param order
     * @param orderItem
     * @return the list of related FulfillmentGroupItems
     */
    public List<FulfillmentGroupItem> getFulfillmentGroupItemsForOrderItem(Order order, OrderItem orderItem);


}
