package cn.globalph.passport.domain;

import cn.globalph.common.audit.Auditable;

import java.io.Serializable;
import java.util.List;

public interface Customer extends Serializable {

    public Long getId();

    public void setId(Long id);

    Auditable getAuditable();

    void setAuditable(Auditable auditable);

    public String getLoginName();

    public void setLoginName(String loginName);

    public String getPassword();

    public void setPassword(String password);

    public boolean isPasswordChangeRequired();

    public void setPasswordChangeRequired(boolean passwordChangeRequired);

    public String getName();

    public void setName(String name);

    public String getEmailAddress();

    public void setEmailAddress(String emailAddress);

    public ChallengeQuestion getChallengeQuestion();

    public void setChallengeQuestion(ChallengeQuestion challengeQuestion);

    public String getChallengeAnswer();

    public void setChallengeAnswer(String challengeAnswer);

    public String getUnencodedPassword();

    public void setUnencodedPassword(String unencodedPassword);

    public boolean isReceiveEmail();

    public void setReceiveEmail(boolean receiveEmail);
    
    public boolean isRegistered();

    public void setRegistered(boolean registered);

    public String getUnencodedChallengeAnswer();

    public void setUnencodedChallengeAnswer(String unencodedChallengeAnswer);
    
    public void setCookied(boolean cookied);
    
    public boolean isCookied();

    public void setLoggedIn(boolean loggedIn);
    
    public boolean isLoggedIn();

    public void setAnonymous(boolean anonymous);
    
    public boolean isAnonymous(); 
    
    public boolean isPersisted();
    
    /**
     * Returns true if this user has been deactivated.
     * Most implementations will not allow the user to login if they are deactivated.
     * 
     * @return
     */
    public boolean isDeactivated();
    
    /**
     * Sets the users deactivated status.
     * 
     * @param deactivated
     */
    public void setDeactivated(boolean deactivated);

    public List<Address> getConsigneeAddresses();

    public void setConsigneeAddresses(List<Address> addresses);

    public String getPhone();

    public void setPhone(String phone);
    
    public String getValidationCode();
    
    public void setValidationCode(String validationCode);
    
    public String getEmailToken();
    
    public void setEmailToken(String emailToken);
    
    public Integer getValidationStatus();
    
    public void setValidationStatus(Integer validationStatus);

//    public List<CustomerPayment> getCustomerPayments();

//    public void setCustomerPayments(List<CustomerPayment> customerPayments);
    
    public void setBonusPoint(Integer bonusPoint);
    
    public Integer getBonusPoint();
    
    public void setUsedBonusPoint(Integer bonusPoint);
    
    public Integer getUsedBonusPoint();
    
    public String getMaskName();
    
    public List<CustomerMessage> getCustomerMessages();
    
    public void setCustomerMessages(List<CustomerMessage> customerMessages);
}
