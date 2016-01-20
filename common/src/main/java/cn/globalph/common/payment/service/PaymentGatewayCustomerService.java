
package cn.globalph.common.payment.service;

import cn.globalph.common.payment.dto.PaymentRequestDTO;
import cn.globalph.common.payment.dto.PaymentResponseDTO;
import cn.globalph.common.vendor.service.exception.PaymentException;

/**
 * <p>Several payment gateways allow you to manage Customer and Credit Card Information from the gateway allowing
 * you to create a transaction from the tokenized customer or payment method at a later date.
 * Note: These are usually extra features you need to pay for when you sign up with the Gateway</p>
 *
 * @author Elbert Bautista (elbertbautista)
 */
public interface PaymentGatewayCustomerService {

    public PaymentResponseDTO createGatewayCustomer(PaymentRequestDTO requestDTO) throws PaymentException;

    public PaymentResponseDTO updateGatewayCustomer(PaymentRequestDTO requestDTO) throws PaymentException;

    public PaymentResponseDTO deleteGatewayCustomer(PaymentRequestDTO requestDTO) throws PaymentException;

}
