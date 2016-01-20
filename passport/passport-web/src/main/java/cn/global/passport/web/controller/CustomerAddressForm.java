package cn.global.passport.web.controller;

import java.io.Serializable;

public class CustomerAddressForm implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String orderNumber;

	protected Long addressId;
	// 邮政编码
	protected String postalCode;
	// 详细地址
	protected String address;
	// 收货人
	protected String receiver;
	// 手机号码
	protected String phone;
	// 省份
	protected String province;
	// 城市
	protected String city;
	// 区县
	protected String district;

	protected String community;

	protected boolean isDefault;
	
	public CustomerAddressForm() {
		// address.setPhonePrimary(new PhoneImpl());
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}	
	
	
}
