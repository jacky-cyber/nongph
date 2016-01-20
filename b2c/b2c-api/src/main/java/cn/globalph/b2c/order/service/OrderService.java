package cn.globalph.b2c.order.service;

import org.apache.commons.logging.Log;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.call.OrderItemRequestDTO;
import cn.globalph.b2c.order.service.exception.AddToCartException;
import cn.globalph.b2c.order.service.exception.RemoveFromCartException;
import cn.globalph.b2c.order.service.exception.UnuseFromCartException;
import cn.globalph.b2c.order.service.exception.UpdateCartException;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.secure.Referenced;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.workflow.WorkflowException;
import cn.globalph.common.payment.PaymentType;
import cn.globalph.coupon.apply.OrderDetailToApplyCoupon;
import cn.globalph.passport.domain.Customer;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The general interface for interacting with shopping carts and completed Orders.
 * In Broadleaf Commerce, a Cart and an Order are the same thing. A "cart" becomes 
 * an order after it has been submitted.
 *
 * Most of the methods in this order are used to modify the cart. However, it is also
 * common to use this service for "named" orders (aka wishlists).
 */
public interface OrderService {

    /**
     * Creates a new Order for the given customer. Generally, you will want to use the customer
     * that is on the current request, which can be grabbed by utilizing the CustomerState 
     * utility class.
     * 
     * The default Broadleaf implementation of this method will provision a new Order in the 
     * database and set the current customer as the owner of the order. If the customer has an
     * email address associated with their profile, that will be copied as well. If the customer
     * is a new, anonymous customer, his username will be set to his database id.
     * 
     * @see cn.globalph.passport.web.core.CustomerState#getCustomer()
     * 
     * @param customer
     * @return the newly created order
     */
    public Order createNewCartForCustomer(Customer customer);

    /**
     * Creates a new Order for the given customer with the given name. Typically, this represents
     * a "wishlist" order that the customer can save but not check out with.
     *
     * @param name the wishlist name
     * @param customer
     * @param args additional arguments to be used by Broadleaf extensions
     * @return the newly created named order
     */
    public Order createNamedOrderForCustomer(String name, Customer customer);

    /**
     * Creates a sub Order for the given order. Typically, this represents
     * a sub order of the original order that has items with different providers .
     *
     * @param order the parent order
     * @param args additional arguments to be used by Broadleaf extensions
     * @return the newly created named order
     */
    Order createSuborderForOrder(Order order);

    /**
     * Looks up an Order by the given customer and a specified order name.
     * 
     * This is typically used to retrieve a "wishlist" order.
     * 
     * @see #createNamedOrderForCustomer(String name, Customer customer)
     * 
     * @param name
     * @param customer
     * @param args additional arguments to be used by Broadleaf extensions
     * @return the named order requested
     */
    public Order findNamedOrderForCustomer(String name, Customer customer);


    List<Order> readSuborderList(Long orderId);
    
    /**
     * Looks up an Order by its database id
     * 
     * @param orderId
     * @return the requested Order
     */
    public Order getOrderById(Long orderId);

    /**
     * Looks up an Order by its database id
     * and optionally calls refresh to ensure that the entity manager pulls the instance from the DB and not from cache
     *
     * @param orderId
     * @return the requested Order
     */
    public Order getOrderById(Long orderId, boolean refresh);
    
    /**
     * Looks up the current shopping cart for the customer. Note that a shopping cart is
     * simply an Order with OrderStatus = IN_PROCESS. If for some reason the given customer
     * has more than one current IN_PROCESS Order, the default Broadleaf implementation will
     * return the first match found. Furthermore, also note that the current shopping cart
     * for a customer must never be named -- an Order with a non-null "name" property indicates
     * that it is a wishlist and not a shopping cart.
     * 
     * @param customer
     * @return the current shopping cart for the customer
     */
    public Order findCartForCustomer(Customer customer);
    
    /**
     * Looks up all Orders for the specified customer, regardless of current OrderStatus
     * 
     * @param customer
     * @return the requested Orders
     */
    public List<Order> findOrdersForCustomer(Customer customer);
    
    /**
     * Looks up all Orders for the specified customer that are in the specified OrderStatus.
     * 
     * @param customer
     * @param status
     * @return the requested Orders
     */
    public List<Order> findOrdersForCustomer(Customer customer, OrderStatus status);
    
    public List<Order> findOrdersForCustomer(Customer customer, List<OrderStatus> sts);
    
    public Integer getOrdersForCustomerTotal(Customer customer, List<OrderStatus> sts);
    
    public List<Order> findOrdersForCustomer(Customer customer, List<OrderStatus> sts, int pageSize, int page);
    
    /**
     * Looks up Orders and returns the order matching the given orderNumber
     * 
     * @param orderNumber
     * @return the requested Order
     */
    public Order findOrderByOrderNumber(String orderNumber);
    
    /**
     * Returns all OrderPayment objects that are associated with the given order
     * 
     * @param order
     * @return the list of all OrderPayment objects
     */
    public List<OrderPayment> findPaymentsForOrder(Order order);

    /**
     * Associates a given OrderPayment with an Order and then saves the order. Note that it is acceptable for the 
     * securePaymentInfo to be null. For example, if the secure credit card details are 
     * handled by a third party, a given application may never have associated securePaymentInfos
     * 
     * @param order
     * @param payment
     * @param securePaymentInfo - null if it doesn't exist
     * @return the persisted version of the OrderPayment
     */
    public OrderPayment addPaymentToOrder(Order order, OrderPayment payment, Referenced securePaymentInfo);
    
    /**
     * Persists the given order to the database. If the priceOrder flag is set to true,
     * the pricing workflow will execute before the order is written to the database.
     * Generally, you will want to price the order in every request scope once, and
     * preferrably on the last call to save() for performance reasons.
     * 
     * However, if you have logic that depends on the Order being priced, there are no
     * issues with saving as many times as necessary.
     * 
     * @param order
     * @param priceOrder
     * @return the persisted Order, which will be a different instance than the Order passed in
     * @throws PricingException
     */
    public Order save(Order order, Boolean priceOrder) throws PricingException;
    
    /**
     * Deletes the given order. Note that the default Broadleaf implementation in 
     * OrderServiceImpl will actually remove the Order instance from the database.
     * 
     * @param order
     */
    public void cancelOrder(Order order);
       
    /**
     * The null order is the default order for all customers when they initially
     * enter the site. Upon the first addition of a product to a cart, a non-null order
     * will be provisioned for the user.
     * 
     * @see cn.globalph.b2c.order.domain.NullOrderImpl for more information
     * 
     * @return a shared, static, unmodifiable NullOrder
     */
    public Order getNullOrder();
    
    /**
     * @see #setAutomaticallyMergeLikeItems(boolean)
     * 
     * @return whether or not like-items will be automatically merged
     */
    public boolean getAutomaticallyMergeLikeItems();

    /**
     * When set to true, the system when items are added to the cart, they will
     * automatically be merged. For example, when a user adds an item to the cart
     * and then adds the item again, the item will have its quantity changed to 2
     * instead of the cart containing two separate items.
     *
     * If this logic needs to be more complex, it is possible to extend the behavior by
     * overriding OrderOfferProcessor.buildIdentifier().
     *
     * @param automaticallyMergeLikeItems
     */
    public void setAutomaticallyMergeLikeItems(boolean automaticallyMergeLikeItems);
    
    /**
     * Changes the OrderStatus to SUBMITTED
     * 
     * @param order to confirm
     * @return the order that was confirmed
     */
    public Order confirmOrder(Order order);
    
    /**
     * Looks through the given order and returns the latest added OrderItem that matches on the skuId
     * and productId. Generally, this is used to retrieve the OrderItem that was just added to the cart.
     * The default Broadleaf implementation will attempt to match on skuId first, and failing that, it will
     * look at the productId.
     * 
     * Note that the behavior is slightly undeterministic in the case that {@link setAutomaticallyMergeLikeItems}
     * is set to true and the last added sku matches on a previously added sku. In this case, the sku that has the
     * merged items would be returned, so the total quantity of the OrderItem might not match exactly what was 
     * just added.
     * 
     * @param order
     * @param skuId
     * @param productId
     * @return the best matching OrderItem with highest index in the list of OrderItems in the order
     */
    public OrderItem findLastMatchingItem(Order order, Long skuId, Long productId);
    
    /**
     * Initiates the addItem workflow that will attempt to add the given quantity of the specified item
     * to the Order. The item to be added can be determined in a few different ways. For example, the 
     * SKU can be specified directly or it can be determine based on a Product and potentially some
     * specified ProductOptions for that given product.
     *
     * The minimum required parameters for OrderItemRequest are: productId and quantity or alternatively, skuId and quantity
     *
     * When priceOrder is false, the system will not reprice the order.   This is more performant in
     * cases such as bulk adds where the repricing could be done for the last item only.
     * 
     * This method differs from the {@link #addItemWithPriceOverrides(Long, OrderItemRequestDTO, boolean)} in that it
     * will clear any values set on the {@link OrderItemRequestDTO} for the overrideSalePrice or overrideRetailPrice.
     * 
     * This design is intended to ensure that override pricing is not called by mistake.   Implementors should
     * use this method when no manual price overrides are allowed.
     *
     * @see OrderItemRequestDTO
     * @param orderId
     * @param orderItemRequest
     * @param priceOrder
     * @return the order the item was added to
     * @throws WorkflowException 
     * @throws Throwable 
     */
    public Order addItem(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder) throws AddToCartException;
    
    /**
     * Initiates the addItem workflow that will attempt to add the given quantity of the specified item
     * to the Order. The item to be added can be determined in a few different ways. For example, the 
     * SKU can be specified directly or it can be determine based on a Product and potentially some
     * specified ProductOptions for that given product.
     *
     * The minimum required parameters for OrderItemRequest are: productId and quantity or alternatively, skuId and quantity
     *
     * When priceOrder is false, the system will not reprice the order.   This is more performant in
     * cases such as bulk adds where the repricing could be done for the last item only.
     * 
     * As opposed to the {@link #addItem(Long, OrderItemRequestDTO, boolean)} method, this method allows
     * the passed in {@link OrderItemRequestDTO} to contain values for the overrideSale or overrideRetail
     * price fields.
     * 
     * This design is intended to ensure that override pricing is not called by mistake.   Implementors should
     * use this method when manual price overrides are allowed.
     *
     * @see OrderItemRequestDTO
     * @param orderId
     * @param orderItemRequest
     * @param priceOrder
     * @return the order the item was added to
     * @throws WorkflowException 
     * @throws Throwable 
     */
    public Order addItemWithPriceOverrides(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder) throws AddToCartException;

    /**
     * Initiates the updateItem workflow that will attempt to update the item quantity for the specified
     * OrderItem in the given Order. The new quantity is specified in the OrderItemRequestDTO
     * 
     * Minimum required parameters for OrderItemRequest: orderItemId, quantity
     * 
     * @see OrderItemRequestDTO
     * @param orderId
     * @param orderItemRequest
     * @param priceOrder
     * @return the order the item was added to
     * @throws UpdateCartException
     * @throws RemoveFromCartException 
     */
    public Order updateItemQuantity(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder) throws UpdateCartException, RemoveFromCartException;
    
    /**
     * Initiates the removeItem workflow that will attempt to remove the specified OrderItem from 
     * the given Order
     * 
     * @see OrderItemRequestDTO
     * @param orderId
     * @param orderItemId
     * @param priceOrder
     * @return the order the item was added to
     * @throws RemoveFromCartException 
     */
    public Order removeItem(Long orderId, Long orderItemId, boolean priceOrder) throws RemoveFromCartException;
    
    public Order unuseItem(Long orderId, Long orderItemId, boolean priceOrder, boolean realDelete) throws UnuseFromCartException;
    
    /**
     * @see #setMoveNamedOrderItems(boolean)
     * @return whether items will be removed from the wishlist when added to the cart
     */
    public boolean isMoveNamedOrderItems();

    /**
     * Determines whether or not items will be removed from the named order (wishlist)
     * when they are moved to the Customer's current cart.
     * 
     * @param moveNamedOrderItems
     */
    public void setMoveNamedOrderItems(boolean moveNamedOrderItems);

    /**
     * @see #setDeleteEmptyNamedOrders(boolean)
     * @return whether empty wishlists will be deleted automatically
     */
    public boolean isDeleteEmptyNamedOrders();

    /**
     * Sets whether or not to delete named orders once all items have been removed.
     * 
     * @param deleteEmptyNamedOrders
     */
    public void setDeleteEmptyNamedOrders(boolean deleteEmptyNamedOrders);

    /**
     * Adds the passed in orderItem to the current cart for the same Customer that owns the
     * named order. This method will remove the item from the wishlist based on whether the 
     * {@link setMoveNamedOrderItems} flag is set.
     * 
     * Note that if an item was in a wishlist and is no longer able to be added to the cart,
     * the item will still be removed from the wishlist.
     * 
     * Note that this method does not change the association of the OrderItems to the new
     * order -- instead, those OrderItems is completely removed and a new OrderItem that mirrors
     * it is created.
     * 
     * @param namedOrder 
     * @param orderItem 
     * @param priceOrder 
     * @return the cart with the requested orderItem added to it
     * @throws RemoveFromCartException
     * @throws AddToCartException
     */
    public Order addItemFromNamedOrder(Order namedOrder, OrderItem orderItem, boolean priceOrder) throws RemoveFromCartException, AddToCartException;
    
    /**
     * This method performs the same function as addItemFromNamedOrder(Order, OrderItem, boolean)
     * except that instead of adding all of the quantity from the named order to the cart, it will
     * only add/move the specific quantity requested.
     * 
     * @see #addItemFromNamedOrder(Order, OrderItem, boolean)
     * 
     * @param namedOrder 
     * @param orderItem 
     * @param quantity
     * @param priceOrder 
     * @return the cart with the requested orderItem added to it
     * @throws RemoveFromCartException
     * @throws AddToCartException
     * @throws UpdateCartException 
     */
    public Order addItemFromNamedOrder(Order namedOrder, OrderItem orderItem, int quantity, boolean priceOrder) throws RemoveFromCartException, AddToCartException, UpdateCartException;

    /**
     * Adds all orderItems to the current cart from the same Customer that owns the named
     * order. This method will remove the item from the wishlist based on whether the 
     * {@link setMoveNamedOrderItems} flag is set.
     * 
     * Note that any items that are in the wishlist but are no longer able to be added to a cart
     * will still be removed from the wishlist.
     * 
     * Note that this method does not change the association of the OrderItems to the new
     * order -- instead, those OrderItems is completely removed and a new OrderItem that mirrors
     * it is created.
     * 
     * @param namedOrder
     * @param priceOrder
     * @return
     * @throws RemoveFromCartException
     * @throws AddToCartException
     */
    public Order addAllItemsFromNamedOrder(Order namedOrder, boolean priceOrder) throws RemoveFromCartException, AddToCartException;

    /**
     * Deletes all the OrderPayment Info's on the order.
     *
     * @param order
     */
    public void removeAllPaymentsFromOrder(Order order);

    /**
     * Deletes the OrderPayment Info of the passed in type from the order
     * Note that this method will also delete any associated Secure OrderPayment Infos if necessary.
     *
     * @param order
     * @param paymentInfoType
     */
    public void removePaymentsFromOrder(Order order, PaymentType paymentInfoType);

    /**
     * Deletes the OrderPayment Info from the order.
     * Note that this method will also delete any associated Secure OrderPayment Infos if necessary.
     *
     * @param order
     * @param paymentInfo
     */
    public void removePaymentFromOrder(Order order, OrderPayment paymentInfo);

    public void deleteOrder(Order cart);

    Order removeInactiveItems(Long orderId, boolean priceOrder) throws RemoveFromCartException;

    /**
     * Since required product option can be added after the item is in the cart, we use this method 
     * to apply product option on an existing item in the cart. No validation will happen at this time, as the validation 
     * at checkout will take care of any missing product options. 
     * 
     * @param orderId
     * @param orderItemRequestDTO
     * @param priceOrder
     * @return Order
     * @throws UpdateCartException
     */
    Order updateProductOptionsForItem(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder) throws UpdateCartException;

    /**
     * This debugging method will print out a console-suitable representation of the current state of the order, including
     * the items in the order and all pricing related information for the order.
     * 
     * @param order the order to debug
     * @param log the Log to use to print a debug-level message
     */
    public void printOrder(Order order, Log log);

    /**
     * Invokes the extension handler of the same name to provide the ability for a module to throw an exception
     * and interrupt a cart operation.
     * 
     * @param cart
     */
    public void preValidateCartOperation(Order cart);
    
    public List<Order> findNoCommentOrdersForCustomer(Customer customer);

    List<Order> readOrderByDateRange(Date from, Date to, OrderStatus stauts);
    
    public List<Order> findNoCommentOrdersForCustomer(Customer customer, int pageSize, int page);
    
    public Integer findNoCommentOrdersForCustomerTotal(Customer customer);

    public void exportOrder(String filePath,String sendDeliveryInfoEmail);
    
    List<Order> findCompleteOverdueOrders();
    
    List<Order> findOverdueOrders();

    List<Order> findNotSentDeliveryInfoOrders();

    List<Order> findNotShippedOrders();
    
    OrderDetailToApplyCoupon generateOrderDetailToApplyCoupon(Order order);

    int determineLogisticsOrders(int minCount);

    int determineExpressOrdersWithCommunity(int minCount);

    int determineExpressOrdersWithoutCommunity();
    
    Map<String, List<Order>> getNotShippedProviderOrders();
    
    int getSkuCountInOrders(Long skuId, Long customerId);
}
