package cn.globalph.b2c.product.domain.wrap;

import cn.globalph.common.money.Money;
import cn.globalph.common.util.xml.ISO8601DateAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "sku")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SkuWrap{

    @XmlElement
    protected Long id;

    @XmlElement
    @XmlJavaTypeAdapter(ISO8601DateAdapter.class)
    protected Date activeStartDate;

    @XmlElement
    @XmlJavaTypeAdapter(ISO8601DateAdapter.class)
    protected Date activeEndDate;

    @XmlElement
    protected String name;

    @XmlElement
    protected Boolean active;
    
    
    @XmlElement
    protected String inventoryType;

    @XmlElement
    protected String description;

    @XmlElement
    protected String longDescription;
    
    @XmlElement
    protected String primaryMediaUrl;
    
    @XmlElement
    protected boolean onSale;
    
    @XmlElement
    protected Money retailPrice;
    
    @XmlElement
    protected Money salePrice;
    
    @XmlElement
    protected WeightWrap weight;

    @XmlElement
    protected DimensionWrap dimension;
    
    @XmlElement(name = "product")
    protected ProductWrap product;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getActiveStartDate() {
		return activeStartDate;
	}

	public void setActiveStartDate(Date activeStartDate) {
		this.activeStartDate = activeStartDate;
	}

	public Date getActiveEndDate() {
		return activeEndDate;
	}

	public void setActiveEndDate(Date activeEndDate) {
		this.activeEndDate = activeEndDate;
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

	public String getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(String inventoryType) {
		this.inventoryType = inventoryType;
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

	public String getPrimaryMediaUrl() {
		return primaryMediaUrl;
	}

	public void setPrimaryMediaUrl(String primaryMediaUrl) {
		this.primaryMediaUrl = primaryMediaUrl;
	}

	public boolean isOnSale() {
		return onSale;
	}

	public void setOnSale(boolean onSale) {
		this.onSale = onSale;
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

	public WeightWrap getWeight() {
		return weight;
	}

	public void setWeight(WeightWrap weight) {
		this.weight = weight;
	}

	public DimensionWrap getDimension() {
		return dimension;
	}

	public void setDimension(DimensionWrap dimension) {
		this.dimension = dimension;
	}

	public ProductWrap getProduct() {
		return product;
	}

	public void setProduct(ProductWrap product) {
		this.product = product;
	}
    
    
}
