package cn.globalph.openadmin.server.service.persistence.module.provider;

import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.ExtractValueRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Persistence provider capable of extracting friendly display values for Money fields
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Scope("prototype")
@Component("blMoneyFieldPersistenceProvider")
public class MoneyFieldPersistenceProvider extends AbstractMoneyFieldPersistenceProvider {
    
    @Override
    public int getOrder() {
        return FieldPersistenceProvider.MONEY;
    }
    
    @Override
    protected boolean canHandleExtraction(ExtractValueRequest extractValueRequest, Property property) {
        return extractValueRequest.getMetadata().getFieldType() == SupportedFieldType.MONEY;
    }
}
