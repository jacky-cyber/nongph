package cn.globalph.b2c.product.domain.wrap;

import cn.globalph.common.money.Money;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "productOptionAllowedValue")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ProductOptionValueWrap{
    
    @XmlElement
    protected String attributeValue;
    
    @XmlElement
    protected Money priceAdjustment;
    
    @XmlElement
    protected Long productOptionId;

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public Money getPriceAdjustment() {
		return priceAdjustment;
	}

	public void setPriceAdjustment(Money priceAdjustment) {
		this.priceAdjustment = priceAdjustment;
	}

	public Long getProductOptionId() {
		return productOptionId;
	}

	public void setProductOptionId(Long productOptionId) {
		this.productOptionId = productOptionId;
	}
}
