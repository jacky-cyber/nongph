package cn.globalph.b2c.checkout.service.gateway;

import cn.globalph.common.payment.service.PaymentGatewayConfiguration;
import cn.globalph.common.payment.service.PaymentGatewayConfigurationService;
import cn.globalph.common.payment.service.PaymentGatewayCreditCardService;
import cn.globalph.common.payment.service.PaymentGatewayCustomerService;
import cn.globalph.common.payment.service.PaymentGatewayFraudService;
import cn.globalph.common.payment.service.PaymentGatewayHostedService;
import cn.globalph.common.payment.service.PaymentGatewayReportingService;
import cn.globalph.common.payment.service.PaymentGatewayRollbackService;
import cn.globalph.common.payment.service.PaymentGatewaySubscriptionService;
import cn.globalph.common.payment.service.PaymentGatewayTransactionConfirmationService;
import cn.globalph.common.payment.service.PaymentGatewayTransactionService;
import cn.globalph.common.payment.service.PaymentGatewayTransparentRedirectService;
import cn.globalph.common.payment.service.PaymentGatewayWebResponseService;
import cn.globalph.common.web.payment.expression.PaymentGatewayFieldExtensionHandler;
import cn.globalph.common.web.payment.processor.CreditCardTypesExtensionHandler;
import cn.globalph.common.web.payment.processor.TRCreditCardExtensionHandler;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * A Default Configuration to handle Passthrough Payments, for example COD payments.
 * This default implementation just supports a rollback service.
 */
@Service("blPassthroughPaymentConfigurationService")
public class PassthroughPaymentConfigurationServiceImpl implements PaymentGatewayConfigurationService {

    @Resource(name = "blPassthroughPaymentConfiguration")
    protected PaymentGatewayConfiguration configuration;

    @Resource(name = "blPassthroughPaymentRollbackService")
    protected PaymentGatewayRollbackService rollbackService;

    @Override
    public PaymentGatewayConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PaymentGatewayTransactionService getTransactionService() {
        return null;
    }

    @Override
    public PaymentGatewayTransactionConfirmationService getTransactionConfirmationService() {
        return null;
    }

    @Override
    public PaymentGatewayReportingService getReportingService() {
        return null;
    }

    @Override
    public PaymentGatewayCreditCardService getCreditCardService() {
        return null;
    }

    @Override
    public PaymentGatewayCustomerService getCustomerService() {
        return null;
    }

    @Override
    public PaymentGatewaySubscriptionService getSubscriptionService() {
        return null;
    }

    @Override
    public PaymentGatewayFraudService getFraudService() {
        return null;
    }

    @Override
    public PaymentGatewayHostedService getHostedService() {
        return null;
    }

    @Override
    public PaymentGatewayRollbackService getRollbackService() {
        return rollbackService;
    }

    @Override
    public PaymentGatewayWebResponseService getWebResponseService() {
        return null;
    }

    @Override
    public PaymentGatewayTransparentRedirectService getTransparentRedirectService() {
        return null;
    }

    @Override
    public TRCreditCardExtensionHandler getCreditCardExtensionHandler() {
        return null;
    }

    @Override
    public PaymentGatewayFieldExtensionHandler getFieldExtensionHandler() {
        return null;
    }

    @Override
    public CreditCardTypesExtensionHandler getCreditCardTypesExtensionHandler() {
        return null;
    }
}
