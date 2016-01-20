package cn.globalph.b2c.order.domain.wrap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "offer")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class OfferWrap{

    @XmlElement
    protected Long offerId;
    @XmlElement
    protected String startDate;
    @XmlElement
    protected String endDate;
    @XmlElement
    protected String marketingMessage;
    @XmlElement
    protected String description;
    @XmlElement
    protected String name;
    @XmlElement
    protected Boolean automatic;
    @XmlElement
    protected EnumerationTypeWrap offerType;

    @XmlElement
    protected EnumerationTypeWrap discountType;
    @XmlElement
    protected int maxUses;
	public Long getOfferId() {
		return offerId;
	}
	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getMarketingMessage() {
		return marketingMessage;
	}
	public void setMarketingMessage(String marketingMessage) {
		this.marketingMessage = marketingMessage;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getAutomatic() {
		return automatic;
	}
	public void setAutomatic(Boolean automatic) {
		this.automatic = automatic;
	}
	public EnumerationTypeWrap getOfferType() {
		return offerType;
	}
	public void setOfferType(EnumerationTypeWrap offerType) {
		this.offerType = offerType;
	}
	public EnumerationTypeWrap getDiscountType() {
		return discountType;
	}
	public void setDiscountType(EnumerationTypeWrap discountType) {
		this.discountType = discountType;
	}
	public int getMaxUses() {
		return maxUses;
	}
	public void setMaxUses(int maxUses) {
		this.maxUses = maxUses;
	}
}
