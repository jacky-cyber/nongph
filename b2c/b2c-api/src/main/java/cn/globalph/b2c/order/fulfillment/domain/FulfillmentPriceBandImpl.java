package cn.globalph.b2c.order.fulfillment.domain;

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
@Table(name = "NPH_FULFILLMENT_PRICE_BAND")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
public class FulfillmentPriceBandImpl extends FulfillmentBandImpl implements FulfillmentPriceBand {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator= "FulfillmentPriceBandId")
    @GenericGenerator(
        name="FulfillmentPriceBandId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FulfillmentPriceBandImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.order.fulfillment.domain.FulfillmentPriceBandImpl")
        }
    )
    @Column(name = "FULFILLMENT_PRICE_BAND_ID")
    protected Long id;

    @Column(name="RETAIL_PRICE_MINIMUM_AMOUNT", precision=19, scale=5, nullable = false)
    protected BigDecimal retailPriceMinimumAmount;
    
    @ManyToOne(targetEntity=BandedPriceFulfillmentOptionImpl.class)
    @JoinColumn(name="FULFILLMENT_OPTION_ID")
    protected BandedPriceFulfillmentOption option;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public BigDecimal getRetailPriceMinimumAmount() {
        return retailPriceMinimumAmount;
    }

    @Override
    public void setRetailPriceMinimumAmount(BigDecimal retailPriceMinimumAmount) {
        this.retailPriceMinimumAmount = retailPriceMinimumAmount;
    }

    @Override
    public BandedPriceFulfillmentOption getOption() {
        return option;
    }

    @Override
    public void setOption(BandedPriceFulfillmentOption option) {
        this.option = option;
    }

}
