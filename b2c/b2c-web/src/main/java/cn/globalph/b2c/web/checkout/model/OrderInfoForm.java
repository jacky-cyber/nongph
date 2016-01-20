package cn.globalph.b2c.web.checkout.model;

import java.io.Serializable;

/**
 * A form to model checking out as guest
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class OrderInfoForm implements Serializable {

    private static final long serialVersionUID = 62974989700147353L;
    
    protected String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    
}
