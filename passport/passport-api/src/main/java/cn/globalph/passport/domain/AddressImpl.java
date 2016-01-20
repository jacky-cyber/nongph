package cn.globalph.passport.domain;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.common.time.domain.TemporalTimestampListener;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@EntityListeners(value = { TemporalTimestampListener.class })
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_ADDRESS")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "AddressImpl_baseAddress")
public class AddressImpl implements Address {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "AddressId")
    @GenericGenerator(
        name="AddressId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="AddressImpl"),
            @Parameter(name="entity_name", value="cn.globalph.passport.domain.AddressImpl")
        }
    )
    @Column(name = "ADDRESS_ID")
    protected Long id;

    @Column(name = "PROVINCE", nullable = false)
    @AdminPresentation(friendlyName = "AddressImpl_Province", prominent = true, gridOrder = 3, order = 50, group = "AddressImpl_Address")
    protected String province;

    @Column(name = "CITY", nullable = false)
    @AdminPresentation(friendlyName = "AddressImpl_City", prominent = true, gridOrder = 4, order = 50, group = "AddressImpl_Address")
    protected String city;

    @Column(name = "DISTRICT", nullable = false)
    @AdminPresentation(friendlyName = "AddressImpl_District", prominent = true, gridOrder = 5, order = 50, group = "AddressImpl_Address")
    protected String district;

    @Column(name = "COMMUNITY")
    @AdminPresentation(friendlyName = "AddressImpl_Community", prominent = true, gridOrder = 6, order = 50, group = "AddressImpl_Address")
    protected String community;
        
    @Column(name = "ADDRESS", nullable = false)
    @AdminPresentation(friendlyName = "AddressImpl_Address_1", order=50, group = "AddressImpl_Address")
    protected String address;

    @Column(name = "POSTAL_CODE")
    @AdminPresentation(friendlyName = "AddressImpl_Postal_Code", order=110, group = "AddressImpl_Address")
    protected String postalCode;
    
    @Column(name = "RECEIVER")
    @AdminPresentation(friendlyName = "AddressImpl_First_Name", prominent = true, gridOrder = 1, order = 10, group = "AddressImpl_Address")
    protected String receiver;
    
    @Column(name = "PHONE")
    @AdminPresentation(friendlyName = "AddressImpl_Phone", prominent = true, gridOrder = 2, order = 10, group = "AddressImpl_Address")
    protected String phone;
    
    @Column(name = "IS_ACTIVE")
    @AdminPresentation(friendlyName = "AddressImpl_Active_Address", order=170, group = "AddressImpl_Address")
    protected boolean isActive = true;
    
    @Column(name = "IS_BUSINESS")
    @AdminPresentation(friendlyName = "AddressImpl_Business_Address", order=180, group = "AddressImpl_Address")
    protected boolean isBusiness = false;
    
    @Column(name = "IS_DEFAULT")
    @AdminPresentation(friendlyName = "AddressImpl_Default_Address", order=160, group = "AddressImpl_Address")
    protected boolean isDefault = false;

    @Column(name = "IS_FROM_RRM")
    @AdminPresentation(friendlyName = "AddressImpl_Wechat_Address", order=170, group = "AddressImpl_Address")
    protected boolean isFromRRM = false;
    
    /**
     * This is intented to be used for address verification integrations and should not be modifiable in the admin
     */
    @Column(name = "STANDARDIZED")
    @AdminPresentation(friendlyName = "AddressImpl_Standardized", order=200, group = "AddressImpl_Address", visibility=VisibilityEnum.HIDDEN_ALL)
    protected Boolean standardized = Boolean.FALSE;
    
    /**
     * This is intented to be used for address verification integrations and should not be modifiable in the admin
     */
    @Column(name = "TOKENIZED_ADDRESS")
    @AdminPresentation(friendlyName = "AddressImpl_Tokenized_Address", order=190, group = "AddressImpl_Address", visibility=VisibilityEnum.HIDDEN_ALL)
    protected String tokenizedAddress;

    /**
     * This is intented to be used for address verification integrations and should not be modifiable in the admin
     */
    @Column(name = "VERIFICATION_LEVEL")
    @AdminPresentation(friendlyName = "AddressImpl_Verification_Level", order=210, group = "AddressImpl_Address", visibility=VisibilityEnum.HIDDEN_ALL)
    protected String verificationLevel;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, 
    		    targetEntity = CustomerImpl.class, 
    		    fetch=FetchType.LAZY,
    		    optional=false )
    @JoinColumn(name = "CUSTOMER_ID")
    @AdminPresentation(excluded = true)
    protected Customer customer;
    
	@ManyToOne(targetEntity = CommunityImpl.class)
	@JoinColumn(name = "FIRST_LEVEL_COMMUNITY_ID")
    @AdminPresentation(excluded = true)
    protected Community firstLevelCommunity;

	@ManyToOne(targetEntity = CommunityImpl.class)
	@JoinColumn(name = "SECOND_LEVEL_COMMUNITY_ID")
    @AdminPresentation(excluded = true)
    protected Community secondLevelCommunity;

	@ManyToOne(targetEntity = NetNodeImpl.class)
	@JoinColumn(name = "NET_NODE_ID")
    @AdminPresentation(excluded = true)
    protected NetNode netNode;
    
    public AddressImpl(){
    }
    
    public AddressImpl(Long id){
    	this.id = id;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String getProvince() {
        return this.province;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getCity() {
        return this.city;
    }

    @Override
    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String getDistrict() {
        return this.district;
    }

    @Override
    public String getCommunity() {
        return StringUtils.isEmpty(community) ? "" : community;
    }

    @Override
    public void setCommunity(String community) {
        this.community = community;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String getTokenizedAddress() {
        return tokenizedAddress;
    }

    @Override
    public void setTokenizedAddress(String tokenizedAddress) {
        this.tokenizedAddress = tokenizedAddress;
    }

    @Override
    public Boolean getStandardized() {
        return standardized;
    }

    @Override
    public void setStandardized(Boolean standardized) {
        this.standardized = standardized;
    }
    
    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean isBusiness() {
        return isBusiness;
    }

    @Override
    public void setBusiness(boolean isBusiness) {
        this.isBusiness = isBusiness;
    }

    @Override
    public String getVerificationLevel() {
        return verificationLevel;
    }

    @Override
    public void setVerificationLevel(String verificationLevel) {
        this.verificationLevel = verificationLevel;
    }

    @Override
	public void setFirstLevelCommunity(Community firstLevelCommunity) {
		this.firstLevelCommunity = firstLevelCommunity;
	}

	@Override
	public Community getFirstLevelCommunity() {
		return this.firstLevelCommunity;
	}

	public void setSecondLevelCommunity(Community secondLevelCommunity) {
		this.secondLevelCommunity = secondLevelCommunity;
	}

	@Override
	public Community getSecondLevelCommunity() {
		return this.secondLevelCommunity;
	}

	@Override
	public void setNetNode(NetNode netNode) {
		this.netNode = netNode;
	}

	@Override
	public NetNode getNetNode() {
		return this.netNode;
	}

    @Override
    public void setFromRRM(boolean isFromRRM) {
        this.isFromRRM = isFromRRM;
    }

    @Override
    public boolean isFromRRM() {
        return this.isFromRRM;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AddressImpl other = (AddressImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        
        if (receiver == null) {
            if (other.receiver != null)
                return false;
        } else if (!receiver.equals(other.receiver))
            return false;
        if (postalCode == null) {
            if (other.postalCode != null)
                return false;
        } else if (!postalCode.equals(other.postalCode))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = ( (customer == null) ? 1 : customer.hashCode() );
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((receiver == null) ? 0 : receiver.hashCode());
        result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
        return result;
    }
}
