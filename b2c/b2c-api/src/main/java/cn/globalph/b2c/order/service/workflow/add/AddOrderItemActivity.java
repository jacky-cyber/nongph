package cn.globalph.b2c.order.service.workflow.add;

import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.call.OrderItemRequest;
import cn.globalph.b2c.order.service.call.OrderItemRequestDTO;
import cn.globalph.b2c.order.service.workflow.CartOperationRequest;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import javax.annotation.Resource;

public class AddOrderItemActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
    
    @Resource(name = "blOrderService")
    protected OrderService orderService;
    
    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;
    
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();

        // Order has been verified in a previous activity -- the values in the request can be trusted
        Order order = request.getOrder();
        
        Sku sku = catalogService.findSkuById(orderItemRequestDTO.getSkuId());
        

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setSku( sku );
        itemRequest.setQuantity( orderItemRequestDTO.getQuantity());
        itemRequest.setRetailPriceOverride( orderItemRequestDTO.getOverrideRetailPrice() );
        itemRequest.setSalePriceOverride( orderItemRequestDTO.getOverrideSalePrice() );
        itemRequest.setItemName( sku.getName() );
        itemRequest.setOrder( order );
        OrderItem item = orderItemService.createOrderItem(itemRequest);
        
        order.getOrderItems().add(item);

        request.setOrderItem(item);
        return context;
    }

}
