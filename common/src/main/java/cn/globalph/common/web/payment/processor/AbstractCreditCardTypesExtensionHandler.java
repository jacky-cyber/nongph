package cn.globalph.common.web.payment.processor;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;

import java.util.Map;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public abstract class AbstractCreditCardTypesExtensionHandler extends AbstractExtensionHandler
        implements CreditCardTypesExtensionHandler {

    @Override
    public ExtensionResultStatusType populateCreditCardMap(Map<String, String> creditCardTypes) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}
