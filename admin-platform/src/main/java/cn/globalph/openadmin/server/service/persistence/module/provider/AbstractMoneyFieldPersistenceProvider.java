package cn.globalph.openadmin.server.service.persistence.module.provider;

import cn.globalph.common.currency.util.BroadleafCurrencyUtils;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationException;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.ExtractValueRequest;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Abstract persistence provider that provides a method to actually handle formatting moneys.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public abstract class AbstractMoneyFieldPersistenceProvider extends FieldPersistenceProviderAdapter {
    
    @Override
    public FieldProviderResponse extractValue(ExtractValueRequest extractValueRequest, Property property) throws PersistenceOperationException {
        if (!canHandleExtraction(extractValueRequest, property)) {
            return FieldProviderResponse.NOT_HANDLED;
        }
        
        if (extractValueRequest.getRequestedValue() == null) {
            return FieldProviderResponse.NOT_HANDLED;
        }
        
        property.setValue(formatValue((BigDecimal)extractValueRequest.getRequestedValue(), extractValueRequest, property));
        property.setDisplayValue(formatDisplayValue((BigDecimal)extractValueRequest.getRequestedValue(), extractValueRequest, property));
        
        return FieldProviderResponse.HANDLED_BREAK;
    }
    
    protected String formatValue(BigDecimal value, ExtractValueRequest extractValueRequest, Property property) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        format.setGroupingUsed(false);
        return format.format(value);
    }
    
    protected String formatDisplayValue(BigDecimal value, ExtractValueRequest extractValueRequest, Property property) {
        return BroadleafCurrencyUtils.getNumberFormatFromCache().format(value);
    }
    
    protected abstract boolean canHandleExtraction(ExtractValueRequest extractValueRequest, Property property);
        
}
