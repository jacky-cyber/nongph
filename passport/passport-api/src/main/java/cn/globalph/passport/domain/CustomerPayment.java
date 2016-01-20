package cn.globalph.passport.domain;

import java.io.Serializable;
import java.util.Map;

public interface CustomerPayment extends Serializable {

    public void setId(Long id);

    public Long getId();

    public Customer getCustomer();

    public void setCustomer(Customer customer);

    public String getPaymentToken();

    public void setPaymentToken(String paymentToken);

    public boolean isDefault();

    public void setDefault(boolean isDefault);

    public Map<String, String> getAdditionalFields();

    public void setAdditionalFields(Map<String, String> additionalFields);

}
