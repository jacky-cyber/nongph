package cn.globalph.payment.service.gateway;

import cn.globalph.common.payment.service.PaymentGatewayConfiguration;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public interface NullPaymentGatewayHostedConfiguration extends PaymentGatewayConfiguration {

    public String getHostedRedirectUrl();

    public String getHostedRedirectReturnUrl();

}
