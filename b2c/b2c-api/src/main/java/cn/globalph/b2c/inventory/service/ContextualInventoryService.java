package cn.globalph.b2c.inventory.service;

import cn.globalph.b2c.checkout.service.workflow.DecrementInventoryActivity;
import cn.globalph.b2c.checkout.service.workflow.DecrementInventoryRollbackHandler;
import cn.globalph.b2c.order.service.workflow.CheckAvailabilityActivity;
import cn.globalph.b2c.product.domain.Sku;

import java.util.Collection;
import java.util.Map;

/**
 * Provides the same methods from {@link InventoryService} but with optional, additional context information. This context
 * can then be passed on to an {@link InventoryServiceExtensionHandler}
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link InventoryService}
 * @see {@link InventoryServiceExtensionHandler}
 * @see {@link CheckAvailabilityActivity}
 * @see {@link DecrementInventoryActivity}
 */
public interface ContextualInventoryService extends InventoryService {
    
    /**
     * Used as a key in the context map methods below. This is used for the current order that should be used to evaluate
     * the methods below
     */
    public static String ORDER_KEY = "ORDER";

    /**
     * Used as a key in the context map methods below. This key is normally populated from the {@link DecrementInventoryActivity}
     * and is utilized in the {@link DecrementInventoryRollbackHandler}. This can be cast to a Map<String, Object> and is
     * designed such that when it is used, non-read operations (decrement and increment) can add what actually happened
     * so that it can be reversed.
     */
    public static String ROLLBACK_STATE_KEY = "ROLLBACK_STATE";
    
    /**
     * @param context can be null. If not null, this should at least contain the {@link #CART_CONTEXT_KEY}
     * @see {@link #retrieveQuantitiesAvailable(Collection, Map)}
     */
    public Integer retrieveQuantityAvailable(Sku sku, Map<String, Object> context);

    /**
     * @param context can be null. If not null, this should at least contain the {@link #CART_CONTEXT_KEY}
     * @see {@link #retrieveQuantitiesAvailable(Collection)}
     */
    public Map<Sku, Integer> retrieveQuantitiesAvailable(Collection<Sku> skus, Map<String, Object> context);
    
    /**
     * @param context can be null. If not null, this should at least contain the {@link #CART_CONTEXT_KEY}
     * @see {@link #isAvailable(Sku, int)}
     */
    public boolean isAvailable(Sku sku, int quantity, Map<String, Object> context);
    
    /**
     * <p>Pass through for {@link #decrementInventory(Map, Map)}
     * @see {@link #decrementInventory(Map, Map)}
     */
    public void decrementInventory(Sku sku, int quantity, Map<String, Object> context) throws InventoryUnavailableException;

    /**
     * <p>Usually invoked from {@link DecrementInventoryActivity}</p>
     * 
     * <p>Callers that invoke this method directly should check the given <b>context</b> object for a {@link #ROLLBACK_STATE_KEY}.
     * This will contain information about what actually happened in terms of decrementing inventory. For implementers of this
     * interface </p>
     * 
     * <p>Implementers of this method (explicitly those that are utilizing the {@link InventoryServiceExtensionHandler})
     * should populate a {@link #ROLLBACK_STATE_KEY} within the given <b>context</b> in order to communicate back to the
     * caller what actually happened while decrementing inventory so that it can be undone later</b></p>
     * 
     * @param context can be null. If not null, this should at least contain the {@link #ORDER_KEY} and/or the
     * {@link #ROLLBACK_STATE_KEY}
     * @see {@link #decrementInventory(Map)}
     */
    public void decrementInventory(Map<Sku, Integer> skuQuantities, Map<String, Object> context) throws InventoryUnavailableException;

    /**
     * @see {@link #incrementInventory(Map, Map)}
     */
    public void incrementInventory(Sku sku, int quantity, Map<String, Object> context);

    /**
     * <p>Callers that invoke this method directly should check for a {@link #ROLLBACK_STATE_KEY} in the given <b>context</b>.
     * This will contain information about what actually happened in terms of decrementing inventory</p>
     * 
     * <p>Implementers of this method (explicitly those that are utilizing the {@link InventoryServiceExtensionHandler})
     * should populate a {@link #ROLLBACK_STATE_KEY} within the given <b>context</b> in order to communicate back to the
     * caller what actually happened while decrementing inventory so that it can be undone later</b></p>
     * 
     * @param context can be null. If not null, this should at least contain the {@link #ROLLBACK_STATE_KEY}
     * @see {@link #incrementInventory(Map)}
     */
    public void incrementInventory(Map<Sku, Integer> skuQuantities, Map<String, Object> context);

}
