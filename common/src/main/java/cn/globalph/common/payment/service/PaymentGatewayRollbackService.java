
package cn.globalph.common.payment.service;

import cn.globalph.common.payment.dto.PaymentRequestDTO;
import cn.globalph.common.payment.dto.PaymentResponseDTO;
import cn.globalph.common.vendor.service.exception.PaymentException;

/**
 * <p>This API allows each module to provide its own implementation for rollback.
 * Each module needs to implement this if for some reason the checkout workflow fails
 * after payments have been finalized and the submitted transaction needs to rollback.</p>
 *
 * @author Elbert Bautista (elbertbautista)
 */
public interface PaymentGatewayRollbackService {

    public PaymentResponseDTO rollbackAuthorize(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException;

    public PaymentResponseDTO rollbackCapture(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException;

    public PaymentResponseDTO rollbackAuthorizeAndCapture(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException;

    public PaymentResponseDTO rollbackRefund(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException;

}
