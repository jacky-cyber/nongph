
package cn.globalph.common.web.payment.expression;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blPaymentGatewayFieldExtensionManager")
public class PaymentGatewayFieldExtensionManager extends ExtensionManager<PaymentGatewayFieldExtensionHandler> {

    public PaymentGatewayFieldExtensionManager() {
        super(PaymentGatewayFieldExtensionHandler.class);
    }

    @Override
    public boolean continueOnHandled() {
        return true;
    }

}
