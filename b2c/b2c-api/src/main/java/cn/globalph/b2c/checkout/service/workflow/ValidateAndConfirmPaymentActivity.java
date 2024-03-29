
package cn.globalph.b2c.checkout.service.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.checkout.service.exception.CheckoutException;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.PaymentTransaction;
import cn.globalph.b2c.payment.service.OrderPaymentService;
import cn.globalph.b2c.payment.service.OrderToPaymentRequestDTOService;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.b2c.workflow.state.ActivityStateManagerImpl;
import cn.globalph.common.money.Money;
import cn.globalph.common.payment.PaymentTransactionType;
import cn.globalph.common.payment.dto.PaymentResponseDTO;
import cn.globalph.common.payment.service.PaymentGatewayConfigurationService;
import cn.globalph.common.payment.service.PaymentGatewayConfigurationServiceProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


/**
 * <p>Verifies that there is enough payment on the order via the <i>successful</i> amount on {@link PaymentTransactionType.AUTHORIZE} and
 * {@link PaymentTransactionType.AUTHORIZE_AND_CAPTURE} transactions. This will also confirm any {@link PaymentTransactionType.UNCONFIRMED} transactions
 * that exist on am {@link OrderPayment}.</p>
 * 
 * <p>If there is an exception (either in this activity or later downstream) the confirmed payments are rolled back via {@link ConfirmPaymentsRollbackHandler}
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
public class ValidateAndConfirmPaymentActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {
    
    protected static final Log LOG = LogFactory.getLog(ValidateAndConfirmPaymentActivity.class);
    
    /**
     * Used by the {@link ConfirmPaymentsRollbackHandler} to roll back transactions that this activity confirms.
     */
    public static final String CONFIRMED_TRANSACTIONS = "confirmedTransactions";

    @Autowired(required = false)
    @Qualifier("blPaymentGatewayConfigurationServiceProvider")
    protected PaymentGatewayConfigurationServiceProvider paymentConfigurationServiceProvider;
    
    @Resource(name = "blOrderToPaymentRequestDTOService")
    protected OrderToPaymentRequestDTOService orderToPaymentRequestService;

    @Resource(name = "blOrderPaymentService")
    protected OrderPaymentService orderPaymentService;

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        
        Map<String, Object> rollbackState = new HashMap<String, Object>(); 
        
        // There are definitely enough payments on the order. We now need to confirm each unconfirmed payment on the order.
        // Unconfirmed payments could be added for things like gift cards and account credits; they are not actually
        // decremented from the user's account until checkout. This could also be used in some credit card processing
        // situations
        // Important: The payment.getAmount() must be the final amount that is going to be confirmed. If the order total
        // changed, the order payments need to be adjusted to reflect this and must add up to the order total.
        // This can happen in the case of PayPal Express or other hosted gateways where the unconfirmed payment comes back
        // to a review page, the customer selects shipping and the order total is adjusted.
        Map<OrderPayment, PaymentTransaction> additionalTransactions = new HashMap<OrderPayment, PaymentTransaction>();
        // Used for the rollback handler; we want to make sure that we roll back transactions that have already been confirmed
        // as well as transctions that we are about to confirm here
        List<PaymentTransaction> confirmedTransactions = new ArrayList<PaymentTransaction>();
        for (OrderPayment payment : order.getPayments()) {
            if (payment.isActive()) {
                for (PaymentTransaction tx : payment.getTransactions()) {
                    if (PaymentTransactionType.UNCONFIRMED.equals(tx.getType())) {
                        if (LOG.isTraceEnabled()) {
                            LOG.trace("Transaction " + tx.getId() + " is not confirmed. Proceeding to confirm transaction.");
                        }

                        // Cannot confirm anything here if there is no provider
                        if (paymentConfigurationServiceProvider == null) {
                            String msg = "There are unconfirmed payment transactions on this payment but no payment gateway" +
                                    " configuration or transaction confirmation service configured";
                            LOG.error(msg);
                            throw new CheckoutException(msg, context.getSeedData());
                        }

                        PaymentGatewayConfigurationService cfg = paymentConfigurationServiceProvider.getGatewayConfigurationService(tx.getOrderPayment().getGatewayType());
                        PaymentResponseDTO responseDTO = cfg.getTransactionConfirmationService()
                                .confirmTransaction(orderToPaymentRequestService.translatePaymentTransaction(payment.getAmount(), tx));

                        if (LOG.isTraceEnabled()) {
                            LOG.trace("Transaction Confirmation Raw Response: " +  responseDTO.getRawResponse());
                        }

                        if (responseDTO.isSuccessful()) {
                            PaymentTransaction transaction = orderPaymentService.createTransaction();
                            transaction.setAmount(responseDTO.getAmount());
                            transaction.setRawResponse(responseDTO.getRawResponse());
                            transaction.setSuccess(responseDTO.isSuccessful());
                            transaction.setType(responseDTO.getPaymentTransactionType());
                            transaction.setParentTransaction(tx);
                            transaction.setOrderPayment(payment);
                            transaction.setAdditionalFields(responseDTO.getResponseMap());
                            confirmedTransactions.add(transaction);
                            additionalTransactions.put(payment, transaction);
                        } else {
                            // Since there was a problems processing the
                            String msg = "Transaction confirmation attempt with id: " + tx.getId() + " was unsuccessful";
                            LOG.error(msg);
                            throw new CheckoutException(msg, context.getSeedData());
                        }
                    } else if (PaymentTransactionType.AUTHORIZE.equals(tx.getType()) ||
                            PaymentTransactionType.AUTHORIZE_AND_CAPTURE.equals(tx.getType())) {
                        // After each transaction is confirmed, associate the new list of confirmed transactions to the rollback state. This has the added
                        // advantage of being able to invoke the rollback handler if there is an exception thrown at some point while confirming multiple
                        // transactions. This is outside of the transaction confirmation block in order to capture transactions
                        // that were already confirmed prior to this activity running
                        confirmedTransactions.add(tx);
                    }
                }
            }
        }
        
        // Once all transactions have been confirmed, add the final
        rollbackState.put(CONFIRMED_TRANSACTIONS, confirmedTransactions);
        ActivityStateManagerImpl.getStateManager().registerState(this, context, getRollbackHandler(), rollbackState);

        // Add the new transactions to this payment
        for (OrderPayment payment : order.getPayments()) {
            if (additionalTransactions.containsKey(payment)) {
                payment.addTransaction(additionalTransactions.get(payment));
            }
        }
        
        // Add authorize and authorize_and_capture; there should only be one or the other in the payment
        Money paymentSum = new Money(BigDecimal.ZERO);
        for (OrderPayment payment : order.getPayments()) {
            if (payment.isActive()) {
                paymentSum = paymentSum.add(payment.getSuccessfulTransactionAmountForType(PaymentTransactionType.AUTHORIZE))
                               .add(payment.getSuccessfulTransactionAmountForType(PaymentTransactionType.AUTHORIZE_AND_CAPTURE));
            }
        }
        
        if (paymentSum.lessThan(order.getTotal())) {
            throw new IllegalArgumentException("There are not enough payments to pay for the total order. The sum of " + 
                    "the payments is " + paymentSum.getAmount().toPlainString() + " and the order total is " + order.getTotal().getAmount().toPlainString());
        }
        
        // There should also likely be something that says whether the payment was successful or not and this should check
        // that as well. Currently there isn't really a concept for that
        return context;
    }

}
