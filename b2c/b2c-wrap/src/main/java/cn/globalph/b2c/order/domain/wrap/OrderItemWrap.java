package cn.globalph.b2c.order.domain.wrap;

import cn.globalph.common.money.Money;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderItem")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class OrderItemWrap{

    @XmlElement
    protected Long id;

    @XmlElement
    protected String name;

    @XmlElement
    protected Integer quantity;

    @XmlElement
    protected Money retailPrice;

    @XmlElement
    protected Money salePrice;

    @XmlElement
    protected Long orderId;

    @XmlElement
    protected Long skuId;

    @XmlElement
    protected Long productId;
    @XmlElement
    protected Boolean isBundle = Boolean.FALSE;
    
    @XmlElement(name = "orderItemPriceDetails")
    @XmlElementWrapper(name = "orderItemPriceDetails")
    protected List<OrderItemPriceDetailWrap> orderItemPriceDetails;

    //This will only be poulated if this is a BundleOrderItem
    @XmlElement(name = "bundleItem")
    @XmlElementWrapper(name = "bundleItems")
    protected List<OrderItemWrap> bundleItems;
    //

    @XmlElementWrapper(name = "qualifiers")
    @XmlElement(name = "qualifier")
    protected List<OrderItemQualifierWrap> qualifiers;

    @XmlElement
    protected Boolean isDiscountingAllowed;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;

    }

    public void setProductId(Long productId) {
        this.productId = productId;

    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Money getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(Money retailPrice) {
		this.retailPrice = retailPrice;
	}

	public Money getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Money salePrice) {
		this.salePrice = salePrice;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Boolean getIsBundle() {
		return isBundle;
	}

	public void setIsBundle(Boolean isBundle) {
		this.isBundle = isBundle;
	}

	public List<OrderItemPriceDetailWrap> getOrderItemPriceDetails() {
		return orderItemPriceDetails;
	}

	public void setOrderItemPriceDetails(
			List<OrderItemPriceDetailWrap> orderItemPriceDetails) {
		this.orderItemPriceDetails = orderItemPriceDetails;
	}

	public List<OrderItemWrap> getBundleItems() {
		return bundleItems;
	}

	public void setBundleItems(List<OrderItemWrap> bundleItems) {
		this.bundleItems = bundleItems;
	}

	public List<OrderItemQualifierWrap> getQualifiers() {
		return qualifiers;
	}

	public void setQualifiers(List<OrderItemQualifierWrap> qualifiers) {
		this.qualifiers = qualifiers;
	}

	public Boolean getIsDiscountingAllowed() {
		return isDiscountingAllowed;
	}

	public void setIsDiscountingAllowed(Boolean isDiscountingAllowed) {
		this.isDiscountingAllowed = isDiscountingAllowed;
	}

	public Long getProductId() {
		return productId;
	}
}
