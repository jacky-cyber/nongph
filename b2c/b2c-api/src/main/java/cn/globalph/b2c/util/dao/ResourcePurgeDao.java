package cn.globalph.b2c.util.dao;

import java.util.Date;
import java.util.List;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.passport.domain.Customer;

/**
 * Adds capability to delete old or defunct entities from the persistence layer (e.g. Carts and anonymous Customers)
 *
 * @author Jeff Fischer
 */
public interface ResourcePurgeDao {

    /**
     * Finds carts from the database. Carts are generally considered orders that have
     * not made it to the submitted status. The method parameters can be left null, or included to refine
     * the select criteria. Note, if statuses are null, the query defaults to selecting
     * only orders that have a status of IN_PROCESS.
     *
     * @param names One or more order names to restrict the select by. Can be null.
     * @param statuses One or more order statuses to restrict the select by. Can be null.
     * @param dateCreatedMinThreshold Min creation date to restrict the select by. Orders created before this date
     *                                are retrieved. Can be null.
     * @param isPreview whether or not the results should be preview orders. Can be null.
     * @return the list of found carts
     */
    List<Order> findCarts(String[] names, OrderStatus[] statuses, Date dateCreatedMinThreshold, Boolean isPreview);

    /**
     * Finds carts from the database. Carts are generally considered orders that have
     * not made it to the submitted status. The method parameters can be left null, or included to refine
     * the select criteria. Note, if statuses are null, the query defaults to selecting
     * only orders that have a status of IN_PROCESS. This overloaded version of the method is capable of
     * retrieving a portion or "page" of the total results.
     *
     * @param names One or more order names to restrict the select by. Can be null.
     * @param statuses One or more order statuses to restrict the select by. Can be null.
     * @param dateCreatedMinThreshold Min creation date to restrict the select by. Orders created before this date
     *                                are retrieved. Can be null.
     * @param isPreview whether or not the results should be preview orders. Can be null.
     * @param startPos the position in the overall result set from which to start the returned list.
     * @param length the max number of results to include in the returned list.
     * @return the list of found carts
     */
    List<Order> findCarts(String[] names, OrderStatus[] statuses, Date dateCreatedMinThreshold, Boolean isPreview, int startPos, int length);

    /**
     * Finds the count of carts from the database. Carts are generally considered orders that have
     * not made it to the submitted status. The method parameters can be left null, or included to refine
     * the select criteria. Note, if statuses are null, the query defaults to selecting
     * only orders that have a status of IN_PROCESS.
     *
     * @param names One or more order names to restrict the select by. Can be null.
     * @param statuses One or more order statuses to restrict the select by. Can be null.
     * @param dateCreatedMinThreshold Min creation date to restrict the select by. Orders created before this date
     *                                are retrieved. Can be null.
     * @param isPreview whether or not the results should be preview orders. Can be null.
     * @return the number of carts found
     */
    Long findCartsCount(String[] names, OrderStatus[] statuses, Date dateCreatedMinThreshold, Boolean isPreview);

    /**
     * Find customers in the database. The method parameters can be left null, or included to refine
     * the select criteria.
     *
     * @param dateCreatedMinThreshold Min creation date to restrict the select by. Customers created before this date
     *                                are retrieved. Can be null.
     * @param registered Whether or not the results should be registered customers. Can be null.
     * @param deactivated Whether or not the results should be deactivated customers. Can be null.
     * @param isPreview Whether or not the results should be preview customers. Can be null.
     * @return the list of found customers
     */
    List<Customer> findCustomers(Date dateCreatedMinThreshold, Boolean registered, Boolean deactivated, Boolean isPreview);

    /**
     * Find customers in the database. The method parameters can be left null, or included to refine
     * the select criteria. This overloaded version of the method is capable of
     * retrieving a portion or "page" of the total results.
     *
     * @param dateCreatedMinThreshold Min creation date to restrict the select by. Customers created before this date
     *                                are retrieved. Can be null.
     * @param registered Whether or not the results should be registered customers. Can be null.
     * @param deactivated Whether or not the results should be deactivated customers. Can be null.
     * @param isPreview Whether or not the results should be preview customers. Can be null.
     * @param startPos the position in the overall result set from which to start the returned list.
     * @param length the max number of results to include in the returned list.
     * @return the list of found customers
     */
    List<Customer> findCustomers(Date dateCreatedMinThreshold, Boolean registered, Boolean deactivated, Boolean isPreview, int startPos, int length);

    /**
     * Find count of customers in the database. The method parameters can be left null, or included to refine
     * the select criteria.
     *
     * @param dateCreatedMinThreshold Min creation date to restrict the select by. Customers created before this date
     *                                are retrieved. Can be null.
     * @param registered Whether or not the results should be registered customers. Can be null.
     * @param deactivated Whether or not the results should be deactivated customers. Can be null.
     * @param isPreview Whether or not the results should be preview customers. Can be null.
     * @return the count of found customers
     */
    Long findCustomersCount(Date dateCreatedMinThreshold, Boolean registered, Boolean deactivated, Boolean isPreview);
}
