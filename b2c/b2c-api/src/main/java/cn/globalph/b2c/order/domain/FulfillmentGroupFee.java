package cn.globalph.b2c.order.domain;

import cn.globalph.common.money.Money;

import java.io.Serializable;

public interface FulfillmentGroupFee extends Serializable {

    public Long getId();

    public void setId(Long id);

    public FulfillmentGroup getFulfillmentGroup();

    public void setFulfillmentGroup(FulfillmentGroup fulfillmentGroup);

    public Money getAmount();

    public void setAmount(Money amount);

    public String getName();

    public void setName(String name);

    public String getReportingCode();

    public void setReportingCode(String reportingCode);
    
    /**
     * Returns whether or not this fee is taxable. If this flag is not set, it returns true by default
     * 
     * @return the taxable flag. If null, returns true
     */
    public Boolean isTaxable();

    /**
     * Sets whether or not this fee is taxable
     * 
     * @param taxable
     */
    public void setTaxable(Boolean taxable);
    
    /**
     * Gets the total tax for this fee, which is the sum of all taxes for this fee.
     * This total is calculated in the TotalActivity stage of the pricing workflow.
     *
     * @return the total tax for this fee
     */
    public Money getTotalTax();

    /**
     * Sets the total tax for this fee, which is the sum of all taxes for this fee.
     * This total should only be set during the TotalActivity stage of the pricing workflow.
     *
     * @param totalTax the total tax for this fee
     */
    public void setTotalTax(Money totalTax);
}
