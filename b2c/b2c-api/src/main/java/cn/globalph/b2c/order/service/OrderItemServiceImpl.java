package cn.globalph.b2c.order.service;

import cn.globalph.b2c.catalog.service.dynamic.DynamicSkuPrices;
import cn.globalph.b2c.catalog.service.dynamic.DynamicSkuPricingService;
import cn.globalph.b2c.order.dao.OrderItemDao;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.call.AbstractOrderItemRequest;
import cn.globalph.b2c.order.service.call.BundleOrderItemRequest;
import cn.globalph.b2c.order.service.call.DiscreteOrderItemRequest;
import cn.globalph.b2c.order.service.call.OrderItemRequest;
import cn.globalph.b2c.order.service.call.OrderItemRequestDTO;
import cn.globalph.b2c.order.service.call.ProductBundleOrderItemRequest;
import cn.globalph.b2c.order.service.type.OrderItemType;
import cn.globalph.b2c.product.domain.ProductOption;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

@Service("blOrderItemService")
public class OrderItemServiceImpl implements OrderItemService {

    @Resource(name="blOrderItemDao")
    protected OrderItemDao orderItemDao;
    
    @Resource(name="blDynamicSkuPricingService" )
    protected DynamicSkuPricingService dynamicSkuPricingService;

    @Override
    public OrderItem readOrderItemById(final Long orderItemId) {
        return orderItemDao.readOrderItemById(orderItemId);
     }

    @Override
    public OrderItem saveOrderItem(final OrderItem orderItem) {
        return orderItemDao.saveOrderItem(orderItem);
     }
    
    @Override
    public void delete(final OrderItem item) {
        orderItemDao.delete(item);
    }
    
    protected void populateDiscreteOrderItem(OrderItem item, AbstractOrderItemRequest itemRequest) {
        item.setSku(itemRequest.getSku());
        item.setQuantity(itemRequest.getQuantity());
        item.setOrder(itemRequest.getOrder());
    }

    @Override
    public OrderItem createOrderItem(final OrderItemRequest itemRequest) {
        final OrderItem item = orderItemDao.create(OrderItemType.BASIC);
        item.setSku( itemRequest.getSku() );
        item.setName(itemRequest.getItemName());
        
        item.setQuantity(itemRequest.getQuantity());
        item.setOrder(itemRequest.getOrder());
        
        
        if (itemRequest.getSalePriceOverride() != null) {
            item.setSalePriceOverride(Boolean.TRUE);
            item.setSalePrice(itemRequest.getSalePriceOverride());
        } else {
        	item.setSalePriceOverride(Boolean.FALSE);
        	item.setSalePrice( itemRequest.getSku().getSalePrice() );
        }

        if (itemRequest.getRetailPriceOverride() != null) {
            item.setRetailPriceOverride(Boolean.TRUE);
            item.setRetailPrice(itemRequest.getRetailPriceOverride());
        } else {
        	item.setRetailPriceOverride(Boolean.FALSE);
        	item.setRetailPrice(itemRequest.getSku().getRetailPrice());
        }
        
        return item;
     }

    @Override
    public OrderItem updateDiscreteOrderItem(OrderItem item, final DiscreteOrderItemRequest itemRequest) {
        List<ProductOption> productOptions = null;
        productOptions = item.getSku().getProduct().getProductOptions();
        List<String> removeKeys = new ArrayList<String>();
        if (productOptions != null && itemRequest.getItemAttributes() != null) {
            for (String name : itemRequest.getItemAttributes().keySet()) {
                //we do not let them update all product options. 
                //Only allow them to update those options that can have validation to take place at later time
                //if  option.getProductOptionValidationType()  is null then it might change the sku, so we dont allow those
                for (ProductOption option : productOptions) {
                    if (option.getAttributeName().equals(name) && option.getProductOptionValidationStrategyType() == null) {
                        removeKeys.add(name);
                        break;
                    	   }
                	  }
            	}
           }
        for (String name : removeKeys) {
            itemRequest.getItemAttributes().remove(name);
           }
        return item;
    }

    public OrderItem createDiscreteOrderItem(final AbstractOrderItemRequest itemRequest) {
        final OrderItem item = orderItemDao.create(OrderItemType.BASIC);
        populateDiscreteOrderItem(item, itemRequest);
        item.setSalePrice(itemRequest.getSku().getSalePrice());
        item.setRetailPrice(itemRequest.getSku().getRetailPrice());
        // item.updatePrices();
        item.updateSaleAndRetailPrices();

        item.assignFinalPrice();

        return item;
      }
    
    @Override
    public OrderItem createDynamicPriceDiscreteOrderItem(final DiscreteOrderItemRequest itemRequest, @SuppressWarnings("rawtypes") HashMap skuPricingConsiderations) {
        final OrderItem item = orderItemDao.create(OrderItemType.EXTERNALLY_PRICED);
        populateDiscreteOrderItem(item, itemRequest);

        DynamicSkuPrices prices = dynamicSkuPricingService.getSkuPrices(itemRequest.getSku(), skuPricingConsiderations);
        item.setRetailPrice(prices.getRetailPrice());
        item.setSalePrice(prices.getSalePrice());
        item.setSalePrice(prices.getSalePrice());
        item.setRetailPrice(prices.getRetailPrice());

        if (itemRequest.getSalePriceOverride() != null) {
            item.setSalePriceOverride(Boolean.TRUE);
            item.setSalePrice(itemRequest.getSalePriceOverride());
           }

        if (itemRequest.getRetailPriceOverride() != null) {
            item.setRetailPriceOverride(Boolean.TRUE);
            item.setRetailPrice(itemRequest.getRetailPriceOverride());
           }

        return item;
    }

    @Override
    public OrderItem createBundleOrderItem(final BundleOrderItemRequest itemRequest) {
        final OrderItem item = orderItemDao.create(OrderItemType.BASIC);
        item.setQuantity(itemRequest.getQuantity());
        item.setName(itemRequest.getName());
        item.setOrder(itemRequest.getOrder());

        if (itemRequest.getSalePriceOverride() != null) {
            item.setSalePriceOverride(Boolean.TRUE);
            item.setSalePrice(itemRequest.getSalePriceOverride());
           }

        if (itemRequest.getRetailPriceOverride() != null) {
            item.setRetailPriceOverride(Boolean.TRUE);
            item.setRetailPrice(itemRequest.getRetailPriceOverride());
           }

        return item;
    }
    
    @Override
    public OrderItem createBundleOrderItem(final ProductBundleOrderItemRequest itemRequest, boolean saveItem) {
        OrderItem bundleOrderItem = orderItemDao.create(OrderItemType.BASIC);
        bundleOrderItem.setQuantity(itemRequest.getQuantity());
        bundleOrderItem.setSku(itemRequest.getSkuBundle());
        bundleOrderItem.setName(itemRequest.getName());
        bundleOrderItem.setOrder(itemRequest.getOrder());

        if (itemRequest.getSalePriceOverride() != null) {
            bundleOrderItem.setSalePriceOverride(Boolean.TRUE);
            bundleOrderItem.setSalePrice(itemRequest.getSalePriceOverride());
           }

        if (itemRequest.getRetailPriceOverride() != null) {
            bundleOrderItem.setRetailPriceOverride(Boolean.TRUE);
            bundleOrderItem.setRetailPrice(itemRequest.getRetailPriceOverride());
           }
        
        if (saveItem) {
            bundleOrderItem = saveOrderItem(bundleOrderItem);
           }

        return bundleOrderItem;
    }

    @Override
    public OrderItem createBundleOrderItem(final ProductBundleOrderItemRequest itemRequest) {
        return createBundleOrderItem(itemRequest, true);
    }
    
    @Override
    public OrderItemRequestDTO buildOrderItemRequestDTOFromOrderItem(OrderItem item) {
        OrderItemRequestDTO orderItemRequest; 
        orderItemRequest = new OrderItemRequestDTO();
        orderItemRequest.setQuantity(item.getQuantity());
        orderItemRequest.setSkuId(item.getSku().getId());
        return orderItemRequest;
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(Long orderId) {
        return orderItemDao.readOrderItemsByOrderId(orderId);
    }
}
