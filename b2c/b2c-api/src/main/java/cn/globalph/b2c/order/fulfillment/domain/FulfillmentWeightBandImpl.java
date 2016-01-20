package cn.globalph.b2c.order.fulfillment.domain;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.util.WeightUnitOfMeasureType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.math.BigDecimal;

/**
 * 
 * @author Phillip Verheyden
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_FULFILLMENT_WEIGHT_BAND")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
public class FulfillmentWeightBandImpl extends FulfillmentBandImpl implements FulfillmentWeightBand {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "FulfillmentWeightBandId")
    @GenericGenerator(
        name="FulfillmentWeightBandId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FulfillmentWeightBandImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.order.fulfillment.domain.FulfillmentWeightBandImpl")
        }
    )
    @Column(name = "FULFILLMENT_WEIGHT_BAND_ID")
    protected Long id;
    
    @Column(name = "MINIMUM_WEIGHT", precision = 19, scale = 5)
    @AdminPresentation(friendlyName = "FulfillmentWeightBandImpl_Weight")
    protected BigDecimal minimumWeight;
    
    @Column(name = "WEIGHT_UNIT_OF_MEASURE")
    @AdminPresentation(friendlyName = "FulfillmentWeightBandImpl_Weight_Units", fieldType= SupportedFieldType.BROADLEAF_ENUMERATION, broadleafEnumeration="cn.globalph.common.util.WeightUnitOfMeasureType")
    protected String weightUnitOfMeasure;
    
    @ManyToOne(targetEntity=BandedWeightFulfillmentOptionImpl.class)
    @JoinColumn(name="FULFILLMENT_OPTION_ID")
    protected BandedWeightFulfillmentOption option;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public BigDecimal getMinimumWeight() {
        return minimumWeight;
    }
    
    @Override
    public void setMinimumWeight(BigDecimal minimumWeight) {
        this.minimumWeight = minimumWeight;
    }
    
    @Override
    public BandedWeightFulfillmentOption getOption() {
        return option;
    }

    @Override
    public void setOption(BandedWeightFulfillmentOption option) {
        this.option = option;
    }

    @Override
    public WeightUnitOfMeasureType getWeightUnitOfMeasure() {
        return WeightUnitOfMeasureType.getInstance(weightUnitOfMeasure);
    }

    @Override
    public void setWeightUnitOfMeasure(WeightUnitOfMeasureType weightUnitOfMeasure) {
        if (weightUnitOfMeasure != null) {
            this.weightUnitOfMeasure = weightUnitOfMeasure.getType();
        }
    }

}
