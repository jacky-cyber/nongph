
package cn.globalph.common.payment.service;

import cn.globalph.common.payment.dto.PaymentRequestDTO;
import cn.globalph.common.payment.dto.PaymentResponseDTO;
import cn.globalph.common.vendor.service.exception.PaymentException;

/**
 * <p>Certain Payment Integrations allow you to use a Hosted Solution,
 * such as PayPal Express and SagePay Form.
 * This API allows you to create the call to send a user to the Gateway's Hosted page
 * and to capture and record transaction responses back from them.</p>
 *
 * @author Elbert Bautista (elbertbautista)
 */
public interface PaymentGatewayHostedService {

    public PaymentResponseDTO requestHostedEndpoint(PaymentRequestDTO paymentRequestDTO) throws PaymentException;

}
