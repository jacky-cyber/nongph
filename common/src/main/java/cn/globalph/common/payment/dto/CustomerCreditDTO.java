package cn.globalph.common.payment.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public class CustomerCreditDTO<T> {

    protected T parent;

    protected Map<String, Object> additionalFields;
    protected String customerCreditAccountNum;
    protected String customerCreditAccountMasked;

    public CustomerCreditDTO() {
        this.additionalFields = new HashMap<String, Object>();
    }

    public CustomerCreditDTO(T parent) {
        this.additionalFields = new HashMap<String, Object>();
        this.parent = parent;
    }

    public T done() {
        return parent;
    }

    public CustomerCreditDTO<T> additionalFields(String key, Object value) {
        additionalFields.put(key, value);
        return this;
    }

    public CustomerCreditDTO<T> customerCreditAccountNum(String customerCreditAccountNum) {
        this.customerCreditAccountNum = customerCreditAccountNum;
        return this;
    }

    public CustomerCreditDTO<T> customerCreditAccountMasked(String customerCreditAccountMasked) {
        this.customerCreditAccountMasked = customerCreditAccountMasked;
        return this;
    }
}
