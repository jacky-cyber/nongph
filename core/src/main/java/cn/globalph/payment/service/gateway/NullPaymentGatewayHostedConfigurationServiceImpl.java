package cn.globalph.payment.service.gateway;

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
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blNullPaymentGatewayHostedConfigurationService")
public class NullPaymentGatewayHostedConfigurationServiceImpl implements PaymentGatewayConfigurationService {

    @Resource(name = "blNullPaymentGatewayHostedConfiguration")
    protected NullPaymentGatewayHostedConfiguration configuration;

    @Resource(name = "blNullPaymentGatewayHostedRollbackService")
    protected PaymentGatewayRollbackService rollbackService;

    @Resource(name = "blNullPaymentGatewayHostedService")
    protected PaymentGatewayHostedService hostedService;

    @Resource(name = "blNullPaymentGatewayHostedTransactionConfirmationService")
    protected PaymentGatewayTransactionConfirmationService transactionConfirmationService;

    @Resource(name = "blNullPaymentGatewayHostedWebResponseService")
    protected PaymentGatewayWebResponseService webResponseService;

    public PaymentGatewayConfiguration getConfiguration() {
        return configuration;
    }

    public PaymentGatewayTransactionService getTransactionService() {
        return null;
    }

    public PaymentGatewayTransactionConfirmationService getTransactionConfirmationService() {
        return transactionConfirmationService;
    }

    public PaymentGatewayReportingService getReportingService() {
        return null;
    }

    public PaymentGatewayCreditCardService getCreditCardService() {
        return null;
    }

    public PaymentGatewayCustomerService getCustomerService() {
        return null;
    }

    public PaymentGatewaySubscriptionService getSubscriptionService() {
        return null;
    }

    public PaymentGatewayFraudService getFraudService() {
        return null;
    }

    public PaymentGatewayHostedService getHostedService() {
        return hostedService;
    }

    public PaymentGatewayRollbackService getRollbackService() {
        return rollbackService;
    }

    public PaymentGatewayWebResponseService getWebResponseService() {
        return webResponseService;
    }

    public PaymentGatewayTransparentRedirectService getTransparentRedirectService() {
        return null;
    }

    public TRCreditCardExtensionHandler getCreditCardExtensionHandler() {
        return null;
    }

    public PaymentGatewayFieldExtensionHandler getFieldExtensionHandler() {
        return null;
    }

    public CreditCardTypesExtensionHandler getCreditCardTypesExtensionHandler() {
        return null;
    }

}
