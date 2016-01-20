package cn.globalph.payment.service.gateway;

import cn.globalph.common.payment.PaymentType;
import cn.globalph.common.payment.dto.PaymentRequestDTO;
import cn.globalph.common.payment.dto.PaymentResponseDTO;
import cn.globalph.common.payment.service.PaymentGatewayHostedService;
import cn.globalph.common.vendor.service.exception.PaymentException;
import cn.globalph.vendor.nullPaymentGateway.service.payment.NullPaymentGatewayConstants;
import cn.globalph.vendor.nullPaymentGateway.service.payment.NullPaymentGatewayType;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * This is an example implementation of a {@link PaymentGatewayHostedService}.
 * This is just a sample that mimics what URL a real hosted payment gateway implementation
 * might generate. This mimics implementations like PayPal Express Checkout
 *
 * In order to use load this demo service, you will need to component scan
 * the package "cn.globalph".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blNullPaymentGatewayHostedService")
public class NullPaymentGatewayHostedServiceImpl implements PaymentGatewayHostedService {

    @Resource(name = "blNullPaymentGatewayHostedConfiguration")
    protected NullPaymentGatewayHostedConfiguration configuration;

    @Override
    public PaymentResponseDTO requestHostedEndpoint(PaymentRequestDTO requestDTO) throws PaymentException {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT,
                NullPaymentGatewayType.NULL_HOSTED_GATEWAY)
                .completeCheckoutOnCallback(requestDTO.isCompleteCheckoutOnCallback())
                .responseMap(NullPaymentGatewayConstants.ORDER_ID, requestDTO.getOrderId())
                .responseMap(NullPaymentGatewayConstants.TRANSACTION_AMT, requestDTO.getTransactionTotal())
                .responseMap(NullPaymentGatewayConstants.HOSTED_REDIRECT_URL,
                        configuration.getHostedRedirectUrl());
        return responseDTO;
    }

}
