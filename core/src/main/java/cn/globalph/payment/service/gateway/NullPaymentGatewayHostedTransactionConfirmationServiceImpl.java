package cn.globalph.payment.service.gateway;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.money.Money;
import cn.globalph.common.payment.PaymentTransactionType;
import cn.globalph.common.payment.PaymentType;
import cn.globalph.common.payment.dto.PaymentRequestDTO;
import cn.globalph.common.payment.dto.PaymentResponseDTO;
import cn.globalph.common.payment.service.PaymentGatewayTransactionConfirmationService;
import cn.globalph.common.vendor.service.exception.PaymentException;
import cn.globalph.vendor.nullPaymentGateway.service.payment.NullPaymentGatewayType;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blNullPaymentGatewayHostedTransactionConfirmationService")
public class NullPaymentGatewayHostedTransactionConfirmationServiceImpl implements PaymentGatewayTransactionConfirmationService {

    protected static final Log LOG = LogFactory.getLog(NullPaymentGatewayHostedTransactionConfirmationServiceImpl.class);

    @Resource(name = "blNullPaymentGatewayHostedConfiguration")
    protected NullPaymentGatewayHostedConfiguration configuration;

    @Override
    public PaymentResponseDTO confirmTransaction(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Null Payment Hosted Gateway - Confirming Transaction with amount: " + paymentRequestDTO.getTransactionTotal());
        }

        PaymentTransactionType type = PaymentTransactionType.AUTHORIZE_AND_CAPTURE;
        if (!configuration.isPerformAuthorizeAndCapture()) {
            type = PaymentTransactionType.AUTHORIZE;
        }

        return new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT,
                NullPaymentGatewayType.NULL_HOSTED_GATEWAY)
                .rawResponse("confirmation - successful")
                .successful(true)
                .paymentTransactionType(type)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()));
    }

}
