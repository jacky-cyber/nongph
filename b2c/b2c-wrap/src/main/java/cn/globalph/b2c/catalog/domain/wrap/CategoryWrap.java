package cn.globalph.b2c.catalog.domain.wrap;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import cn.globalph.b2c.product.domain.wrap.ProductWrap;
import cn.globalph.common.util.xml.ISO8601DateAdapter;

public class CategoryWrap{

    @XmlElement
    protected Long id;

    @XmlElement
    protected String name;

    @XmlElement
    protected String description;

    @XmlElement
    protected Boolean active;

    @XmlElement
    protected String url;

    @XmlElement
    protected String urlKey;

    @XmlElement
    @XmlJavaTypeAdapter(ISO8601DateAdapter.class)
    protected Date activeStartDate;

    @XmlElement
    @XmlJavaTypeAdapter(ISO8601DateAdapter.class)
    protected Date activeEndDate;

    @XmlElement(name = "category")
    @XmlElementWrapper(name = "subcategories")
    protected List<CategoryWrap> subcategories;

    @XmlElement(name = "product")
    @XmlElementWrapper(name = "products")
    protected List<ProductWrap> products;

    @XmlElement(name = "categoryAttribute")
    @XmlElementWrapper(name = "categoryAttributes")
    protected List<CategoryAttributeWrap> categoryAttributes;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlKey() {
		return urlKey;
	}

	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
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

	public List<CategoryWrap> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<CategoryWrap> subcategories) {
		this.subcategories = subcategories;
	}

	public List<ProductWrap> getProducts() {
		return products;
	}

	public void setProducts(List<ProductWrap> products) {
		this.products = products;
	}

	public List<CategoryAttributeWrap> getCategoryAttributes() {
		return categoryAttributes;
	}

	public void setCategoryAttributes(List<CategoryAttributeWrap> categoryAttributes) {
		this.categoryAttributes = categoryAttributes;
	}
}
