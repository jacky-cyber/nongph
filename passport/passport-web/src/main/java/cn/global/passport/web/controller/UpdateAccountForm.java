package cn.global.passport.web.controller;

import java.io.Serializable;

public class UpdateAccountForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String emailAddress;
    private String name;
    private String phone;
    private Integer validationStatus;
    private Long customerId;
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(Integer validationStatus) {
		this.validationStatus = validationStatus;
	}
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}	
}
