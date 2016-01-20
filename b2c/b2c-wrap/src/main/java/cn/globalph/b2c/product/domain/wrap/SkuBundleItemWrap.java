package cn.globalph.b2c.product.domain.wrap;

import cn.globalph.common.money.Money;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "skuBundleItem")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SkuBundleItemWrap{
    
    @XmlElement
    protected Long id;
    
    @XmlElement
    protected Integer quantity;
    
    @XmlElement
    protected Money salePrice;
    
    @XmlElement
    protected Money retailPrice;
    
    @XmlElement
    protected Long bundleId;
    
    @XmlElement
    protected SkuWrap sku;
    
    @XmlElement
    protected String name;

    @XmlElement
    protected Boolean active;

    @XmlElement
    protected String description;

    @XmlElement
    protected String longDescription;
    @XmlElement
    private Long productId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Money getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Money salePrice) {
		this.salePrice = salePrice;
	}
	public Money getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Money retailPrice) {
		this.retailPrice = retailPrice;
	}
	public Long getBundleId() {
		return bundleId;
	}
	public void setBundleId(Long bundleId) {
		this.bundleId = bundleId;
	}
	public SkuWrap getSku() {
		return sku;
	}
	public void setSku(SkuWrap sku) {
		this.sku = sku;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
}
