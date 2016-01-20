package cn.globalph.passport.domain;

import java.io.Serializable;

/**
 * @author felix.wu
 */
public interface Address extends Serializable {

    public void setId(Long id);

    public Long getId();

    void setProvince(String province);

    String getProvince();

    void setCity(String city);

    String getCity();

    void setDistrict(String district);

    String getDistrict();
    
    public void setAddress(String address);

    String getCommunity();

    void setCommunity(String community);

    public String getAddress();

    public void setPostalCode(String postalCode);
    public String getPostalCode();

    public String getReceiver();
    public void setReceiver(String receiverName);

    public String getPhone();
    public void setPhone(String phone);
    
    public boolean isActive();
    public void setActive(boolean isActive);
    
    public boolean isBusiness();
    public void setBusiness(boolean isBusiness);
    
    public boolean isDefault();
    public void setDefault(boolean isDefault);
    
    public Boolean getStandardized();
    public void setStandardized(Boolean standardized);
    
    public String getTokenizedAddress();
    public void setTokenizedAddress(String tAddress);

    public String getVerificationLevel();
    public void setVerificationLevel(String verificationLevel);
  
    public Customer getCustomer();
    public void setCustomer(Customer customer);
    
    public void setFirstLevelCommunity(Community firstLevelCommunity);
	public Community getFirstLevelCommunity();

	public void setSecondLevelCommunity(Community secondLevelCommunity);
	public Community getSecondLevelCommunity();

	public void setNetNode(NetNode netNode);
	public NetNode getNetNode();

    void setFromRRM(boolean isFromRRM);

    boolean isFromRRM();
}
