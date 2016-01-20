package cn.globalph.common.payment.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chad Harchar (charchar)
 *
 * Many payment solutions allow you to set up subscriptions (recurring payments)
 * with payment transactions.
 *
 * The following DTO represent the usual parameters that you may wish to pass:
 *
 * recurringAmount: the amount that you charge for every recurring payment
 * frequency: the frequency of the recurring payment
 * numberOfInstallments: the number of payments for this subscription
 * startDate: the start date for this subscription
 *
 */
public class SubscriptionDTO<T> {

    protected T parent;

    protected Map<String, Object> additionalFields;
    protected String recurringAmount;
    protected String frequency;
    protected String numberOfInstallments;
    protected String startDate;

    public SubscriptionDTO() {
        this.additionalFields = new HashMap<String, Object>();
    }

    public SubscriptionDTO(T parent) {
        this.additionalFields = new HashMap<String, Object>();
        this.parent = parent;
    }

    public T done() {
        return parent;
    }

    public SubscriptionDTO<T> additionalFields(String key, Object value) {
        additionalFields.put(key, value);
        return this;
    }

    public SubscriptionDTO<T> recurringAmount(String recurringAmount) {
        this.recurringAmount = recurringAmount;
        return this;
    }

    public SubscriptionDTO<T> frequency(String frequency) {
        this.frequency = frequency;
        return this;
    }

    public SubscriptionDTO<T> numberOfInstallments(String numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
        return this;
    }

    public SubscriptionDTO<T> startDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public Map<String, Object> getAdditionalFields() {
        return additionalFields;
    }

    public String getRecurringAmount() {
        return recurringAmount;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public String getStartDate() {
        return startDate;
    }

    public boolean subscriptionPopulated() {
        return ((getAdditionalFields() != null && !getAdditionalFields().isEmpty()) ||
                getRecurringAmount() != null ||
                getFrequency() != null ||
                getNumberOfInstallments() != null ||
                getStartDate() != null);
    }
}
