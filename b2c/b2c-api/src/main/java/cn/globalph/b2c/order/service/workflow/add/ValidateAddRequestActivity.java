package cn.globalph.b2c.order.service.workflow.add;

import org.apache.commons.lang.StringUtils;

import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.ProductOptionValidationService;
import cn.globalph.b2c.order.service.call.ActivityMessageDTO;
import cn.globalph.b2c.order.service.call.OrderItemRequestDTO;
import cn.globalph.b2c.order.service.exception.ProductOptionValidationException;
import cn.globalph.b2c.order.service.exception.RequiredAttributeNotProvidedException;
import cn.globalph.b2c.order.service.type.MessageType;
import cn.globalph.b2c.order.service.workflow.CartOperationRequest;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.ProductOption;
import cn.globalph.b2c.product.domain.ProductOptionValidationStrategyType;
import cn.globalph.b2c.product.domain.ProductOptionValue;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.workflow.ActivityMessages;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

public class ValidateAddRequestActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
    
    @Resource(name = "blOrderService")
    protected OrderService orderService;
    
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    @Resource(name = "blProductOptionValidationService")
    protected ProductOptionValidationService productOptionValidationService;
    
    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;

    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();
        
        // Quantity was not specified or was equal to zero. We will not throw an exception,
        // but we will preven the workflow from continuing to execute
        if (orderItemRequestDTO.getQuantity() == null || orderItemRequestDTO.getQuantity() == 0) {
            context.stopProcess();
            return context;
        }

        // Throw an exception if the user tried to add a negative quantity of something
        if (orderItemRequestDTO.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        // Throw an exception if the user did not specify an order to add the item to
        if (request.getOrder() == null) {
            throw new IllegalArgumentException("Order is required when adding item to order");
        }
          
        if (orderItemRequestDTO.getSkuId() == null) 
            throw new IllegalArgumentException("Product was specified but no matching SKU was found for skuId: " + orderItemRequestDTO.getSkuId());
         
        // 验证SKU ID是否存在
        Sku sku = catalogService.findSkuById(orderItemRequestDTO.getSkuId());
        if (sku == null) 
            throw new IllegalArgumentException("Product was specified but no matching SKU was found for skuId: " + orderItemRequestDTO.getSkuId());
        if (!sku.isActive()) {
            throw new IllegalArgumentException("The requested skuId of " + sku.getId() + " is no longer active");
        } 
        return context;
    }
    
    protected Sku findMatchingSku(Product product, Map<String, String> attributeValues, ActivityMessages messages) {
        Map<String, String> attributeValuesForSku = new HashMap<String,String>();
        // Verify that required product-option values were set.

        if (product != null && product.getProductOptions() != null && product.getProductOptions().size() > 0) {
            for (ProductOption productOption : product.getProductOptions()) {
                if (productOption.getRequired() && (productOption.getProductOptionValidationStrategyType() == null ||
                        productOption.getProductOptionValidationStrategyType().getRank() <= ProductOptionValidationStrategyType.ADD_ITEM.getRank())) {
                    if (StringUtils.isEmpty(attributeValues.get(productOption.getAttributeName()))) {
                        throw new RequiredAttributeNotProvidedException("Unable to add to product ("+ product.getId() +") cart. Required attribute was not provided: " + productOption.getAttributeName());
                    } else if (productOption.getUseInSkuGeneration()) {
                        attributeValuesForSku.put(productOption.getAttributeName(), attributeValues.get(productOption.getAttributeName()));
                    	   }
                	 }
                if (!productOption.getRequired() && StringUtils.isEmpty(attributeValues.get(productOption.getAttributeName()))) {
                    //if the productoption is not required, and user has not set the optional value, then we dont need to validate
                } else if (productOption.getProductOptionValidationType() != null && (productOption.getProductOptionValidationStrategyType() == null || productOption.getProductOptionValidationStrategyType().getRank() <= ProductOptionValidationStrategyType.ADD_ITEM.getRank())) {
                        productOptionValidationService.validate(productOption, attributeValues.get(productOption.getAttributeName()));
                     }
                if((productOption.getProductOptionValidationStrategyType() != null && productOption.getProductOptionValidationStrategyType().getRank() > ProductOptionValidationStrategyType.ADD_ITEM.getRank()))
                {
                    //we need to validate however, we will not error out since this message is 
                    try {
                        productOptionValidationService.validate(productOption, attributeValues.get(productOption.getAttributeName()));
                    } catch (ProductOptionValidationException e) {
                        ActivityMessageDTO msg = new ActivityMessageDTO(MessageType.PRODUCT_OPTION.getType(), 1, e.getMessage());
                        msg.setErrorCode(productOption.getErrorCode());
                        messages.getActivityMessages().add(msg);
                    }
                    
                }
            }
            

            if (product !=null && product.getActiveSkus() != null) {
                for (Sku sku : product.getActiveSkus()) {
                   if (checkSkuForMatch(sku, attributeValuesForSku)) {
                       return sku;
                   }
                }
            }
        }

        return null;
    }

    protected boolean checkSkuForMatch(Sku sku, Map<String,String> attributeValues) {
        if (attributeValues == null || attributeValues.size() == 0) {
            return false;
        }

        for (String attributeName : attributeValues.keySet()) {
            boolean optionValueMatchFound = false;
            for (ProductOptionValue productOptionValue : sku.getProductOptionValues()) {
                if (productOptionValue.getProductOption().getAttributeName().equals(attributeName)) {
                    if (productOptionValue.getAttributeValue().equals(attributeValues.get(attributeName))) {
                        optionValueMatchFound = true;
                        break;
                    } else {
                        return false;
                    }
                }
            }

            if (optionValueMatchFound) {
                continue;
            } else {
                return false;
            }
        }

        return true;
    }
}
