package cn.globalph.b2c.order.service.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.inventory.service.ContextualInventoryService;
import cn.globalph.b2c.inventory.service.InventoryUnavailableException;
import cn.globalph.b2c.inventory.service.type.InventoryType;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.call.OrderItemRequestDTO;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * This activity handles both adds and updates. In both cases, this will check the availability and quantities (if applicable)
 * of the passed in request. If this is an update request, this will use the {@link Sku} from {@link OrderItemRequestDTO#getOrderItemId()}.
 * If this is an add request, there is no order item yet so the {@link Sku} is looked up via the {@link OrderItemRequestDTO#getSkuId()}.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public class CheckAvailabilityActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {

    private static final Log LOG = LogFactory.getLog(CheckAvailabilityActivity.class);
    
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;
    
    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;
    
    @Resource(name = "blInventoryService")
    protected ContextualInventoryService inventoryService;
    
    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        
        Sku sku;
        Long orderItemId = request.getItemRequest().getOrderItemId();
        if (orderItemId != null) {
            // this must be an update request as there is an order item ID available
            OrderItem orderItem = orderItemService.readOrderItemById(orderItemId);
            sku = orderItem.getSku();
        } else {
            // No order item, this must be a new item add request
            Long skuId = request.getItemRequest().getSkuId();
            sku = catalogService.findSkuById(skuId);
          }
        
        
        // First check if this Sku is available
        if (!sku.isAvailable()) {
            throw new InventoryUnavailableException("The referenced Sku " + sku.getId() + " is marked as unavailable",
                    sku.getId(), request.getItemRequest().getQuantity(), 0);
           }
        
        if (InventoryType.CHECK_QUANTITY.equals(sku.getInventoryType())) {
            Integer requestedQuantity = request.getItemRequest().getQuantity();
            
            Map<String, Object> inventoryContext = new HashMap<String, Object>();
            inventoryContext.put(ContextualInventoryService.ORDER_KEY, context.getSeedData().getOrder());
            boolean available = inventoryService.isAvailable(sku, requestedQuantity, inventoryContext);
            if (!available) {
                throw new InventoryUnavailableException(sku.getId(),
                        requestedQuantity, inventoryService.retrieveQuantityAvailable(sku, inventoryContext));
            }
        }
        
        // the other case here is ALWAYS_AVAILABLE and null, which we are treating as being available
        
        return context;
    }
    
}
