package cn.globalph.passport.domain;

import java.io.Serializable;

public interface NetNode extends Serializable{

	public Long getId();
	public void setId(Long id);
	
	public String getNetNodeName();
	public void setNetNodeName(String netNodeName);
	
	public String getAddress();
	public void setAddress(String address);
	
	public String getContactWith();
	public void setCotactWith(String contactWith);
	
	public String getPhoneNumber();
	public void setPhoneNumber(String phoneNumber);
	
	public Community getCommunity();
	public void setCommunity(Community community);
	
}
