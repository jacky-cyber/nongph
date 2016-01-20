package cn.globalph.b2c.product.domain.wrap;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "relatedProduct")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class RelatedProductWrap{

    @XmlElement
    protected Long id;
    
    @XmlElement
    protected BigDecimal sequence;
    
    @XmlElement
    protected String promotionalMessage;

    @XmlElement
    protected ProductWrap product;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getSequence() {
		return sequence;
	}

	public void setSequence(BigDecimal sequence) {
		this.sequence = sequence;
	}

	public String getPromotionalMessage() {
		return promotionalMessage;
	}

	public void setPromotionalMessage(String promotionalMessage) {
		this.promotionalMessage = promotionalMessage;
	}

	public ProductWrap getProduct() {
		return product;
	}

	public void setProduct(ProductWrap product) {
		this.product = product;
	}
    
}
