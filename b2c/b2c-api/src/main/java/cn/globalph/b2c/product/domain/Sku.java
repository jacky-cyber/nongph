package cn.globalph.b2c.product.domain;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.Dimension;
import cn.globalph.b2c.catalog.domain.QuantityAvailableSkuTemplate;
import cn.globalph.b2c.catalog.domain.Weight;
import cn.globalph.b2c.catalog.service.dynamic.SkuPricingConsiderationContext;
import cn.globalph.b2c.inventory.service.InventoryService;
import cn.globalph.b2c.inventory.service.type.InventoryType;
import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.order.service.type.FulfillmentType;
import cn.globalph.b2c.order.service.workflow.CheckAvailabilityActivity;
import cn.globalph.common.media.domain.Media;
import cn.globalph.common.money.Money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Implementations of this interface are used to hold data about a SKU.  A SKU is
 * a specific item that can be sold including any specific attributes of the item such as
 * color or size.
 * <br>
 * <br>
 * You should implement this class if you want to make significant changes to how the
 * class is persisted.  If you just want to add additional fields then you should extend {@link SkuImpl}.
 *
 * @see {@link SkuImpl}, {@link Money}
 * @author btaylor
 *
 */
public interface Sku extends Serializable {

    /**
     * Returns the id of this sku
     */
    public Long getId();

    /**
     * Sets the id of this sku
     */
    public void setId(Long id);

    /**
     * This is the sum total of the priceAdjustments from the associated ProductOptionValues
     * 
     * @return <b>null</b> if there are no ProductOptionValues associated with this Sku or
     * all of their priceAdjustments are null. Otherwise this will be the sum total of those price
     * adjustments
     * 
     * @see {@link ProductOptionValue}
     */
    public Money getProductOptionValueAdjustments();
    
    /**
     * Returns the Sale Price of the Sku.  The Sale Price is the standard price the vendor sells
     * this item for.  If {@link SkuPricingConsiderationContext} is set, this uses the DynamicSkuPricingService
     * to calculate what this should actually be rather than use the property itself
     * 
     * @see SkuPricingConsiderationContext, DynamicSkuPricingService
     */
    public Money getSalePrice();

    /**
     * Sets the the Sale Price of the Sku.  The Sale Price is the standard price the vendor sells
     * this item for. This price will automatically be overridden if your system is utilizing
     * the DynamicSkuPricingService.
     */
    public void setSalePrice(Money salePrice);

    /**
     * Determines if there is a sale price.  In other words, determines whether salePrice is null. Returns true if 
     * salePrice is not null.  Returns false otherwise.
     * @return
     */
    public boolean hasSalePrice();

    /**
     * Returns the Retail Price of the Sku.  The Retail Price is the MSRP of the sku. If {@link SkuPricingConsiderationContext}
     * is set, this uses the DynamicSkuPricingService to calculate what this should actually be rather than use the property
     * itself.
     * 
     * @throws IllegalStateException if retail price is null. 
     * 
     * @see SkuPricingConsiderationContext, DynamicSkuPricingService, Sku.hasRetailPrice()
     */
    public Money getRetailPrice();

    /**
     * Sets the retail price for the Sku. This price will automatically be overridden if your system is utilizing
     * the DynamicSkuPricingService.
     * 
     * @param retail price for the Sku
     */
    public void setRetailPrice(Money retailPrice);

    /**
     * Provides a way of determining if a Sku has a retail price without getting an IllegalStateException. Returns true if 
     * retailPrice is not null.  Returns false otherwise.
     * @see Sku.getRetailPrice()
     * @return
     */
    public boolean hasRetailPrice();

    /**
     * Resolves the price of the Sku. If the Sku is on sale (that is, isOnSale() returns true), the
     * return value will be the result of getSalePrice(). Otherwise, the return value will be the result of
     * getRetailPrice().
     * @return the price of the Sku
     */
    public Money getPrice();

    /**
     * Returns the List Price of the Sku.  The List Price is the MSRP of the sku.
     * @deprecated
     */
    @Deprecated
    public Money getListPrice();

    /**
     * Sets the the List Price of the Sku.  The List Price is the MSRP of the sku.
     * @deprecated
     */
    @Deprecated
    public void setListPrice(Money listPrice);

    /**
     * Returns the name of the Sku.  The name is a label used to show when displaying the sku.
     */
    public String getName();

    /**
     * Sets the the name of the Sku.  The name is a label used to show when displaying the sku.
     */
    public void setName(String name);

    /**
     * Returns the brief description of the Sku.
     */
    public String getDescription();

    /**
     * Sets the brief description of the Sku.
     */
    public void setDescription(String description);

    /**
     * Returns the long description of the sku.
     */
    public String getLongDescription();

    /**
     * Sets the long description of the sku.
     */
    public void setLongDescription(String longDescription);

    /**
     * Returns the origin description of the sku.
     */
    public String getOriginDescription();

    /**
     * Sets the origin description of the sku.
     */
    public void setOriginDescription(String paramDescription);
    
    /**
     * Returns the parameter description of the sku.
     */
    public String getParamDescription();

    /**
     * Sets the parameter description of the sku.
     */
    public void setParamDescription(String paramDescription);
    
    /**
     * Returns whether the Sku qualifies for discounts or not.  This field is used by the pricing engine
     * to apply offers.
     */
    public Boolean isDiscountable();

    /**
     * Sets the whether the Sku qualifies for discounts or not.  This field is used by the pricing engine
     * to apply offers.
     */
    public void setDiscountable(Boolean discountable);

    /**
     * <p>Availability is really a concern of inventory vs a concern of the Sku being active or not. A Sku could be marked as
     * unavailable but still be considered 'active' where you still want to show the Sku on the site but not actually sell
     * it. This defaults to true</p>
     * 
     * <p>This method only checks that this Sku is not marked as {@link InventoryType#UNAVAILABLE}. If {@link #getInventoryType()}
     * is set to {@link InventoryType#CHECK_QUANTITY} then this will return true.</p>
     * 
     * @deprecated use {@link #getInventoryType()} or {@link InventoryService#isAvailable(Sku, int)} instead.
     */
    @Deprecated
    public Boolean isAvailable();

    /**
     * Returns the first date that the Sku should be available for sale.  This field is used to determine
     * whether a user can add the sku to their cart.
     */
    public Date getActiveStartDate();

    /**
     * Sets the the first date that the Sku should be available for sale.  This field is used to determine
     * whether a user can add the sku to their cart.
     */
    public void setActiveStartDate(Date activeStartDate);

    /**
     * Returns the the last date that the Sku should be available for sale.  This field is used to determine
     * whether a user can add the sku to their cart.
     */
    public Date getActiveEndDate();

    /**
     * Sets the the last date that the Sku should be available for sale.  This field is used to determine
     * whether a user can add the sku to their cart.
     */
    public void setActiveEndDate(Date activeEndDate);

    /**
     * Get the dimensions for this Sku
     * 
     * @return this Sku's embedded Weight
     */
    public Dimension getDimension();

    /**
     * Sets the embedded Dimension for this Sku
     * 
     * @param dimension
     */
    public void setDimension(Dimension dimension);

    /**
     * Gets the embedded Weight for this Sku
     * 
     * @return this Sku's embedded Weight
     */
    public Weight getWeight();

    /**
     * Sets the embedded Weight for this Sku
     * 
     * @param weight
     */
    public void setWeight(Weight weight);
    
    /**
     * Returns a boolean indicating whether this sku is active.  This is used to determine whether a user
     * the sku can add the sku to their cart.
     */
    public boolean isActive();

    /**
     * Returns a map of key/value pairs where the key is a string for the name of a media object and the value
     * is a media object.
     */
    public Map<String, Media> getSkuMedia();

    /**
     * Sets a map of key/value pairs where the key is a string for the name of a media object and the value
     * is an object of type Media.
     */
    public void setSkuMedia(Map<String, Media> skuMedia);

    /**
     * Returns whether or not this Sku, the given Product and the given Category are all active
     * 
     * @param product - Product that should be active
     * @param category - Category that should be active
     * @return <b>true</b> if this Sku, <code>product</code> and <code>category</code> are all active
     * <b>false</b> otherwise
     */
    public boolean isActive(Product product, Category category);

    /**
     * Denormalized set of key-value pairs to attach to a Sku. If you are looking for setting up
     * a {@link ProductOption} scenario (like colors, sizes, etc) see {@link getProductOptionValues()}
     * and {@link setProductOptionValues()}
     *
     * @return the attributes for this Sku
     */
    public Map<String, SkuAttribute> getSkuAttributes();

    /**
     * Sets the denormalized set of key-value pairs on a Sku
     *
     * @param skuAttributes
     */
    public void setSkuAttributes(Map<String, SkuAttribute> skuAttributes);

    /**
     * Gets the ProductOptionValues used to map to this Sku. For instance, this Sku could hold specific
     * inventory, price and image information for a "Blue" "Extra-Large" shirt
     * 
     * @return the ProductOptionValues for this Sku
     * @see {@link ProductOptionValue}, {@link ProductOption}
     */
    public List<ProductOptionValue> getProductOptionValues();

    /**
     * Sets the ProductOptionValues that should be mapped to this Sku
     * 
     * @param productOptionValues
     * @see {@link ProductOptionValue}, {@link ProductOption}
     */
    public void setProductOptionValues(List<ProductOptionValue> productOptionValues);

    /**
     * This will return the correct Product association that is being used on the Sku. If this Sku is a default Sku
     * for a Product (as in, {@link #getDefaultProduct()} != null) than this will return {@link #getDefaultProduct()}. If this is not
     * a default Sku for a Product, this will return the @ManyToOne Product relationship created by adding this Sku to a Product's
     * list of Skus, or using {@link setProduct(Product)}.
     * <br />
     * <br />
     * In some implementations, it might make sense to have both the @OneToOne association set ({@link Product#setDefaultSku(Sku)})
     * as well as the @ManyToOne association set ({@link #setProduct(Product)}). In this case, This method would only return
     * the result of {@link #getDefaultProduct()}.  However, the @OneToOne and @ManyToOne association should never actually
     * refer to different Products, and would represent an error state. If you require this, consider subclassing and using
     * your own @ManyToMany relationship between Product and Sku. If you are trying to model bundles, consider using a {@link ProductBundle}
     * and subsequent {@link SkuBundleItem}s.
     * 
     * @return {@link #getDefaultProduct()} if {@link #getDefaultProduct()} is non-null, the @ManyToOne Product association otherwise. If no
     * relationship is set, returns null
     */
    public Product getProduct();

    /**
     * Associates a Sku to a given Product. This will then show up in the list returned by {@link Product#getSkus()}
     * 
     * @param product - Product to associate this Sku to
     * @see Product#getSkus()
     */
    public void setProduct(Product product);

    /**
     * A product is on sale provided the sale price is not null, non-zero, and less than the retail price.
     * 
     * Note that this flag should always be checked before showing or using a sale price as it is possible 
     * for a sale price to be greater than the retail price from a purely data perspective.
     * 
     * @return whether or not the product is on sale
     */
    public boolean isOnSale();

    /**
     * Whether this Sku can be sorted by a machine
     * 
     * @return <b>true</b> if this Sku can be sorted by a machine
     * @deprecated use {@link #getIsMachineSortable()} instead since that is the correct bean notation
     */
    @Deprecated
    public Boolean isMachineSortable();

    /**
     * Whether this Sku can be sorted by a machine
     * 
     */
    public Boolean getIsMachineSortable();

    /**
     * Sets whether or not this Sku can be sorted by a machine
     * 
     * @param isMachineSortable
     * @deprecated use {@link #setIsMachineSortable(Boolean)} instead since that is the correct bean notation
     */
    @Deprecated
    public void setMachineSortable(Boolean isMachineSortable);
    
    /**
     * Sets whether or not this Sku can be sorted by a machine
     * @param isMachineSortable
     */
    public void setIsMachineSortable(Boolean isMachineSortable);

    /**
     * Gets all the extra fees for this particular Sku. If the fee type is FULFILLMENT, these are stored
     * on {@link FulfillmentGroup#getFulfillmentGroupFees()} for an Order
     * 
     * @return the {@link SkuFee}s for this Sku
     */
    public List<SkuFee> getFees();

    /**
     * Sets the extra fees for this particular Sku
     * 
     * @param fees
     */
    public void setFees(List<SkuFee> fees);

    /**
     * Gets the flat rate for fulfilling this {@link Sku} for a particular {@link FulfillmentOption}. Depending
     * on the result of {@link FulfillmentOption#getUseFlatRates()}, this flat rate will be used in calculating
     * the cost of fulfilling this {@link Sku}.
     * 
     * @return the flat rates for this {@link Sku}
     */
    public Map<FulfillmentOption, BigDecimal> getFulfillmentFlatRates();

    /**
     * Sets the flat rates for fulfilling this {@link Sku} for a particular {@link FulfillmentOption}. Depending
     * on the result of {@link FulfillmentOption#getUseFlatRates()}, this flat rate will be used in calculating
     * the cost of fulfilling this {@link Sku}.
     * 
     * @param fulfillmentFlatRates
     */
    public void setFulfillmentFlatRates(Map<FulfillmentOption, BigDecimal> fulfillmentFlatRates);

    /**
     * Gets the {@link FulfillmentOption}s that this {@link Sku} should be excluded from. For instance,
     * some {@link Sku}s might not be available to be fulfilled next-day
     * 
     * @return
     */
    public List<FulfillmentOption> getExcludedFulfillmentOptions();

    /**
     * Sets the {@link FulfillmentOption}s that this Sku should be excluded from being apart of
     * 
     * @param excludedFulfillmentOptions
     */
    public void setExcludedFulfillmentOptions(List<FulfillmentOption> excludedFulfillmentOptions);

    /**
     * Returns the type of inventory for this sku
     * @return the {@link cn.globalph.b2c.inventory.service.type.InventoryType} for this sku
     */
    public InventoryType getInventoryType();

    /**
     * Sets the type of inventory for this sku
     * @param inventoryType the {@link InventoryType} for this sku
     */
    public void setInventoryType(InventoryType inventoryType);
    
    /**
     * <p>Used in conjuction with {@link InventoryType#CHECK_QUANTITY} within the blAddItemWorkflow and blUpdateItemWorkflow.
     * This field is checked within the {@link CheckAvailabilityActivity} to determine if inventory is actually available
     * for this Sku.</p>
     * 
     * <p>In order to enable this feature in a Broadleaf 3.1.1+ installation, you must hook up the {@link QuantityAvailableSkuTemplate}
     * to dynamically weave in the quantityAvailable field or override this method in a subclass. This is enabled out of the
     * box in Broadleaf 3.2+</p>
     */
    public Integer getQuantityAvailable();
    
    /**
     * <p>Used in conjunction with {@link InventoryType#CHECK_QUANTITY} from {@link #getInventoryType()}. This sets how much
     * inventory is available for this Sku.</p>
     * 
     * <p>In order to enable this feature in a Broadleaf 3.1.1+ installation, you must hook up the {@link QuantityAvailableSkuTemplate}
     * to dynamically weave in the quantityAvailable field or override this method in a subclass. This is enabled out of the
     * box in Broadleaf 3.2+</p>
     * 
     * @param quantityAvailable the quantity available for this sku 
     */
    public void setQuantityAvailable(Integer quantityAvailable);
    
    /**
     * Returns the fulfillment type for this sku. May be null.
     * @return
     */
    public FulfillmentType getFulfillmentType();
    
    /**
     * Sets the fulfillment type for this sku. May return null.
     * @param fulfillmentType
     */
    public void setFulfillmentType(FulfillmentType fulfillmentType);

    /**
     * Clears any currently stored dynamic pricing
     */
    public void clearDynamicPrices();
    
    public void setSaleCount(Integer saleCount);
    
    public Integer getSaleCount();
    
    public List<ComparePrice> getComparePrice();
    
    public void setComparePrice(List<ComparePrice> comparePrice);
    
    public boolean getIsGroupOn();
    
    public void setIsGroupOn(boolean isGroupOn);
    
    public GroupOn getGroupOn(); 
    
    public void setGroupOn(GroupOn groupOn);
    
    /**
     * seconds kill
     * @param isSk
     */
    public void setIsSeckilling(boolean isSeckilling);
    
    public boolean getIsSeckilling();
    
    public void setSeckillingStartDate(Date seckillingStartDate);
    
    public Date getSeckillingStartDate();
    
    public void setSeckillingEndDate(Date seckillingEndDate);
    
    public Date getSeckillingEndDate();
    
    public void setLimit(Integer limit);
    
    public Integer getLimit();
    
    public boolean isSeckillingValid();
    
    public Long getSeckillingEndRemainTime();
    
    public Long getSeckillingStartRemainTime();

    public boolean getIsOffline();
    
    public void setIsOffline(boolean isOffline);
    
    public boolean isCodeSupport();
    
    public void setCodeSupport(boolean codeSupport);
    
    public boolean isPresale();
    
    public void setPresale(boolean presale);

    /*add jenny 15/09/14 s*/
    boolean isPointSupport();

    void setPointCode(boolean pointCode);
    /*add jenny 15/09/14 e*/
}
