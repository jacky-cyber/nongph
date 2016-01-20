package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.checkout.service.workflow.CheckoutSeed;
import cn.globalph.b2c.checkout.service.workflow.DecrementInventoryRollbackHandler;
import cn.globalph.b2c.inventory.service.ContextualInventoryService;
import cn.globalph.b2c.inventory.service.type.InventoryType;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.SkuBundle;
import cn.globalph.b2c.product.domain.SkuBundleItem;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.b2c.workflow.state.ActivityStateManagerImpl;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 增加库存活动
 *
 * @author felix.wu
 */
public class IncrementInventoryActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    @Resource(name = "blInventoryService")
    protected ContextualInventoryService inventoryService;

    public IncrementInventoryActivity() {
        super();
        super.setAutomaticallyRegisterRollbackHandler(false);
    }

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        CheckoutSeed seed = context.getSeedData();
        List<OrderItem> orderItems = seed.getOrder().getOrderItems();

        //map to hold skus and quantity purchased
        HashMap<Sku, Integer> skuInventoryMap = new HashMap<Sku, Integer>();

        for (OrderItem orderItem : orderItems) {
            Sku sku = orderItem.getSku();
            Integer quantity = skuInventoryMap.get(sku);
            if (quantity == null) {
                quantity = orderItem.getQuantity();
            } else {
                quantity += orderItem.getQuantity();
            }
            if (InventoryType.CHECK_QUANTITY.equals(sku.getInventoryType())) {
                skuInventoryMap.put(sku, quantity);
            }
            if (sku instanceof SkuBundle) {
                SkuBundle bundle = (SkuBundle) sku;
                // Now add all of the sku items within the bundle
                List<SkuBundleItem> bundleItems = bundle.getSkuBundleItems();
                for (SkuBundleItem bi : bundleItems) {
                    if (InventoryType.CHECK_QUANTITY.equals(bi.getSku().getInventoryType())) {
                        quantity = skuInventoryMap.get(bi.getSku());
                        if (quantity == null) {
                            quantity = (orderItem.getQuantity() * bi.getQuantity());
                        } else {
                            quantity += (orderItem.getQuantity() * bi.getQuantity());
                        }
                        skuInventoryMap.put(bi.getSku(), quantity);
                    }
                }
            }
        }

        Map<String, Object> rollbackState = new HashMap<String, Object>();
        if (getRollbackHandler() != null && !getAutomaticallyRegisterRollbackHandler()) {
            if (getStateConfiguration() != null && !getStateConfiguration().isEmpty()) {
                rollbackState.putAll(getStateConfiguration());
            }
            // Register the map with the rollback state object early on; this allows the extension handlers to incrementally
            // add state while decrementing but still throw an exception
            ActivityStateManagerImpl.getStateManager().registerState(this, context, getRollbackRegion(), getRollbackHandler(), rollbackState);
        }

        if (!skuInventoryMap.isEmpty()) {
            Map<String, Object> contextualInfo = new HashMap<String, Object>();
            contextualInfo.put(ContextualInventoryService.ORDER_KEY, context.getSeedData().getOrder());
            contextualInfo.put(ContextualInventoryService.ROLLBACK_STATE_KEY, new HashMap<String, Object>());
            inventoryService.incrementInventory(skuInventoryMap, contextualInfo);

            if (getRollbackHandler() != null && !getAutomaticallyRegisterRollbackHandler()) {
                rollbackState.put(DecrementInventoryRollbackHandler.ROLLBACK_BLC_INVENTORY_INCREMENTED, skuInventoryMap);
                rollbackState.put(DecrementInventoryRollbackHandler.ROLLBACK_BLC_ORDER_ID, seed.getOrder().getId());
            }

            // add the rollback state that was used in the rollback handler
            rollbackState.put(DecrementInventoryRollbackHandler.EXTENDED_ROLLBACK_STATE, contextualInfo.get(ContextualInventoryService.ROLLBACK_STATE_KEY));
        }

        return context;
    }

}
