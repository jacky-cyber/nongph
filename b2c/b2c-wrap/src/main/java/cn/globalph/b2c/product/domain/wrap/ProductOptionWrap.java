package cn.globalph.b2c.product.domain.wrap;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "productOption")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ProductOptionWrap{
    
    @XmlElement
    protected String attributeName;

    @XmlElement
    protected String label;

    @XmlElement
    protected Boolean required;
    
    @XmlElement
    protected String productOptionType;
    @XmlElement
    protected String productOptionValidationStrategyType;
    @XmlElement
    protected String productOptionValidationType;
    @XmlElement(name = "allowedValue")
    @XmlElementWrapper(name = "allowedValues")
    protected List<ProductOptionValueWrap> allowedValues;
    @XmlElement
    protected String validationString;
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public String getProductOptionType() {
		return productOptionType;
	}
	public void setProductOptionType(String productOptionType) {
		this.productOptionType = productOptionType;
	}
	public String getProductOptionValidationStrategyType() {
		return productOptionValidationStrategyType;
	}
	public void setProductOptionValidationStrategyType(
			String productOptionValidationStrategyType) {
		this.productOptionValidationStrategyType = productOptionValidationStrategyType;
	}
	public String getProductOptionValidationType() {
		return productOptionValidationType;
	}
	public void setProductOptionValidationType(String productOptionValidationType) {
		this.productOptionValidationType = productOptionValidationType;
	}
	public List<ProductOptionValueWrap> getAllowedValues() {
		return allowedValues;
	}
	public void setAllowedValues(List<ProductOptionValueWrap> allowedValues) {
		this.allowedValues = allowedValues;
	}
	public String getValidationString() {
		return validationString;
	}
	public void setValidationString(String validationString) {
		this.validationString = validationString;
	}
}
