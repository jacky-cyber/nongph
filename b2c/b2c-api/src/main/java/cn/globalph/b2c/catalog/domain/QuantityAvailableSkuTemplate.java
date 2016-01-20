package cn.globalph.b2c.catalog.domain;

import cn.globalph.b2c.product.domain.ProductImpl;
import cn.globalph.common.presentation.AdminPresentation;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Enables basic inventory management on a Sku
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Embeddable
public class QuantityAvailableSkuTemplate {

    @Column(name = "QUANTITY_AVAILABLE")
    @AdminPresentation(friendlyName = "SkuImpl_Sku_QuantityAvailable",
            order = 1010,
            tab = ProductImpl.Presentation.Tab.Name.Inventory,
            tabOrder = ProductImpl.Presentation.Tab.Order.Inventory,
            group = ProductImpl.Presentation.Group.Name.Inventory,
            groupOrder = ProductImpl.Presentation.Group.Order.Inventory)
    protected Integer quantityAvailable = 0;

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }
    
    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }
    
}
