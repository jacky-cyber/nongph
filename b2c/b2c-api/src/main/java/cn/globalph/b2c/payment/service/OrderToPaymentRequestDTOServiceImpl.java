package cn.globalph.b2c.payment.service;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.FulfillmentGroupService;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.PaymentTransaction;
import cn.globalph.common.money.Money;
import cn.globalph.common.payment.PaymentType;
import cn.globalph.common.payment.dto.PaymentRequestDTO;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.Customer;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @author felix.wu
 */
@Service("blOrderToPaymentRequestDTOService")
public class OrderToPaymentRequestDTOServiceImpl implements OrderToPaymentRequestDTOService {

    public static final String ZERO_TOTAL = "0";
    
    @Resource(name = "blFulfillmentGroupService")
    protected FulfillmentGroupService fgService;

    @Override
    public PaymentRequestDTO translateOrder(Order order) {
        if (order != null) {
            PaymentRequestDTO requestDTO = new PaymentRequestDTO()
                    .orderId(order.getId().toString());

            populateCustomerInfo(order, requestDTO);
            populateShipTo(order, requestDTO);
            populateBillTo(order, requestDTO);
            populateTotals(order, requestDTO);
            populateDefaultLineItemsAndSubtotal(order, requestDTO);

            return requestDTO;
        }

        return null;
    }

    @Override
    public PaymentRequestDTO translatePaymentTransaction(Money transactionAmount, PaymentTransaction paymentTransaction) {
        //Will set the full amount to be charged on the transaction total/subtotal and not worry about shipping/tax breakdown
        PaymentRequestDTO requestDTO = new PaymentRequestDTO()
            .transactionTotal(transactionAmount.getAmount().toPlainString())
            .orderSubtotal(transactionAmount.getAmount().toPlainString())
            .shippingTotal(ZERO_TOTAL)
            .taxTotal(ZERO_TOTAL)
            .orderId(paymentTransaction.getOrderPayment().getOrder().getId().toString());
        
        //Copy Additional Fields from PaymentTransaction into the Request DTO.
        //This will contain any gateway specific information needed to perform actions on this transaction
        Map<String, String> additionalFields = paymentTransaction.getAdditionalFields();
        for (String key : additionalFields.keySet()) {
            requestDTO.additionalField(key, additionalFields.get(key));
        }

        return requestDTO;
    }

    protected void populateTotals(Order order, PaymentRequestDTO requestDTO) {
        String total = ZERO_TOTAL;
        String shippingTotal = ZERO_TOTAL;
        String taxTotal = ZERO_TOTAL;

        if (order.getTotalAfterAppliedPayments() != null) {
            total = order.getTotalAfterAppliedPayments().toString();
        }

        if (order.getTotalFulfillmentCharges() != null) {
            shippingTotal = order.getTotalFulfillmentCharges().toString();
        }

        requestDTO.transactionTotal(total)
                  .shippingTotal(shippingTotal)
                  .taxTotal(taxTotal);
     }

    protected void populateCustomerInfo(Order order, PaymentRequestDTO requestDTO) {
        Customer customer = order.getCustomer();
        String phoneNumber = customer.getPhone();
        String email = customer.getEmailAddress();

        requestDTO.customer()
                .customerId( customer.getId().toString() )
                .name( customer.getName() )
                .email( email )
                .phone( phoneNumber );
    }

    /**
     * Uses the first shippable fulfillment group to populate the {@link PaymentRequestDTO#shipTo()} object
     * @param order the {@link Order} to get data from
     * @param requestDTO the {@link PaymentRequestDTO} that should be populated
     * @see {@link FulfillmentGroupService#getFirstShippableFulfillmentGroup(Order)}
     */
    protected void populateShipTo(Order order, PaymentRequestDTO requestDTO) {
        List<FulfillmentGroup> fgs = order.getFulfillmentGroups();
        if (fgs != null && fgs.size() > 0) {
            FulfillmentGroup defaultFg = fgService.getFirstShippableFulfillmentGroup(order);
            if (defaultFg != null && defaultFg.getAddress() != null) {
                Address fgAddress = defaultFg.getAddress();
                String stateAbbr = null;
                String countryAbbr = null;
                String phone = null;

                if (fgAddress.getPhone() != null) {
                    phone = fgAddress.getPhone();
                }

                requestDTO.shipTo()
                        .addressFirstName(fgAddress.getReceiver())
                        .addressLine1(fgAddress.getAddress())
                        .addressStateRegion(stateAbbr)
                        .addressPostalCode(fgAddress.getPostalCode())
                        .addressCountryCode(countryAbbr)
                        .addressPhone(phone);
            }
        }
    }

    protected void populateBillTo(Order order, PaymentRequestDTO requestDTO) {
        for (OrderPayment payment : order.getPayments()) {
            if (payment.isActive() && PaymentType.CREDIT_CARD.equals(payment.getType())) {
                requestDTO.billTo();
            }
        }
    }

    /**
     * IMPORTANT:
     * <p>If you would like to pass Line Item information to a payment gateway
     * so that it shows up on the hosted site, you will need to override this method and
     * construct line items to conform to the requirements of that particular gateway:</p>
     *
     * <p>For Example: The Paypal Express Checkout NVP API validates that the order subtotal that you pass in,
     * add up to the amount of the line items that you pass in. So,
     * In that case you will need to take into account any additional fees, promotions,
     * credits, gift cards, etc... that are applied to the payment and add them
     * as additional line items with a negative amount when necessary.</p>
     *
     * <p>Each gateway that accepts line item information may require you to construct
     * this differently. Please consult the module documentation on how it should
     * be properly constructed.</p>
     *
     * <p>In this default implementation, just the subtotal is set, without any line item details.</p>
     *
     * @param order
     * @param requestDTO
     */
    protected void populateDefaultLineItemsAndSubtotal(Order order, PaymentRequestDTO requestDTO) {
        String subtotal = ZERO_TOTAL;
        if (order.getSubTotal() != null) {
            subtotal = order.getSubTotal().toString();
        }

        requestDTO.orderSubtotal(subtotal);
    }

}
