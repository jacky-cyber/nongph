package cn.globalph.b2c.pricing.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.PaymentTransaction;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.common.money.Money;
import cn.globalph.common.payment.PaymentTransactionType;
import cn.globalph.common.payment.PaymentType;

/**
 * The AdjustOrderPaymentsActivity is responsible for adjusting any of the order payments
 * that have already been applied to the order. This happens when order payments have
 * been applied to the order but the order total has changed. In the case of a hosted
 * gateway solution like PayPal Express Checkout, the order payment is created when the
 * customer redirects to the Review Order Page (Checkout page) and the user selects
 * a shipping method which may affect the order total. Since the Hosted Order payment
 * is unconfirmed, we need to adjust the amount on this order payment before
 * we complete checkout and confirm the payment with PayPal again.
 *
 * For this default implementation,
 * This algorithm will add up all the active applied payments to the order that are not of type
 * 'UNCONFIRMED' and payment type 'THIRD_PARTY_ACCOUNT'
 * The order.getTotal() minus all the applied payments that are NOT Unconfirmed and of a Third Party account
 * will then be set as the new amount that should be processed by the Third Party Account.
 *
 * Example:
 * 1) Initial Checkout Step
 * Order - Total = $30
 * - Order Payment (PayPal Express Checkout) - [Unconfirmed] $10
 * - Gift Card - [Unconfirmed] $10
 * - Customer Credit - [Unconfirmed] $10
 *
 * 2) Shipping Method picked and changes the order total
 * Order - Total = $35
 * - Order Payment (PayPal Express Checkout) - [Unconfirmed] $10
 * - Gift Card - [Unconfirmed] $10
 * - Customer Credit - [Unconfirmed] $10
 *
 * 3) Adjust Order Payment Activity ($35 - ($10 + $10)) = $15
 * Order - Total = $35
 * - Order Payment (PayPal Express Checkout) - [Unconfirmed] $15
 * - Gift Card - [Unconfirmed] $10
 * - Customer Credit - [Unconfirmed] $10
 *
 * @author felix.wu
 */
public class AdjustOrderPaymentsActivity extends BaseActivity<ProcessContext<Order>> {

    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();

        OrderPayment unconfirmedThirdParty = null;
        Money appliedPaymentsWithoutThirdParty = Money.ZERO;
        for (OrderPayment payment : order.getPayments()) {
            if (payment.isActive()) {
                PaymentTransaction initialTransaction = payment.getInitialTransaction();

                if (initialTransaction != null &&
                        PaymentTransactionType.UNCONFIRMED.equals(initialTransaction.getType()) &&
                        PaymentType.THIRD_PARTY_ACCOUNT.equals(payment.getType()))  {
                    unconfirmedThirdParty = payment;
                } else if (payment.getAmount() != null) {
                    appliedPaymentsWithoutThirdParty = appliedPaymentsWithoutThirdParty.add(payment.getAmount());
                }
            }

        }

        if (unconfirmedThirdParty != null) {
            Money difference = order.getTotal().subtract(appliedPaymentsWithoutThirdParty);
            unconfirmedThirdParty.setAmount(difference);
        }

        context.setSeedData(order);
        return context;
    }

}
