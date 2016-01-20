package cn.globalph.b2c.offer.domain.wrap;

import cn.globalph.common.money.Money;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "adjustment")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class AdjustmentWrap {

    @XmlElement
    protected Long id;

    @XmlElement
    protected Long offerid;
    
    @XmlElement
    protected String reason;    

    @XmlElement
    protected String marketingMessage;

    @XmlElement
    protected Money adjustmentValue;

    @XmlElement
    protected String discountType;

    @XmlElement
    protected BigDecimal discountAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOfferid() {
		return offerid;
	}

	public void setOfferid(Long offerid) {
		this.offerid = offerid;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMarketingMessage() {
		return marketingMessage;
	}

	public void setMarketingMessage(String marketingMessage) {
		this.marketingMessage = marketingMessage;
	}

	public Money getAdjustmentValue() {
		return adjustmentValue;
	}

	public void setAdjustmentValue(Money adjustmentValue) {
		this.adjustmentValue = adjustmentValue;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
}
