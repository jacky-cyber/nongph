
package cn.globalph.common.web.payment.processor;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blCreditCardTypesExtensionManager")
public class CreditCardTypesExtensionManager extends ExtensionManager<CreditCardTypesExtensionHandler> {

    public CreditCardTypesExtensionManager() {
        super(CreditCardTypesExtensionHandler.class);
    }

    @Override
    public boolean continueOnHandled() {
        return true;
    }

}
