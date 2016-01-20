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
@Service("blNullPaymentGatewayConfigurationService")
public class NullPaymentGatewayConfigurationServiceImpl implements PaymentGatewayConfigurationService {

    @Resource(name = "blNullPaymentGatewayConfiguration")
    protected NullPaymentGatewayConfiguration configuration;

    @Resource(name = "blNullPaymentGatewayRollbackService")
    protected PaymentGatewayRollbackService rollbackService;

    @Resource(name = "blNullPaymentGatewayWebResponseService")
    protected PaymentGatewayWebResponseService webResponseService;

    @Resource(name = "blNullPaymentGatewayTransparentRedirectService")
    protected PaymentGatewayTransparentRedirectService transparentRedirectService;

    @Resource(name = "blNullPaymentGatewayTRExtensionHandler")
    protected TRCreditCardExtensionHandler creditCardExtensionHandler;

    @Resource(name = "blNullPaymentGatewayFieldExtensionHandler")
    protected PaymentGatewayFieldExtensionHandler fieldExtensionHandler;

    public PaymentGatewayConfiguration getConfiguration() {
        return configuration;
    }

    public PaymentGatewayTransactionService getTransactionService() {
        return null;
    }

    public PaymentGatewayTransactionConfirmationService getTransactionConfirmationService() {
        return null;
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
        return null;
    }

    public PaymentGatewayRollbackService getRollbackService() {
        return rollbackService;
    }

    public PaymentGatewayWebResponseService getWebResponseService() {
        return webResponseService;
    }

    public PaymentGatewayTransparentRedirectService getTransparentRedirectService() {
        return transparentRedirectService;
    }

    public TRCreditCardExtensionHandler getCreditCardExtensionHandler() {
        return creditCardExtensionHandler;
    }

    public PaymentGatewayFieldExtensionHandler getFieldExtensionHandler() {
        return fieldExtensionHandler;
    }

    public CreditCardTypesExtensionHandler getCreditCardTypesExtensionHandler() {
        return null;
    }

}
