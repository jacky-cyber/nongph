package cn.globalph.b2c.order.fulfillment.domain;

import cn.globalph.b2c.order.service.type.FulfillmentBandResultAmountType;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.client.SupportedFieldType;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import java.math.BigDecimal;

/**
 * 
 * @author Phillip Verheyden
 * @see {@link FulfillmentPriceBandImpl}, {@link FulfillmentWeightBandImpl}
 */
@MappedSuperclass
public abstract class FulfillmentBandImpl implements FulfillmentBand {

    private static final long serialVersionUID = 1L;

    @Column(name="RESULT_AMOUNT", precision=19, scale=5, nullable = false)
    protected BigDecimal resultAmount;
    
    @Column(name="RESULT_AMOUNT_TYPE", nullable = false)
    @AdminPresentation(friendlyName="Result Type", fieldType=SupportedFieldType.BROADLEAF_ENUMERATION, broadleafEnumeration="cn.globalph.b2c.order.service.type.FulfillmentBandResultAmountType")
    protected String resultAmountType = FulfillmentBandResultAmountType.RATE.getType();

    @Override
    public BigDecimal getResultAmount() {
        return resultAmount;
    }

    @Override
    public void setResultAmount(BigDecimal resultAmount) {
        this.resultAmount = resultAmount;
    }

    @Override
    public FulfillmentBandResultAmountType getResultAmountType() {
        return FulfillmentBandResultAmountType.getInstance(resultAmountType);
    }

    @Override
    public void setResultAmountType(FulfillmentBandResultAmountType resultAmountType) {
        this.resultAmountType = resultAmountType.getType();
    }

}
