package cn.globalph.vendor.nullPaymentGateway.web.processor;

import cn.globalph.common.payment.dto.PaymentRequestDTO;
import cn.globalph.common.payment.dto.PaymentResponseDTO;
import cn.globalph.common.payment.service.PaymentGatewayHostedService;
import cn.globalph.common.vendor.service.exception.PaymentException;
import cn.globalph.vendor.nullPaymentGateway.service.payment.NullPaymentGatewayConstants;

import org.springframework.stereotype.Component;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;
import org.thymeleaf.standard.expression.StandardExpressionProcessor;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>A Thymeleaf processor that will generate a Mock Hosted Link given a passed in PaymentRequestDTO.</p>
 *
 * <pre><code>
 * <form blc:null_payment_hosted_action="${paymentRequestDTO}" complete_checkout="${false}" method="POST">
 *   <input type="image" src="https://www.paypal.com/en_US/i/btn/btn_xpressCheckout.gif" align="left" style="margin-right:7px;" alt="Submit Form" />
 * </form>
 * </code></pre>
 *
 * In order to use this sample processor, you will need to component scan
 * the package "cn.globalph".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Component("blNullPaymentGatewayHostedActionProcessor")
public class NullPaymentGatewayHostedActionProcessor extends AbstractAttributeModifierAttrProcessor {

    @Resource(name = "blNullPaymentGatewayHostedService")
    private PaymentGatewayHostedService paymentGatewayHostedService;

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public NullPaymentGatewayHostedActionProcessor() {
        super("null_payment_hosted_action");
    }

    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    protected Map<String, String> getModifiedAttributeValues(Arguments arguments, Element element, String attributeName) {
        Map<String, String> attrs = new HashMap<String, String>();

        PaymentRequestDTO requestDTO = (PaymentRequestDTO) StandardExpressionProcessor.processExpression(arguments, element.getAttributeValue(attributeName));
        String url = "";

        if (requestDTO != null) {
            if ( element.getAttributeValue("complete_checkout") != null) {
                Boolean completeCheckout = (Boolean) StandardExpressionProcessor.processExpression(arguments,
                        element.getAttributeValue("complete_checkout"));
                element.removeAttribute("complete_checkout");
                requestDTO.completeCheckoutOnCallback(completeCheckout);
            }

            try {
                PaymentResponseDTO responseDTO = paymentGatewayHostedService.requestHostedEndpoint(requestDTO);
                url = responseDTO.getResponseMap().get(NullPaymentGatewayConstants.HOSTED_REDIRECT_URL).toString();
            } catch (PaymentException e) {
                throw new RuntimeException("Unable to Create Null Payment Gateway Hosted Link", e);
            }
        }

        attrs.put("action", url);
        return attrs;
    }

    @Override
    protected ModificationType getModificationType(Arguments arguments, Element element, String attributeName, String newAttributeName) {
        return ModificationType.SUBSTITUTION;
    }

    @Override
    protected boolean removeAttributeIfEmpty(Arguments arguments, Element element, String attributeName, String newAttributeName) {
        return true;
    }

    @Override
    protected boolean recomputeProcessorsAfterExecution(Arguments arguments, Element element, String attributeName) {
        return false;
    }
}
