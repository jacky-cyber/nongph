package cn.globalph.b2c.order.domain.wrap;

import cn.globalph.b2c.offer.domain.wrap.AdjustmentWrap;
import cn.globalph.common.money.Money;
import cn.globalph.passort.domain.wrap.AddressWrap;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fulfillmentGroup")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class FulfillmentGroupWrap {

    @XmlElement
    protected Long id;

    @XmlElement
    protected Long orderId;

    @XmlElement
    protected EnumerationTypeWrap fulfillmentType;

    @XmlElement
    protected FulfillmentOptionWrap fulfillmentOption;

    @XmlElement
    protected Money total;

    @XmlElement
    protected AddressWrap address;

    @XmlElement(name = "fulfillmentGroupAdjustment")
    @XmlElementWrapper(name = "fulfillmentGroupAdjustments")
    protected List<AdjustmentWrap> fulfillmentGroupAdjustments;

    @XmlElement(name = "fulfillmentGroupItem")
    @XmlElementWrapper(name = "fulfillmentGroupItems")
    protected List<FulfillmentGroupItemWrap> fulfillmentGroupItems;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public EnumerationTypeWrap getFulfillmentType() {
		return fulfillmentType;
	}

	public void setFulfillmentType(EnumerationTypeWrap fulfillmentType) {
		this.fulfillmentType = fulfillmentType;
	}

	public FulfillmentOptionWrap getFulfillmentOption() {
		return fulfillmentOption;
	}

	public void setFulfillmentOption(FulfillmentOptionWrap fulfillmentOption) {
		this.fulfillmentOption = fulfillmentOption;
	}

	public Money getTotal() {
		return total;
	}

	public void setTotal(Money total) {
		this.total = total;
	}

	public AddressWrap getAddress() {
		return address;
	}

	public void setAddress(AddressWrap address) {
		this.address = address;
	}

	public List<AdjustmentWrap> getFulfillmentGroupAdjustments() {
		return fulfillmentGroupAdjustments;
	}

	public void setFulfillmentGroupAdjustments(
			List<AdjustmentWrap> fulfillmentGroupAdjustments) {
		this.fulfillmentGroupAdjustments = fulfillmentGroupAdjustments;
	}

	public List<FulfillmentGroupItemWrap> getFulfillmentGroupItems() {
		return fulfillmentGroupItems;
	}

	public void setFulfillmentGroupItems(
			List<FulfillmentGroupItemWrap> fulfillmentGroupItems) {
		this.fulfillmentGroupItems = fulfillmentGroupItems;
	}
}
