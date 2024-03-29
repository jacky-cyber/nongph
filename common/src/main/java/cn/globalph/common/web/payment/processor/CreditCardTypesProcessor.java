
package cn.globalph.common.web.payment.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.element.AbstractLocalVariableDefinitionElementProcessor;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>The following processor will add any Payment Gateway specific Card Type 'codes' to the model if
 * the gateway requires that a 'Card Type' (e.g. Visa, MasterCard, etc...) be sent along with
 * the credit card number and expiry date.
 * </p>
 *
 * <p>This processor will put the key 'paymentGatewayCardTypes' on the model if there are any types available</p>
 *
 * <p>Here is an example:</p>
 *
 * <pre><code>
 *  <blc:credit_card_types >
 *      <div th:if="${paymentGatewayCardTypes != null}" class="form-group">
 *          <label for="cardNumber">Card Type</label>
 *          <select th:name="${#paymentGatewayField.mapName('creditCard.creditCardType')}">
 *              <option th:each="entry : ${paymentGatewayCardTypes}" th:value="${entry.key}" th:text="${entry.value}"></option>
 *          </select>
 *      </div>
 *  </blc:credit_card_types>
 * </code></pre>
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Component("blCreditCardTypesProcessor")
public class CreditCardTypesProcessor extends AbstractLocalVariableDefinitionElementProcessor {

    protected static final Log LOG = LogFactory.getLog(CreditCardTypesProcessor.class);

    @Resource(name = "blCreditCardTypesExtensionManager")
    protected CreditCardTypesExtensionManager extensionManager;

    public CreditCardTypesProcessor() {
        super("credit_card_types");
    }

    @Override
    public int getPrecedence() {
        return 100;
    }

    @Override
    protected boolean removeHostElement(Arguments arguments, Element element) {
        return false;
    }

    @Override
    protected Map<String, Object> getNewLocalVariables(Arguments arguments, Element element) {
        Map<String, Object> localVars = new HashMap<String, Object>();

        Map<String, String> creditCardTypes = new HashMap<String, String>();

        try {
            extensionManager.getHandlerProxy().populateCreditCardMap(creditCardTypes);
        } catch (Exception e) {
            LOG.warn("Unable to Populate Credit Card Types Map for this Payment Module, or card type is not needed.");
        }

        if (!creditCardTypes.isEmpty()) {
            localVars.put("paymentGatewayCardTypes", creditCardTypes);
        }

        return localVars;
    }



}
