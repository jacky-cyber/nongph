
package cn.globalph.common.payment.service;

import cn.globalph.common.vendor.service.exception.PaymentException;
import cn.globalph.common.vendor.service.monitor.ServiceStatusDetectable;
import cn.globalph.common.vendor.service.type.ServiceStatusType;

/**
 * <p>All payment gateway classes that intend to make an external call, either manually
 * from an HTTP Post or through an SDK which makes its own external call, should
 * extend this class. The implementations should override the abstract methods:
 * communicateWithVendor(), and getFailureReportingThreshold();</p>
 *
 * <p>The generic Type 'T' represents the payment request object that is going to be sent to the external gateway.
 * The generic Type 'R' reprenents the payment result object that will be returned</p>
 *
 * <p>This allows anyone using the framework to configure the ServiceMonitor AOP hooks
 * and detect any outages to provide (email/logging) feedback when necessary.</p>
 *
 * @see cn.globalph.common.vendor.service.monitor.ServiceMonitor
 * @see cn.globalph.common.vendor.service.monitor.StatusHandler
 * @see ServiceStatusDetectable
 *
 * @author Elbert Bautista (elbertbautista)
 */
public abstract class AbstractExternalPaymentGatewayCall<T,R> implements ServiceStatusDetectable<T> {

    protected Integer failureCount = 0;
    protected Boolean isUp = true;

    protected synchronized void clearStatus() {
        isUp = true;
        failureCount = 0;
    }

    protected synchronized void incrementFailure() {
        if (failureCount >= getFailureReportingThreshold()) {
            isUp = false;
        } else {
            failureCount++;
        }
    }

    @Override
    public synchronized ServiceStatusType getServiceStatus() {
        if (isUp) {
            return ServiceStatusType.UP;
        } else {
            return ServiceStatusType.DOWN;
        }
    }

    @Override
    public R process(T paymentRequest) throws PaymentException {
        R response;
        try {
            response = communicateWithVendor(paymentRequest);
        } catch (Exception e) {
            incrementFailure();
            throw new PaymentException(e);
        }
        clearStatus();

        return response;
    }

    public abstract R communicateWithVendor(T paymentRequest) throws Exception;

    public abstract Integer getFailureReportingThreshold();

}
