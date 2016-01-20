package cn.globalph.b2c.payment.domain;

import cn.globalph.common.money.Money;
import cn.globalph.passport.domain.Customer;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface PaymentResponseItem extends Serializable {

    public String getAuthorizationCode();

    public void setAuthorizationCode(String authorizationCode);

    public String getMiddlewareResponseCode();

    public void setMiddlewareResponseCode(String middlewareResponseCode);

    public String getMiddlewareResponseText();

    public void setMiddlewareResponseText(String middlewareResponseText);

    public String getProcessorResponseCode();

    public void setProcessorResponseCode(String processorResponseCode);

    public String getProcessorResponseText();

    public void setProcessorResponseText(String processorResponseText);

    /**
     * The amount that the system processed. For example, when submitting an order, this would be the order.getTotal.
     * If refunding $10, this would be 10.
     *
     * @return
     */
    public Money getTransactionAmount();

    /**
     * Sets the transaction amount.
     *
     * @param amount
     */
    public void setTransactionAmount(Money amount);

    public Boolean getTransactionSuccess();

    public void setTransactionSuccess(Boolean transactionSuccess);

    public Date getTransactionTimestamp();

    public void setTransactionTimestamp(Date transactionTimestamp);

    public String getImplementorResponseCode();

    public void setImplementorResponseCode(String implementorResponseCode);

    public String getImplementorResponseText();

    public void setImplementorResponseText(String implementorResponseText);

    public String getTransactionId();

    public void setTransactionId(String transactionId);

    public String getAvsCode();

    public void setAvsCode(String avsCode);

    // TODO: Rename to getRemainingTransactionAmount
    public Money getRemainingBalance();

    public void setRemainingBalance(Money remainingBalance);

    public Map<String, String> getAdditionalFields();

    public void setAdditionalFields(Map<String, String> additionalFields);

    public String getUserName();

    public void setUserName(String userName);

    public Customer getCustomer();

    public void setCustomer(Customer customer);

    public void setPaymentTransaction(PaymentTransaction paymentTransaction);

    public PaymentTransaction getPaymentTransaction();

}
