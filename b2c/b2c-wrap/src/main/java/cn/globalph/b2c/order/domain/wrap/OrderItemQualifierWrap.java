package cn.globalph.b2c.order.domain.wrap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderItemQualifier")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class OrderItemQualifierWrap{

    @XmlElement
    protected Long offerId;

    @XmlElement
    protected Long quantity;

	public Long getOfferId() {
		return offerId;
	}

	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
    
}
