package cn.globalph.b2c.web.checkout.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author felix.wu
 */
public class CustomerCreditInfoForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> accountNumbers;

    public List<String> getAccountNumbers() {
        return accountNumbers;
    }

    public void setAccountNumbers(List<String> accountNumbers) {
        this.accountNumbers = accountNumbers;
    }
}
