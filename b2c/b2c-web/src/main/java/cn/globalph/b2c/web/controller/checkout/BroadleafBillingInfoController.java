package cn.globalph.b2c.web.controller.checkout;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.web.checkout.model.BillingInfoForm;
import cn.globalph.b2c.web.order.CartState;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.payment.PaymentGatewayType;
import cn.globalph.common.payment.PaymentType;
import cn.globalph.passport.domain.Address;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author felix.wu
 */
public class BroadleafBillingInfoController extends AbstractCheckoutController {

    /**
     * Processes the request to save a billing address.
     *
     * Note: this default Broadleaf implementation will create an OrderPayment of
     * type CREDIT_CARD if it doesn't exist and save the passed in billing address
     *
     * @param request
     * @param response
     * @param model
     * @param billingForm
     * @return the return path
     * @throws cn.globalph.common.exception.ServiceException
     */
    public String saveBillingAddress(HttpServletRequest request, HttpServletResponse response, Model model,
                                 BillingInfoForm billingForm, BindingResult result) throws PricingException, ServiceException {
        Order cart = CartState.getCart();

        if (billingForm.isUseShippingAddress()){
            copyShippingAddressToBillingAddress(cart, billingForm);
        }

        if (result.hasErrors()) {
            return getCheckoutView();
        }

        boolean found = false;
        for (OrderPayment p : cart.getPayments()) {
            if (PaymentType.CREDIT_CARD.equals(p.getType()) &&
                    p.isActive()) {
                found = true;
            }
        }

        if (!found) {
            // A Temporary Order Payment will be created to hold the billing address.
            // The Payment Gateway will send back any validated address and
            // the PaymentGatewayCheckoutService will persist a new payment of type CREDIT_CARD when it applies it to the Order
            OrderPayment tempOrderPayment = orderPaymentService.create();
            tempOrderPayment.setType(PaymentType.CREDIT_CARD);
            tempOrderPayment.setPaymentGatewayType(PaymentGatewayType.TEMPORARY);
            tempOrderPayment.setOrder(cart);
            cart.getPayments().add(tempOrderPayment);
        }

        orderService.save(cart, true);

        if (isAjaxRequest(request)) {
            //Add module specific model variables
            checkoutControllerExtensionManager.getHandlerProxy().addAdditionalModelVariables(model);
            return getCheckoutView();
        } else {
            return getCheckoutPageRedirect();
        }
    }

    /**
     * This method will copy the shipping address of the first fulfillment group on the order
     * to the billing address on the BillingInfoForm that is passed in.
     */
    protected void copyShippingAddressToBillingAddress(Order order, BillingInfoForm billingInfoForm) {
        if (order.getFulfillmentGroups().get(0) != null) {
            Address shipping = order.getFulfillmentGroups().get(0).getAddress();
            if (shipping != null) {
                Address billing = addressService.create();
                billing.setReceiver(shipping.getReceiver());
                billing.setAddress(shipping.getAddress());
                billing.setPostalCode(shipping.getPostalCode());
                billing.setPhone(shipping.getPhone());
                billingInfoForm.setAddress(billing);
            }
        }
    }
}
