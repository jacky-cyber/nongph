package cn.globalph.b2c.catalog.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import cn.globalph.b2c.product.domain.ProductImpl;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.util.WeightUnitOfMeasureType;

/**
 * 
 * @felix.wu
 *
 */
@Embeddable
public class Weight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "WEIGHT")
    @AdminPresentation(friendlyName = "ProductWeight_Product_Weight", order = 8000,
        tab = ProductImpl.Presentation.Tab.Name.Shipping, tabOrder = ProductImpl.Presentation.Tab.Order.Shipping,
        group = ProductImpl.Presentation.Group.Name.Shipping, groupOrder = ProductImpl.Presentation.Group.Order.Shipping)
    protected BigDecimal weight;

        
    @Column(name = "WEIGHT_UNIT_OF_MEASURE")
    @AdminPresentation(friendlyName = "ProductWeight_Product_Weight_Units", order = 9000,
        tab = ProductImpl.Presentation.Tab.Name.Shipping, tabOrder = ProductImpl.Presentation.Tab.Order.Shipping,
        group = ProductImpl.Presentation.Group.Name.Shipping, groupOrder = ProductImpl.Presentation.Group.Order.Shipping,
        fieldType= SupportedFieldType.BROADLEAF_ENUMERATION, 
        broadleafEnumeration="cn.globalph.common.util.WeightUnitOfMeasureType")
    protected String weightUnitOfMeasure;

    public WeightUnitOfMeasureType getWeightUnitOfMeasure() {
        return WeightUnitOfMeasureType.getInstance(weightUnitOfMeasure);
    }

    public void setWeightUnitOfMeasure(WeightUnitOfMeasureType weightUnitOfMeasure) {
        if (weightUnitOfMeasure != null) {
            this.weightUnitOfMeasure = weightUnitOfMeasure.getType();
        }
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weight)) return false;

        Weight weight1 = (Weight) o;

        if (weight != null ? !weight.equals(weight1.weight) : weight1.weight != null) return false;
        if (weightUnitOfMeasure != null ? !weightUnitOfMeasure.equals(weight1.weightUnitOfMeasure) : weight1
                .weightUnitOfMeasure != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = weight != null ? weight.hashCode() : 0;
        result = 31 * result + (weightUnitOfMeasure != null ? weightUnitOfMeasure.hashCode() : 0);
        return result;
    }
}
