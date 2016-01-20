package cn.globalph.b2c.inventory.domain.wrap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "inventory")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class InventoryWrap {

    @XmlElement
    protected Long skuId;
    
    @XmlElement(nillable = true)
    protected Integer quantityAvailable;
    
    @XmlElement(nillable = true)
    protected String inventoryType;

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Integer getQuantityAvailable() {
		return quantityAvailable;
	}

	public void setQuantityAvailable(Integer quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}

	public String getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(String inventoryType) {
		this.inventoryType = inventoryType;
	}
}
