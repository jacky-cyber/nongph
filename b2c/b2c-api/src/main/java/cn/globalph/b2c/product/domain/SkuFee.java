package cn.globalph.b2c.product.domain;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupFee;
import cn.globalph.common.money.Money;

import java.io.Serializable;
import java.util.List;

/**
 * Used to represent Sku-specific surcharges when fulfilling this item. For instance there might be a disposal fee
 * when selling batteries or an environmental fee for tires.
 * 
 * @see {@link FulfillmentGroupFee}, {@link FulfillmentGroup}
 *
 */
public interface SkuFee extends Serializable {
    
    public Long getId();

    public void setId(Long id);

    /**
     * Get the name of the surcharge
     * 
     * @return the surcharge name
     */
    public String getName();

    /**
     * Sets the name of the surcharge
     * 
     * @param name
     */
    public void setName(String name);

    /**
     * Get the description of the surcharge
     * 
     * @return the surcharge description
     */
    public String getDescription();

    /**
     * Sets the fee description
     * 
     * @param description
     */
    public void setDescription(String description);

    /**
     * Gets the amount to charge for this surcharge
     * 
     * @return the fee amount
     */
    public Money getAmount();

    /**
     * Sets the amount to charge for this surcharge
     * 
     * @param amount
     */
    public void setAmount(Money amount);

    /**
     * Gets the optional MVEL expression used as additional criteria to determine if
     * this fee applies
     * 
     * @return the MVEL expression of extra criteria to determine if this
     * fee applies
     */
    public String getExpression();

    /**
     * Sets the MVEL expression used to determine if this fee should be applied. If this is
     * null or empty, this fee will always be applied
     * 
     * @param expression - a valid MVEL expression
     */
    public void setExpression(String expression);
    
    public SkuFeeType getFeeType();

    public void setFeeType(SkuFeeType feeType);

    /**
     * Gets the Skus associated with this surcharge
     * 
     * @return Skus that have this particular surcharge
     */
    public List<Sku> getSkus();

    /**
     * Sets the Skus associated with this surcharge
     * 
     * @param skus
     */
    public void setSkus(List<Sku> skus);
    
}
