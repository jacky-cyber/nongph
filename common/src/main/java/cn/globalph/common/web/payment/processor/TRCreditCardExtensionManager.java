
package cn.globalph.common.web.payment.processor;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blTRCreditCardExtensionManager")
public class TRCreditCardExtensionManager extends ExtensionManager<TRCreditCardExtensionHandler> {

    public TRCreditCardExtensionManager() {
        super(TRCreditCardExtensionHandler.class);
    }

    @Override
    public boolean continueOnHandled() {
        return true;
    }

}