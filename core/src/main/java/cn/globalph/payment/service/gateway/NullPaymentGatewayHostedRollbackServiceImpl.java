package cn.globalph.payment.service.gateway;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.money.Money;
import cn.globalph.common.payment.PaymentTransactionType;
import cn.globalph.common.payment.PaymentType;
import cn.globalph.common.payment.dto.PaymentRequestDTO;
import cn.globalph.common.payment.dto.PaymentResponseDTO;
import cn.globalph.common.payment.service.PaymentGatewayRollbackService;
import cn.globalph.common.vendor.service.exception.PaymentException;
import cn.globalph.vendor.nullPaymentGateway.service.payment.NullPaymentGatewayType;

import org.springframework.stereotype.Service;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blNullPaymentGatewayHostedRollbackService")
public class NullPaymentGatewayHostedRollbackServiceImpl implements PaymentGatewayRollbackService {

    protected static final Log LOG = LogFactory.getLog(NullPaymentGatewayHostedRollbackServiceImpl.class);

    @Override
    public PaymentResponseDTO rollbackAuthorize(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Null Payment Hosted Gateway - Rolling back authorize transaction with amount: " + transactionToBeRolledBack.getTransactionTotal());
        }

        return new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT,
                NullPaymentGatewayType.NULL_HOSTED_GATEWAY)
                .rawResponse("rollback authorize - successful")
                .successful(true)
                .paymentTransactionType(PaymentTransactionType.REVERSE_AUTH)
                .amount(new Money(transactionToBeRolledBack.getTransactionTotal()));

    }

    @Override
    public PaymentResponseDTO rollbackCapture(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
        throw new PaymentException("The Rollback Capture method is not supported for this module");
    }

    @Override
    public PaymentResponseDTO rollbackAuthorizeAndCapture(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Null Payment Hosted Gateway - Rolling back authorize and capture transaction with amount: " + transactionToBeRolledBack.getTransactionTotal());
        }

        return new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT,
                NullPaymentGatewayType.NULL_HOSTED_GATEWAY)
                .rawResponse("rollback authorize and capture - successful")
                .successful(true)
                .paymentTransactionType(PaymentTransactionType.VOID)
                .amount(new Money(transactionToBeRolledBack.getTransactionTotal()));
    }

    @Override
    public PaymentResponseDTO rollbackRefund(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
        throw new PaymentException("The Rollback Refund method is not supported for this module");
    }

}

