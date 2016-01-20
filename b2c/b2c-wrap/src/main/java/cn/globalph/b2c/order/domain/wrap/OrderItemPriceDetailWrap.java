package cn.globalph.b2c.order.domain.wrap;

import cn.globalph.b2c.offer.domain.wrap.AdjustmentWrap;
import cn.globalph.common.money.Money;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderItemAttribute")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class OrderItemPriceDetailWrap{
    
    @XmlElement
    protected Long id;
    
    @XmlElement
    protected Money totalAdjustmentValue;

    @XmlElement
    protected Money totalAdjustedPrice;

    @XmlElement
    protected Integer quantity;
    
    @XmlElement(name = "adjustment")
    @XmlElementWrapper(name = "adjustments")
    protected List<AdjustmentWrap> orderItemPriceDetailAdjustments = new LinkedList<AdjustmentWrap>();
	
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Money getTotalAdjustmentValue() {
		return totalAdjustmentValue;
	}
	public void setTotalAdjustmentValue(Money totalAdjustmentValue) {
		this.totalAdjustmentValue = totalAdjustmentValue;
	}
	public Money getTotalAdjustedPrice() {
		return totalAdjustedPrice;
	}
	public void setTotalAdjustedPrice(Money totalAdjustedPrice) {
		this.totalAdjustedPrice = totalAdjustedPrice;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public List<AdjustmentWrap> getOrderItemPriceDetailAdjustments() {
		return orderItemPriceDetailAdjustments;
	}
	public void setOrderItemPriceDetailAdjustments(
			List<AdjustmentWrap> orderItemPriceDetailAdjustments) {
		this.orderItemPriceDetailAdjustments = orderItemPriceDetailAdjustments;
	}
    
}
