package cn.globalph.b2c.order.domain.wrap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "EnumerationTypeWrap")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class EnumerationTypeWrap{


    @XmlElement
    protected String friendlyName;

    @XmlElement
    protected String type;

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
