package cn.globalph.payment.service.gateway;

import cn.globalph.common.payment.service.PaymentGatewayConfiguration;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public interface NullPaymentGatewayConfiguration extends PaymentGatewayConfiguration {

    public String getTransparentRedirectUrl();

    public String getTransparentRedirectReturnUrl();

}
