package cn.globalph.b2c.order.domain;

import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.override.AdminPresentationMergeEntry;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverride;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverrides;
import cn.globalph.common.presentation.override.PropertyType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@DiscriminatorColumn(name = "TYPE")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_FULFILLMENT_GROUP_FEE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "blOrderElements")
@AdminPresentationMergeOverrides(
    {
        @AdminPresentationMergeOverride(name = "", mergeEntries =
            @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.READONLY,
                                            booleanOverrideValue = true))
    }
)
public class FulfillmentGroupFeeImpl implements FulfillmentGroupFee {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "FulfillmentGroupFeeId")
    @GenericGenerator(
        name="FulfillmentGroupFeeId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FulfillmentGroupFeeImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.order.domain.FulfillmentGroupFeeImpl")
        }
    )
    @Column(name = "FULFILLMENT_GROUP_FEE_ID")
    protected Long id;

    @ManyToOne(targetEntity = FulfillmentGroupImpl.class, optional = false)
    @JoinColumn(name = "FULFILLMENT_GROUP_ID")
    protected FulfillmentGroup fulfillmentGroup;

    @Column(name = "AMOUNT", precision=19, scale=5)
    @AdminPresentation(friendlyName = "FulfillmentGroupFeeImpl_Amount", prominent = true, gridOrder = 2000, order = 2000)
    protected BigDecimal amount;

    @Column(name = "NAME")
    @AdminPresentation(friendlyName = "FulfillmentGroupFeeImpl_Name", prominent = true, gridOrder = 1000, order = 1000)
    protected String name;

    @Column(name = "REPORTING_CODE")
    @AdminPresentation(friendlyName = "FulfillmentGroupFeeImpl_Reporting_Code", order = 3000)
    protected String reportingCode;
    
    @Column(name = "FEE_TAXABLE_FLAG")
    @AdminPresentation(friendlyName = "FulfillmentGroupFeeImpl_Taxable", order = 5000)
    protected Boolean feeTaxable = false; 
    
    @Column(name = "TOTAL_FEE_TAX", precision=19, scale=5)
    @AdminPresentation(friendlyName = "FulfillmentGroupFeeImpl_Total_Fee_Tax", order=4000, fieldType=SupportedFieldType.MONEY)
    protected BigDecimal totalTax;

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
    public Money getAmount() {
        return amount == null ? null : new Money(amount);
    }

    @Override
    public void setAmount(Money amount) {
        this.amount = Money.toAmount(amount);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getReportingCode() {
        return reportingCode;
    }

    @Override
    public void setReportingCode(String reportingCode) {
        this.reportingCode = reportingCode;
    }
    
    @Override
    public Boolean isTaxable() {
        return feeTaxable == null ? true : feeTaxable;
    }

    @Override
    public void setTaxable(Boolean taxable) {
        this.feeTaxable = taxable;
    }
    
    @Override
    public Money getTotalTax() {
        return totalTax == null ? null : new Money(totalTax);
    }

    @Override
    public void setTotalTax(Money totalTax) { 
        this.totalTax = Money.toAmount(totalTax);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((fulfillmentGroup == null) ? 0 : fulfillmentGroup.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((reportingCode == null) ? 0 : reportingCode.hashCode());
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
        FulfillmentGroupFeeImpl other = (FulfillmentGroupFeeImpl) obj;
        if (amount == null) {
            if (other.amount != null) {
                return false;
            }
        } else if (!amount.equals(other.amount)) {
            return false;
        }
        if (fulfillmentGroup == null) {
            if (other.fulfillmentGroup != null) {
                return false;
            }
        } else if (!fulfillmentGroup.equals(other.fulfillmentGroup)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (reportingCode == null) {
            if (other.reportingCode != null) {
                return false;
            }
        } else if (!reportingCode.equals(other.reportingCode)) {
            return false;
        }
        return true;
    }

}
