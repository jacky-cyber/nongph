package cn.globalph.b2c.offer.domain;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupImpl;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationToOneLookup;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.override.AdminPresentationMergeEntry;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverride;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverrides;
import cn.globalph.common.presentation.override.PropertyType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "NPH_FG_ADJUSTMENT")
@Inheritance(strategy=InheritanceType.JOINED)
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
@AdminPresentationMergeOverrides(
    {
        @AdminPresentationMergeOverride(name = "", mergeEntries =
            @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.READONLY,
                                            booleanOverrideValue = true))
    }
)
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "FulfillmentGroupAdjustmentImpl_baseFulfillmentGroupAdjustment")
public class FulfillmentGroupAdjustmentImpl implements FulfillmentGroupAdjustment{

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "FGAdjustmentId")
    @GenericGenerator(
        name="FGAdjustmentId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FulfillmentGroupAdjustmentImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.offer.domain.FulfillmentGroupAdjustmentImpl")
        }
    )
    @Column(name = "FG_ADJUSTMENT_ID")
    protected Long id;

    @ManyToOne(targetEntity = FulfillmentGroupImpl.class)
    @JoinColumn(name = "FULFILLMENT_GROUP_ID")
    @Index(name="FGADJUSTMENT_INDEX", columnNames={"FULFILLMENT_GROUP_ID"})
    @AdminPresentation(excluded = true)
    protected FulfillmentGroup fulfillmentGroup;

    @ManyToOne(targetEntity = OfferImpl.class, optional=false)
    @JoinColumn(name = "OFFER_ID")
    @Index(name="FGADJUSTMENT_OFFER_INDEX", columnNames={"OFFER_ID"})
    @AdminPresentation(friendlyName = "FulfillmentGroupAdjustmentImpl_Offer", order=1000,
            prominent = true, gridOrder = 1000)
    @AdminPresentationToOneLookup()
    protected Offer offer;

    @Column(name = "ADJUSTMENT_REASON", nullable=false)
    @AdminPresentation(friendlyName = "FulfillmentGroupAdjustmentImpl_FG_Adjustment_Reason", order=2000)
    protected String reason;

    @Column(name = "ADJUSTMENT_VALUE", nullable=false, precision=19, scale=5)
    @AdminPresentation(friendlyName = "FulfillmentGroupAdjustmentImpl_FG_Adjustment_Value", order=3000,
            fieldType = SupportedFieldType.MONEY, prominent = true,
            gridOrder = 2000)
    protected BigDecimal value = Money.ZERO.getAmount();

    @Override
    public void init(FulfillmentGroup fulfillmentGroup, Offer offer, String reason){
        this.fulfillmentGroup = fulfillmentGroup;
        this.offer = offer;

        if (reason == null) {
            this.reason = offer.getName();
        } else {
            this.reason = reason;
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public FulfillmentGroup getFulfillmentGroup() {
        return fulfillmentGroup;
    }

    @Override
    public void setFulfillmentGroup(FulfillmentGroup fulfillmentGroup) {
        this.fulfillmentGroup = fulfillmentGroup;
    }

    @Override
    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public Money getValue() {
        return value == null ? null : new Money(value);
    }
    
    @Override
    public void setValue(Money value) {
        this.value = value.getAmount();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fulfillmentGroup == null) ? 0 : fulfillmentGroup.hashCode());
        result = prime * result + ((offer == null) ? 0 : offer.hashCode());
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FulfillmentGroupAdjustmentImpl other = (FulfillmentGroupAdjustmentImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (fulfillmentGroup == null) {
            if (other.fulfillmentGroup != null) {
                return false;
            }
        } else if (!fulfillmentGroup.equals(other.fulfillmentGroup)) {
            return false;
        }
        if (offer == null) {
            if (other.offer != null) {
                return false;
            }
        } else if (!offer.equals(other.offer)) {
            return false;
        }
        if (reason == null) {
            if (other.reason != null) {
                return false;
            }
        } else if (!reason.equals(other.reason)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

}
