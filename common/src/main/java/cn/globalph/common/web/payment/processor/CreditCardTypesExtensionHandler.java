
package cn.globalph.common.web.payment.processor;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;

import java.util.Map;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public interface CreditCardTypesExtensionHandler extends ExtensionHandler {

    /**
     * The registered Extension Handler will populate any specific Payment Gateway
     * codes required for Credit Card Types.
     *
     * key = "Card Type Code to send to the Gateway"
     * value = "Friendly Name of Card type (e.g. Visa, MasterCard, etc...)"
     *
     * @param creditCardTypes
     * @return
     */
    public ExtensionResultStatusType populateCreditCardMap(Map<String, String> creditCardTypes);

}
