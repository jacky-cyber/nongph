package cn.globalph.b2c.order.domain.wrap;

import cn.globalph.common.money.Money;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fulfillmentGroupItem")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class FulfillmentGroupItemWrap{

    @XmlElement
    protected Long id;

    @XmlElement
    protected Long fulfillmentGroupId;

    @XmlElement
    protected Long orderItemId;

    @XmlElement
    protected Integer quantity;

    @XmlElement
    protected Money totalItemAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFulfillmentGroupId() {
		return fulfillmentGroupId;
	}

	public void setFulfillmentGroupId(Long fulfillmentGroupId) {
		this.fulfillmentGroupId = fulfillmentGroupId;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Money getTotalItemAmount() {
		return totalItemAmount;
	}

	public void setTotalItemAmount(Money totalItemAmount) {
		this.totalItemAmount = totalItemAmount;
	}
}
