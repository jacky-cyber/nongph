package cn.globalph.b2c.payment.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.checkout.service.CheckoutService;
import cn.globalph.b2c.checkout.service.exception.CheckoutException;
import cn.globalph.b2c.checkout.service.workflow.CheckoutResponse;
import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.NullOrderImpl;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.FulfillmentGroupService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.PaymentTransaction;
import cn.globalph.common.payment.PaymentAdditionalFieldType;
import cn.globalph.common.payment.PaymentGatewayType;
import cn.globalph.common.payment.PaymentType;
import cn.globalph.common.payment.dto.AddressDTO;
import cn.globalph.common.payment.dto.GatewayCustomerDTO;
import cn.globalph.common.payment.dto.PaymentResponseDTO;
import cn.globalph.common.payment.service.PaymentGatewayCheckoutService;
import cn.globalph.common.payment.service.PaymentGatewayConfiguration;
import cn.globalph.common.web.payment.controller.PaymentGatewayAbstractController;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.AddressService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;


/**
 * Core framework implementation of the {@link PaymentGatewayCheckoutService}.
 * 
 * @see {@link PaymentGatewayAbstractController}
 * @author felix.wu
 */
@Service("blPaymentGatewayCheckoutService")
public class DefaultPaymentGatewayCheckoutService implements PaymentGatewayCheckoutService {
    
    private static final Log LOG = LogFactory.getLog(DefaultPaymentGatewayCheckoutService.class);

    @Resource(name = "blOrderService")
    protected OrderService orderService;
    
    @Resource(name = "blOrderPaymentService")
    protected OrderPaymentService orderPaymentService;
    
    @Resource(name = "blCheckoutService")
    protected CheckoutService checkoutService;
    
    @Resource(name = "blAddressService")
    protected AddressService addressService;
    
    @Resource(name = "blFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;

    @Value("${default.payment.gateway.checkout.useGatewayBillingAddress}")
    protected boolean useBillingAddressFromGateway = true;
    
    @Override
    public Long applyPaymentToOrder(PaymentResponseDTO responseDTO, PaymentGatewayConfiguration config) {
        
        //Payments can ONLY be parsed into Order Payments if they are 'valid'
        if (!responseDTO.isValid()) {
            throw new IllegalArgumentException("Invalid payment responses cannot be parsed into the order payment domain");
        }
        
        if (config == null) {
            throw new IllegalArgumentException("Config service cannot be null");
        }
        
        Long orderId = Long.parseLong(responseDTO.getOrderId());
        Order order = orderService.getOrderById(orderId);
        
        if (!OrderStatus.IN_PROCESS.equals(order.getStatus()) && !OrderStatus.CSR_OWNED.equals(order.getStatus())) {
            throw new IllegalArgumentException("Cannot apply another payment to an Order that is not IN_PROCESS or CSR_OWNED");
        }
        
        Customer customer = order.getCustomer();
        if (customer.isAnonymous()) {
            GatewayCustomerDTO<PaymentResponseDTO> gatewayCustomer = responseDTO.getCustomer();
            if (StringUtils.isEmpty(customer.getName()) && gatewayCustomer != null) {
                customer.setName(gatewayCustomer.getName());
            }
            if (StringUtils.isEmpty(customer.getEmailAddress()) && gatewayCustomer != null) {
                customer.setEmailAddress(gatewayCustomer.getEmail());
            }
        }

        // If the gateway sends back Shipping Information, we will save that to the first shippable fulfillment group.
        populateShippingInfo(responseDTO, order);

        // If this gateway does not support multiple payments then mark all of the existing payments
        // as invalid before adding the new one
        List<OrderPayment> paymentsToInvalidate = new ArrayList<OrderPayment>();
        if (!config.handlesMultiplePayments()) {
            PaymentGatewayType gateway = config.getGatewayType();
            for (OrderPayment payment : order.getPayments()) {
                // There may be a temporary Order Payment on the Order (e.g. to save the billing address)
                // This will be marked as invalid, as the billing address that will be saved on the order will be parsed off the
                // Response DTO sent back from the Gateway as it may have Address Verification or Standardization.
                // If you do not wish to use the Billing Address coming back from the Gateway, you can override the
                // populateBillingInfo() method
                if (PaymentGatewayType.TEMPORARY.equals(payment.getGatewayType()) ||
                        (payment.getGatewayType() != null && payment.getGatewayType().equals(gateway))) {

                    paymentsToInvalidate.add(payment);
                }
            }
        }

        for (OrderPayment payment : paymentsToInvalidate) {
            order.getPayments().remove(payment);
            markPaymentAsInvalid(payment.getId());
        }

        // ALWAYS create a new order payment for the payment that comes in. Invalid payments should be cleaned up by
        // invoking {@link #markPaymentAsInvalid}.
        OrderPayment payment = orderPaymentService.create();
        payment.setType(responseDTO.getPaymentType());
        payment.setPaymentGatewayType(responseDTO.getPaymentGatewayType());
        payment.setAmount(responseDTO.getAmount());
        
        // Create the transaction for the payment
        PaymentTransaction transaction = orderPaymentService.createTransaction();
        transaction.setAmount(responseDTO.getAmount());
        transaction.setRawResponse(responseDTO.getRawResponse());
        transaction.setSuccess(responseDTO.isSuccessful());
        transaction.setType(responseDTO.getPaymentTransactionType());
        for (Entry<String, String> entry : responseDTO.getResponseMap().entrySet()) {
            transaction.getAdditionalFields().put(entry.getKey(), entry.getValue());
        }

        //Set the Credit Card Info on the Additional Fields Map
        if (PaymentType.CREDIT_CARD.equals(responseDTO.getPaymentType()) &&
                responseDTO.getCreditCard().creditCardPopulated()) {

            transaction.getAdditionalFields().put(PaymentAdditionalFieldType.NAME_ON_CARD.getType(),
                    responseDTO.getCreditCard().getCreditCardHolderName());
            transaction.getAdditionalFields().put(PaymentAdditionalFieldType.CARD_TYPE.getType(),
                    responseDTO.getCreditCard().getCreditCardType());
            transaction.getAdditionalFields().put(PaymentAdditionalFieldType.EXP_DATE.getType(),
                    responseDTO.getCreditCard().getCreditCardExpDate());
            transaction.getAdditionalFields().put(PaymentAdditionalFieldType.LAST_FOUR.getType(),
                    responseDTO.getCreditCard().getCreditCardLastFour());
        }
        
        //TODO: validate that this particular type of transaction can be added to the payment (there might already
        // be an AUTHORIZE transaction, for instance)
        //Persist the order payment as well as its transaction
        payment.setOrder(order);
        transaction.setOrderPayment(payment);
        payment.addTransaction(transaction);
        payment = orderPaymentService.save(payment);

        if (transaction.getSuccess()) {
            orderService.addPaymentToOrder(order, payment, null);
        } else {
            // We will have to mark the entire payment as invalid and boot the user to re-enter their
            // billing info and payment information as there may be an error either with the billing address/or credit card
            handleUnsuccessfulTransaction(payment);
        }
        
        return payment.getId();
    }

    protected void populateShippingInfo(PaymentResponseDTO responseDTO, Order order) {
        FulfillmentGroup shippableFulfillmentGroup = fulfillmentGroupService.getFirstShippableFulfillmentGroup(order);
        Address shippingAddress = null;
        if (responseDTO.getShipTo() != null && shippableFulfillmentGroup != null) {
            shippingAddress = addressService.create();
            AddressDTO<PaymentResponseDTO> shipToDTO = responseDTO.getShipTo();
            shippingAddress.setReceiver(shipToDTO.getAddressFirstName());
            shippingAddress.setAddress(shipToDTO.getAddressLine1());

            shippingAddress.setPostalCode(shipToDTO.getAddressPostalCode());

            if (shipToDTO.getAddressPhone() != null) {
                shippingAddress.setPhone(shipToDTO.getAddressPhone());
            }

            shippableFulfillmentGroup = fulfillmentGroupService.findFulfillmentGroupById(shippableFulfillmentGroup.getId());
            if (shippableFulfillmentGroup != null) {
                shippableFulfillmentGroup.setAddress(shippingAddress);
                fulfillmentGroupService.save(shippableFulfillmentGroup);
            }
        }
    }

    /**
     * This default implementation will mark the entire payment as invalid and boot the user to re-enter their
     * billing info and payment information as there may be an error with either the billing address or credit card.
     * This is the safest method, because depending on the implementation of the Gateway, we may not know exactly where
     * the error occurred (e.g. Address Verification enabled, etc...) So, we will assume that the error invalidates
     * the entire Order Payment, and the customer will have to re-enter their billing and credit card information to be
     * processed again.
     *
     * @param payment
     */
    protected void handleUnsuccessfulTransaction(OrderPayment payment) {
        markPaymentAsInvalid(payment.getId());
    }

    @Override
    public void markPaymentAsInvalid(Long orderPaymentId) {
        OrderPayment payment = orderPaymentService.readPaymentById(orderPaymentId);
        if (payment == null) {
            throw new IllegalArgumentException("Could not find payment with id " + orderPaymentId);
        }
        orderPaymentService.delete(payment);
    }

    @Override
    public String initiateCheckout(Long orderId) throws Exception{
        Order order = orderService.getOrderById(orderId, true);
        if (order == null || order instanceof NullOrderImpl) {
            throw new IllegalArgumentException("Could not order with id " + orderId);
        }
        
        CheckoutResponse response;

        try {
            response = checkoutService.performCheckout(order);
        } catch (CheckoutException e) {
            throw new Exception(e);
        }

        if (response.getOrder().getOrderNumber() == null) {
            LOG.error("Order Number for Order ID: " + order.getId() + " is null.");
        }

        return response.getOrder().getOrderNumber();
    }

    @Override
    public String lookupOrderNumberFromOrderId(PaymentResponseDTO responseDTO) {
        Order order = orderService.getOrderById(Long.parseLong(responseDTO.getOrderId()), true);
        if (order == null) {
            throw new IllegalArgumentException("An order with ID " + responseDTO.getOrderId() + " cannot be found for the" +
            		" given payment response.");
        }
        return order.getOrderNumber();
    }

    public boolean isUseBillingAddressFromGateway() {
        return useBillingAddressFromGateway;
    }

    public void setUseBillingAddressFromGateway(boolean useBillingAddressFromGateway) {
        this.useBillingAddressFromGateway = useBillingAddressFromGateway;
    }
}
