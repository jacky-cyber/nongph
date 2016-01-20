package cn.globalph.b2c.product.domain.wrap;

import cn.globalph.common.money.Money;
import cn.globalph.common.util.xml.ISO8601DateAdapter;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "product")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ProductWrap{

    @XmlElement
    protected Long id;

    @XmlElement
    protected String name;

    public String getName(){
    	return this.name;
    }
    @XmlElement
    protected String description;

    @XmlElement
    protected String longDescription;
    
    @XmlElement
    protected String url;
    
    @XmlElement
    protected boolean featuredProduct;

    @XmlElement
    protected Money retailPrice;

    @XmlElement
    protected Money salePrice;

    @XmlElement
    protected MediaWrap primaryMedia;

    @XmlElement
    protected Boolean active;

    @XmlElement(name = "productOption")
    @XmlElementWrapper(name = "productOptions")
    protected List<ProductOptionWrap> productOptions;

    // For bundles
    @XmlElement
    protected Integer priority;

    @XmlElement
    protected Money bundleItemsRetailPrice;

    @XmlElement
    protected Money bundleItemsSalePrice;

    //End for bundles

    @XmlElement
    @XmlJavaTypeAdapter(ISO8601DateAdapter.class)
    protected Date activeStartDate;

    @XmlElement
    @XmlJavaTypeAdapter(ISO8601DateAdapter.class)
    protected Date activeEndDate;

    @XmlElement
    protected String manufacturer;

    @XmlElement
    protected String model;

    @XmlElement
    protected String promoMessage;
    
    @XmlElement
    protected Long defaultCategoryId;

    @XmlElement(name = "upsaleProduct")
    @XmlElementWrapper(name = "upsaleProducts")
    protected List<RelatedProductWrap> upsaleProducts;

    @XmlElement(name = "crossSaleProduct")
    @XmlElementWrapper(name = "crossSaleProducts")
    protected List<RelatedProductWrap> crossSaleProducts;

    @XmlElement(name = "productAttribute")
    @XmlElementWrapper(name = "productAttributes")
    protected List<ProductAttributeWrap> productAttributes;

    @XmlElement(name = "media")
    @XmlElementWrapper(name = "mediaItems")
    protected List<MediaWrap> media;

    @XmlElement(name = "skuBundleItem")
    @XmlElementWrapper(name = "skuBundleItems")
    protected List<SkuBundleItemWrap> skuBundleItems;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isFeaturedProduct() {
		return featuredProduct;
	}

	public void setFeaturedProduct(boolean featuredProduct) {
		this.featuredProduct = featuredProduct;
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

	public MediaWrap getPrimaryMedia() {
		return primaryMedia;
	}

	public void setPrimaryMedia(MediaWrap primaryMedia) {
		this.primaryMedia = primaryMedia;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<ProductOptionWrap> getProductOptions() {
		return productOptions;
	}

	public void setProductOptions(List<ProductOptionWrap> productOptions) {
		this.productOptions = productOptions;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Money getBundleItemsRetailPrice() {
		return bundleItemsRetailPrice;
	}

	public void setBundleItemsRetailPrice(Money bundleItemsRetailPrice) {
		this.bundleItemsRetailPrice = bundleItemsRetailPrice;
	}

	public Money getBundleItemsSalePrice() {
		return bundleItemsSalePrice;
	}

	public void setBundleItemsSalePrice(Money bundleItemsSalePrice) {
		this.bundleItemsSalePrice = bundleItemsSalePrice;
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

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPromoMessage() {
		return promoMessage;
	}

	public void setPromoMessage(String promoMessage) {
		this.promoMessage = promoMessage;
	}

	public Long getDefaultCategoryId() {
		return defaultCategoryId;
	}

	public void setDefaultCategoryId(Long defaultCategoryId) {
		this.defaultCategoryId = defaultCategoryId;
	}

	public List<RelatedProductWrap> getUpsaleProducts() {
		return upsaleProducts;
	}

	public void setUpsaleProducts(List<RelatedProductWrap> upsaleProducts) {
		this.upsaleProducts = upsaleProducts;
	}

	public List<RelatedProductWrap> getCrossSaleProducts() {
		return crossSaleProducts;
	}

	public void setCrossSaleProducts(List<RelatedProductWrap> crossSaleProducts) {
		this.crossSaleProducts = crossSaleProducts;
	}

	public List<ProductAttributeWrap> getProductAttributes() {
		return productAttributes;
	}

	public void setProductAttributes(List<ProductAttributeWrap> productAttributes) {
		this.productAttributes = productAttributes;
	}

	public List<MediaWrap> getMedia() {
		return media;
	}

	public void setMedia(List<MediaWrap> media) {
		this.media = media;
	}

	public List<SkuBundleItemWrap> getSkuBundleItems() {
		return skuBundleItems;
	}

	public void setSkuBundleItems(List<SkuBundleItemWrap> skuBundleItems) {
		this.skuBundleItems = skuBundleItems;
	}

	public void setName(String name) {
		this.name = name;
	}
}
