package cn.globalph.payment.service.gateway;

import cn.globalph.common.payment.PaymentGatewayType;
import cn.globalph.vendor.nullPaymentGateway.service.payment.NullPaymentGatewayType;

import org.springframework.stereotype.Service;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blNullPaymentGatewayHostedConfiguration")
public class NullPaymentGatewayHostedConfigurationImpl implements NullPaymentGatewayHostedConfiguration {

    protected int failureReportingThreshold = 1;

    protected boolean performAuthorizeAndCapture = true;

    @Override
    public String getHostedRedirectUrl() {
        return "/hosted/null-checkout";
    }

    @Override
    public String getHostedRedirectReturnUrl() {
        return "/null-checkout/hosted/return";
    }

    @Override
    public boolean isPerformAuthorizeAndCapture() {
        return true;
    }

    @Override
    public void setPerformAuthorizeAndCapture(boolean performAuthorizeAndCapture) {
        this.performAuthorizeAndCapture = performAuthorizeAndCapture;
    }

    @Override
    public int getFailureReportingThreshold() {
        return failureReportingThreshold;
    }

    @Override
    public void setFailureReportingThreshold(int failureReportingThreshold) {
        this.failureReportingThreshold = failureReportingThreshold;
    }

    @Override
    public boolean handlesAuthorize() {
        return true;
    }

    @Override
    public boolean handlesCapture() {
        return false;
    }

    @Override
    public boolean handlesAuthorizeAndCapture() {
        return true;
    }

    @Override
    public boolean handlesReverseAuthorize() {
        return false;
    }

    @Override
    public boolean handlesVoid() {
        return false;
    }

    @Override
    public boolean handlesRefund() {
        return false;
    }

    @Override
    public boolean handlesPartialCapture() {
        return false;
    }

    @Override
    public boolean handlesMultipleShipment() {
        return false;
    }

    @Override
    public boolean handlesRecurringPayment() {
        return false;
    }

    @Override
    public boolean handlesSavedCustomerPayment() {
        return false;
    }

    @Override
    public boolean handlesMultiplePayments() {
        return false;
    }

    @Override
    public PaymentGatewayType getGatewayType() {
        return NullPaymentGatewayType.NULL_HOSTED_GATEWAY;
    }
}
