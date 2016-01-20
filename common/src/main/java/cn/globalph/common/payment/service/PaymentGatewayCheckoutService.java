
package cn.globalph.common.payment.service;

import cn.globalph.common.payment.dto.PaymentResponseDTO;
import cn.globalph.common.web.payment.controller.PaymentGatewayAbstractController;

/**
 * <p>The default implementation of this interface is represented in the core Broadleaf framework at
 * {@link cn.globalph.b2c.payment.service.BroadleafPaymentGatewayCheckoutService}. This is designed as
 * a generic contract for allowing payment modules to add payments to an order represented in Broadleaf while still
 * staying decoupled from any of the Broadleaf core framework concepts.</p>
 * 
 * <p>These service methods are usually invoked from the controller that listens to the endpoint hit by the external payment
 * provider (which should be a subclass of {@link PaymentGatewayAbstractController}).</p>
 * 
 * @see {@link PaymentGatewayAbstractController}
 *
 * @author Elbert Bautista (elbertbautista)
 * @author Phillip Verheyden (phillipuniverse)
 */
public interface PaymentGatewayCheckoutService {

    /**
     * @param responseDTO the response that came back from the gateway
     * @param configService configuration values for the payment gateway
     * @return a unique ID of the payment as it is saved in the Broadleaf domain. This ID can be referred to to retrieve
     * the payment on the Broadleaf side for other methods like {@link #markPaymentAsInvalid(Long)}
     * @throws IllegalArgumentException if the {@link PaymentResponseDTO#getValid()} returns false or if the order that
     * the {@link PaymentResponseDTO} is attempted to be applied to has already gone through checkout
     */
    public Long applyPaymentToOrder(PaymentResponseDTO responseDTO, PaymentGatewayConfiguration config)
        throws IllegalArgumentException;

    /**
     * Marks a given order payment as invalid. In the default implementation, this archives the payment. This can be
     * determined from the result of {@link #applyPaymentToOrder(PaymentResponseDTO, PaymentGatewayConfiguration)}
     * @param orderPaymentId the payment ID to mark as invalid
     */
    public void markPaymentAsInvalid(Long orderPaymentId);

    /**
     * Initiates the checkout process for a given <b>orderId</b>. This is usually from {@link PaymentResponseDTO#getOrderId()}
     * @param orderId the order to check out
     * @return the response from checking out the order
     */
    public String initiateCheckout(Long orderId) throws Exception;

    /**
     * Looks up the order number for a particular order id from the {@link PaymentResponseDTO}. This can be used to redirect
     * the user coming from the payment gateway to the order confirmation page.
     * 
     * @param responseDTO the response from the gateway
     * @return The order number for order id. This method can return null if the order number has not already been set
     * (which usually means that the order has not already been checked out)
     * @throws IllegalArgumentException if the order cannot be found from the {@link PaymentResponseDTO}
     */
    public String lookupOrderNumberFromOrderId(PaymentResponseDTO responseDTO) throws IllegalArgumentException;

}
