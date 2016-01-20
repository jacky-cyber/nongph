package cn.globalph.b2c.util.service;

import java.util.Map;

/**
 * Service capable of deleting old or defunct entities from the persistence layer (e.g. Carts and anonymous Customers)
 *
 * @author Jeff Fischer
 */
public interface ResourcePurgeService {

    /**
     * Execute a purge of carts from the persistence layer based on the configuration parameters. The default
     * implementation is capable of looking at any combination of name, status and creation date. Take a look
     * at {@link cn.globalph.b2c.order.service.OrderService#findCarts(String[],
     * cn.globalph.b2c.order.service.type.OrderStatus[], java.util.Date, Boolean, int, int)}
     * for more info on the default behavior.
     *
     * @param config Map of params used to drive the selection of carts to purge
     */
    void purgeCarts(Map<String, String> config);

    void purgeCustomers(final Map<String, String> config);

    /**
     * Override the default page size (set by property streaming.transaction.item.page.size) by which entities will be purged.
     * Candidates are retrieved in pages and deleted a page at a time to minimize transaction times.
     *
     * @return the page size
     */
    Integer getPageSize();

    /**
     * Override the default page size (set by property streaming.transaction.item.page.size) by which entities will be purged.
     * Candidates are retrieved in pages and deleted a page at a time to minimize transaction times.
     *
     * @param pageSize the page size
     */
    void setPageSize(Integer pageSize);
}
