package cn.globalph.b2c.order.domain.wrap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fulfillmentOption")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class FulfillmentOptionWrap{

    @XmlElement
    protected Long id;

    @XmlElement
    protected String name;

    @XmlElement
    protected String description;

    @XmlElement
    protected EnumerationTypeWrap fulfillmentType;
    
    public Long getId() {
        return this.id;
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

	public EnumerationTypeWrap getFulfillmentType() {
		return fulfillmentType;
	}

	public void setFulfillmentType(EnumerationTypeWrap fulfillmentType) {
		this.fulfillmentType = fulfillmentType;
	}

	public void setId(Long id) {
		this.id = id;
	}
}