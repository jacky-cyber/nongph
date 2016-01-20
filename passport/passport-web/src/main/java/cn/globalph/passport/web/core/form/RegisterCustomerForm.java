package cn.globalph.passport.web.core.form;

import cn.globalph.passport.domain.Customer;

import java.io.Serializable;

public class RegisterCustomerForm implements Serializable {
    protected static final long serialVersionUID = 1L;

    protected Customer customer;
    protected String password;
    protected String passwordConfirm;
    protected String redirectUrl;

    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPasswordConfirm() {
        return passwordConfirm;
    }
    
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
    
    public String getRedirectUrl() {
        return redirectUrl;
    }
    
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    
}
