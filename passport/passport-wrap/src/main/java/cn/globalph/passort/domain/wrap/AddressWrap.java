package cn.globalph.passort.domain.wrap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a JAXB wrapper around Address.
 */
@XmlRootElement(name = "address")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class AddressWrap{

    @XmlElement
    protected Long id;

    @XmlElement
    protected String receiver;

    @XmlElement
    protected String address;

    @XmlElement
    protected String city;

    @XmlElement
    protected String postalCode;

    @XmlElement
    protected String phone;

    @XmlElement
    protected Boolean isBusiness;

    @XmlElement
    protected Boolean isDefault;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getIsBusiness() {
		return isBusiness;
	}

	public void setIsBusiness(Boolean isBusiness) {
		this.isBusiness = isBusiness;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
}
