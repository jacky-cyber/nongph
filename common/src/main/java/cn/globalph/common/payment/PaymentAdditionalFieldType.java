package cn.globalph.common.payment;


import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class PaymentAdditionalFieldType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, PaymentAdditionalFieldType> TYPES = new LinkedHashMap<String, PaymentAdditionalFieldType>();

    public static final PaymentAdditionalFieldType NAME_ON_CARD = new PaymentAdditionalFieldType("NAME_ON_CARD", "Cardholders Name");
    public static final PaymentAdditionalFieldType CARD_TYPE = new PaymentAdditionalFieldType("CARD_TYPE", "Card Type");
    public static final PaymentAdditionalFieldType EXP_DATE = new PaymentAdditionalFieldType("EXP_DATE", "Expiration Date");
    public static final PaymentAdditionalFieldType EXP_MONTH = new PaymentAdditionalFieldType("EXP_MONTH", "Expiration Month");
    public static final PaymentAdditionalFieldType EXP_YEAR = new PaymentAdditionalFieldType("EXP_YEAR", "Expiration Year");
    
    // Generic Fields that can be used for multiple payment types
    public static final PaymentAdditionalFieldType PAYMENT_TYPE = new PaymentAdditionalFieldType("PAYMENT_TYPE", "Type of OrderPayment");
    public static final PaymentAdditionalFieldType NAME_ON_ACCOUNT = new PaymentAdditionalFieldType("NAME_ON_ACCOUNT", "Name on Account");
    public static final PaymentAdditionalFieldType ACCOUNT_TYPE = new PaymentAdditionalFieldType("ACCOUNT_TYPE", "Account Type");
    public static final PaymentAdditionalFieldType LAST_FOUR = new PaymentAdditionalFieldType("LAST_FOUR", "Last Four Digits ofAccount or CC");
    public static final PaymentAdditionalFieldType GIFT_CARD_NUM = new PaymentAdditionalFieldType("GIFT_CARD_NUM", "Gift Card Number");
    public static final PaymentAdditionalFieldType EMAIL = new PaymentAdditionalFieldType("EMAIL", "Email");
    public static final PaymentAdditionalFieldType ACCOUNT_CREDIT_NUM = new PaymentAdditionalFieldType("ACCOUNT_CREDIT_NUM", "Account Credit Number");   
    public static final PaymentAdditionalFieldType AUTH_CODE = new PaymentAdditionalFieldType("AUTH_CODE", "Authorization Code");
    public static final PaymentAdditionalFieldType REQUEST_ID = new PaymentAdditionalFieldType("REQUEST_ID", "Request Id");
    public static final PaymentAdditionalFieldType SUBSCRIPTION_ID = new PaymentAdditionalFieldType("SUBSCRIPTION_ID", "Subscription Id");
    public static final PaymentAdditionalFieldType SUBSCRIPTION_TITLE = new PaymentAdditionalFieldType("SUBSCRIPTION_TITLE", "Subscription Title");

    public static PaymentAdditionalFieldType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public PaymentAdditionalFieldType() {
        //do nothing
    }

    public PaymentAdditionalFieldType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    public String getType() {
        return type;
    }

    public String getFriendlyType() {
        return friendlyType;
    }

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PaymentAdditionalFieldType other = (PaymentAdditionalFieldType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
