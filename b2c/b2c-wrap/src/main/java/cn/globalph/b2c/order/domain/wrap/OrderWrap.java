package cn.globalph.b2c.order.domain.wrap;

import cn.globalph.b2c.offer.domain.wrap.AdjustmentWrap;
import cn.globalph.common.money.Money;
import cn.globalph.passort.domain.wrap.CustomerWrap;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class OrderWrap{

    @XmlElement
    protected Long id;

    @XmlElement
    protected String status;

    @XmlElement
    protected Money totalShipping;

    @XmlElement
    protected Money subTotal;

    @XmlElement
    protected Money total;

    @XmlElement
    protected CustomerWrap customer;

    @XmlElement(name = "orderItem")
    @XmlElementWrapper(name = "orderItems")
    protected List<OrderItemWrap> orderItems;

    @XmlElement(name = "fulfillmentGroup")
    @XmlElementWrapper(name = "fulfillmentGroups")
    protected List<FulfillmentGroupWrap> fulfillmentGroups;

    @XmlElement(name = "payment")
    @XmlElementWrapper(name = "payments")
    protected List<OrderPaymentWrap> payments;

    @XmlElement(name = "orderAdjustment")
    @XmlElementWrapper(name = "orderAdjustments")
    protected List<AdjustmentWrap> orderAdjustments;

    @XmlElement(name = "cartMessages")
    @XmlElementWrapper(name = "cartMessages")
    protected List<CartMessageWrap> cartMessages;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Money getTotalShipping() {
		return totalShipping;
	}

	public void setTotalShipping(Money totalShipping) {
		this.totalShipping = totalShipping;
	}

	public Money getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Money subTotal) {
		this.subTotal = subTotal;
	}

	public Money getTotal() {
		return total;
	}

	public void setTotal(Money total) {
		this.total = total;
	}

	public CustomerWrap getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerWrap customer) {
		this.customer = customer;
	}

	public List<OrderItemWrap> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemWrap> orderItems) {
		this.orderItems = orderItems;
	}

	public List<FulfillmentGroupWrap> getFulfillmentGroups() {
		return fulfillmentGroups;
	}

	public void setFulfillmentGroups(List<FulfillmentGroupWrap> fulfillmentGroups) {
		this.fulfillmentGroups = fulfillmentGroups;
	}

	public List<OrderPaymentWrap> getPayments() {
		return payments;
	}

	public void setPayments(List<OrderPaymentWrap> payments) {
		this.payments = payments;
	}

	public List<AdjustmentWrap> getOrderAdjustments() {
		return orderAdjustments;
	}

	public void setOrderAdjustments(List<AdjustmentWrap> orderAdjustments) {
		this.orderAdjustments = orderAdjustments;
	}

	public List<CartMessageWrap> getCartMessages() {
		return cartMessages;
	}

	public void setCartMessages(List<CartMessageWrap> cartMessages) {
		this.cartMessages = cartMessages;
	}
}
