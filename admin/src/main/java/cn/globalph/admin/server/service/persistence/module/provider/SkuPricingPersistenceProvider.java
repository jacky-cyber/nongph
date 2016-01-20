package cn.globalph.admin.server.service.persistence.module.provider;

import org.apache.commons.beanutils.PropertyUtils;

import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.SkuImpl;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationException;
import cn.globalph.openadmin.server.service.persistence.module.FieldManager;
import cn.globalph.openadmin.server.service.persistence.module.provider.AbstractMoneyFieldPersistenceProvider;
import cn.globalph.openadmin.server.service.persistence.module.provider.FieldPersistenceProvider;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.ExtractValueRequest;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Persistence provider capable of extracting friendly display values for Sku prices, taking currency into consideration.
 * 
 */
@Scope("prototype")
@Component("blSkuPricingPersistenceProvider")
public class SkuPricingPersistenceProvider extends AbstractMoneyFieldPersistenceProvider {
    
    public static int ORDER = FieldPersistenceProvider.MONEY - 1000;
    
    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    public FieldProviderResponse extractValue(ExtractValueRequest extractValueRequest, Property property) throws PersistenceOperationException {
        if (!canHandleExtraction(extractValueRequest, property)) {
            return FieldProviderResponse.NOT_HANDLED;
        }
        
        Object displayValue = extractValueRequest.getRequestedValue();
        if (displayValue == null) {
            try {
                displayValue = PropertyUtils.getProperty(extractValueRequest.getEntity(), property.getName());
                ((BasicFieldMetadata)property.getFieldMetadata()).setDerived(true);
            } catch (Exception e) {
                //swallow all exceptions because null is fine for the display value
            }
        }
        Object actualValue = extractValueRequest.getRequestedValue();
        
        property.setValue(formatValue(actualValue, extractValueRequest, property));
        property.setDisplayValue(formatDisplayValue(displayValue, extractValueRequest, property));

        return FieldProviderResponse.HANDLED_BREAK;
    }
    
    protected String formatValue(Object value, ExtractValueRequest extractValueRequest, Property property) {
        if (value == null) {
            return null;
        }
        BigDecimal decimalValue = (value instanceof Money) ? ((Money)value).getAmount() : (BigDecimal) value;
        return super.formatValue(decimalValue, extractValueRequest, property);
    }
    
    protected String formatDisplayValue(Object value, ExtractValueRequest extractValueRequest, Property property) {
        if (value == null) {
            return null;
        }
        BigDecimal decimalValue = (value instanceof Money) ? ((Money)value).getAmount() : (BigDecimal) value;
        return super.formatDisplayValue(decimalValue, extractValueRequest, property);
    }
    
    /**
     * Handle all fields that have declared themselves to be apart of a Sku and have a field type of Money
     *  
     * @param extractValueRequest
     * @param property
     * @return whether or not we can handle extraction
     */
    @Override
    protected boolean canHandleExtraction(ExtractValueRequest extractValueRequest, Property property) {
        return (
                extractValueRequest.getMetadata().getTargetClass().equals(SkuImpl.class.getName()) ||
                extractValueRequest.getMetadata().getTargetClass().equals(Sku.class.getName())
               ) 
                && !property.getName().contains(FieldManager.MAPFIELDSEPARATOR)
                && SupportedFieldType.MONEY.equals(extractValueRequest.getMetadata().getFieldType());
    }
    
    protected boolean isDefaultSkuProperty(ExtractValueRequest extractValueRequest, Property property) {
        return property.getName().startsWith("defaultSku");
    }

}
