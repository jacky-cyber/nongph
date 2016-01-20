
package cn.globalph.common.payment.service;

import cn.globalph.common.web.payment.expression.PaymentGatewayFieldExtensionHandler;
import cn.globalph.common.web.payment.processor.CreditCardTypesExtensionHandler;
import cn.globalph.common.web.payment.processor.TRCreditCardExtensionHandler;

/**
 * Each payment gateway module should configure an instance of this. In order for multiple gateways to exist in the system
 * at the same time, a list of these is managed via the {@link PaymentGatewayConfigurationServiceProvider}. This allows for proper
 * delegation to the right gateway to perform operations against via different order payments on an order.
 * 
 * @author Elbert Bautista (elbertbautista)
 * @author Phillip Verheyden (phillipuniverse)
 */
public interface PaymentGatewayConfigurationService {

    public PaymentGatewayConfiguration getConfiguration();

    public PaymentGatewayTransactionService getTransactionService();

    public PaymentGatewayTransactionConfirmationService getTransactionConfirmationService();

    public PaymentGatewayReportingService getReportingService();

    public PaymentGatewayCreditCardService getCreditCardService();

    public PaymentGatewayCustomerService getCustomerService();

    public PaymentGatewaySubscriptionService getSubscriptionService();

    public PaymentGatewayFraudService getFraudService();

    public PaymentGatewayHostedService getHostedService();

    public PaymentGatewayRollbackService getRollbackService();

    public PaymentGatewayWebResponseService getWebResponseService();

    public PaymentGatewayTransparentRedirectService getTransparentRedirectService();

    public TRCreditCardExtensionHandler getCreditCardExtensionHandler();

    public PaymentGatewayFieldExtensionHandler getFieldExtensionHandler();

    public CreditCardTypesExtensionHandler getCreditCardTypesExtensionHandler();

}
