package cn.globalph.b2c.product.domain;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.Dimension;
import cn.globalph.b2c.catalog.domain.Weight;
import cn.globalph.b2c.catalog.service.dynamic.DefaultDynamicSkuPricingInvocationHandler;
import cn.globalph.b2c.catalog.service.dynamic.DynamicSkuPrices;
import cn.globalph.b2c.catalog.service.dynamic.SkuActiveDateConsiderationContext;
import cn.globalph.b2c.catalog.service.dynamic.SkuPricingConsiderationContext;
import cn.globalph.b2c.inventory.service.type.InventoryType;
import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.order.domain.FulfillmentOptionImpl;
import cn.globalph.b2c.order.service.type.FulfillmentType;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyCollection;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyMap;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.media.domain.Media;
import cn.globalph.common.media.domain.MediaImpl;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.*;
import cn.globalph.common.presentation.client.AddMethodType;
import cn.globalph.common.presentation.client.OperationScope;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.common.util.DateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Parameter;
import org.springframework.util.ClassUtils;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.*;

/**
 * The Class SkuImpl is the default implementation of {@link Sku}. A SKU is a
 * specific item that can be sold including any specific attributes of the item
 * such as color or size. <br>
 * <br>
 * If you want to add fields specific to your implementation you should extend this class and add your fields. If you
 * need to make significant changes to the SkuImpl then you should implement
 * your own version of {@link Sku}.<br>
 * <br>
 * This implementation uses a Hibernate implementation of JPA configured through
 * annotations. The Entity references the following tables: NPH_SKU,
 * NPH_SKU_IMAGE
 *
 * !!!!!!!!!!!!!!!!!
 * <p>For admin required field validation, if this Sku is apart of an additionalSkus list (meaning it is not a defaultSku) then
 * it should have no required restrictions on it. All additional Skus can delegate to the defaultSku of the related product
 * for all of its fields. For this reason, if you would like to mark more fields as required then rather than using
 * {@link AdminPresentation#requiredOverride()}, use the mo:overrides section in bl-admin-applicationContext.xml for Product
 * and reference each required field like 'defaultSku.name' or 'defaultSku.retailPrice'.</p>
 * @see {@link Sku}
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_SKU")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blProducts")
@AdminPresentationClass(friendlyName = "baseSku")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class SkuImpl implements Sku {
    
    private static final Log LOG = LogFactory.getLog(SkuImpl.class);
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "SkuId")
    @GenericGenerator(
        name = "SkuId",
        strategy = "cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name = "segment_value", value = "SkuImpl"),
            @Parameter(name = "entity_name", value = "cn.globalph.b2c.product.domain.SkuImpl")
        }
    )
    @Column(name = "SKU_ID")
    @AdminPresentation(friendlyName = "SkuImpl_Sku_ID", 
                       visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @Column(name = "SALE_PRICE", precision = 19, scale = 5, nullable = false)
    @AdminPresentation(friendlyName = "SkuImpl_Sku_Sale_Price",
            order = 2000,
                       group = ProductImpl.Presentation.Group.Name.Price, 
                       groupOrder = ProductImpl.Presentation.Group.Order.Price,
                       prominent = true, 
                       gridOrder = 6, 
                       fieldType = SupportedFieldType.MONEY)
    protected BigDecimal salePrice;

    @Column(name = "RETAIL_PRICE", precision = 19, scale = 5)
    @AdminPresentation(friendlyName = "SkuImpl_Sku_Retail_Price", 
                       order = 1000, 
                       group = ProductImpl.Presentation.Group.Name.Price, 
                       groupOrder = ProductImpl.Presentation.Group.Order.Price,
                       prominent = true, 
                       gridOrder = 5, 
                       fieldType = SupportedFieldType.MONEY)
    protected BigDecimal retailPrice;

    @Column(name = "SALE_COUNT")
    @AdminPresentation(friendlyName = "SkuImpl_Sku_SaleCount",
            order = 1010,
            prominent = true,
            gridOrder = 7,
            tab = ProductImpl.Presentation.Tab.Name.Inventory,
            tabOrder = ProductImpl.Presentation.Tab.Order.Inventory,
            group = ProductImpl.Presentation.Group.Name.Inventory,
            groupOrder = ProductImpl.Presentation.Group.Order.Inventory)
    protected Integer saleCount = 0;

    @Column(name = "QUANTITY_AVAILABLE")
    @AdminPresentation(friendlyName = "SkuImpl_Sku_QuantityAvailable",
            order = 1011,
            prominent = true,
            gridOrder = 8,
            tab = ProductImpl.Presentation.Tab.Name.Inventory,
            tabOrder = ProductImpl.Presentation.Tab.Order.Inventory,
            group = ProductImpl.Presentation.Group.Name.Inventory,
            groupOrder = ProductImpl.Presentation.Group.Order.Inventory)
    protected Integer quantityAvailable = 0;
    
    @Column(name = "NAME")
    @Index(name = "SKU_NAME_INDEX", columnNames = {"NAME"})
    @AdminPresentation(friendlyName = "SkuImpl_Sku_Name",  
                       order = ProductImpl.Presentation.FieldOrder.NAME,
                       group = ProductImpl.Presentation.Group.Name.General, 
                       groupOrder = ProductImpl.Presentation.Group.Order.General,
                       prominent = true, 
                       gridOrder = 1, 
                       columnWidth = "260px",
                       translatable = true)
    protected String name;

    @Column(name = "DESCRIPTION")
    @AdminPresentation(friendlyName = "短描述", 
                       order = ProductImpl.Presentation.FieldOrder.SHORT_DESCRIPTION, 
                       group = ProductImpl.Presentation.Group.Name.General, 
                       groupOrder = ProductImpl.Presentation.Group.Order.General)
    protected String description;

    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    @Column(name = "LONG_DESCRIPTION", length = Integer.MAX_VALUE - 1)
    @AdminPresentation(friendlyName = "长描述",
            order = ProductImpl.Presentation.FieldOrder.LONG_DESCRIPTION,
            tab = ProductImpl.Presentation.Tab.Name.Description,
            tabOrder = ProductImpl.Presentation.Tab.Order.Description,
            largeEntry = true,
            fieldType = SupportedFieldType.HTML_BASIC,
            translatable = true)
    protected String longDescription;

    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    @Column(name = "ORIGIN_DESCRIPTION", length = Integer.MAX_VALUE - 1)
    @AdminPresentation(friendlyName = "产地介绍",
            order = ProductImpl.Presentation.FieldOrder.ORIGIN_DESCRIPTION,
            tab = ProductImpl.Presentation.Tab.Name.Description,
            tabOrder = ProductImpl.Presentation.Tab.Order.Description,
            largeEntry = true,
            fieldType = SupportedFieldType.HTML_BASIC,
            translatable = true)
    protected String originDescription;

    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    @Column(name = "PARAM_DESCRIPTION", length = Integer.MAX_VALUE - 1)
    @AdminPresentation(friendlyName = "商品规格",
            order = ProductImpl.Presentation.FieldOrder.PARAM_DESCRIPTION,
            tab = ProductImpl.Presentation.Tab.Name.Description,
            tabOrder = ProductImpl.Presentation.Tab.Order.Description,
            largeEntry = true,
            fieldType = SupportedFieldType.HTML_BASIC,
            translatable = true)
    protected String paramDescrition;

    @Column(name = "DISCOUNTABLE_FLAG")
    @Index(name="SKU_DISCOUNTABLE_INDEX", columnNames={"DISCOUNTABLE_FLAG"})
    @AdminPresentation(friendlyName = "SkuImpl_Sku_Discountable", 
                       order = 2000, 
                       tab = ProductImpl.Presentation.Tab.Name.Advanced, 
                       tabOrder = ProductImpl.Presentation.Tab.Order.Advanced, 
                       group = ProductImpl.Presentation.Group.Name.Advanced, 
                       groupOrder = ProductImpl.Presentation.Group.Order.Advanced)
    protected Character discountable = 'Y';

    @Column(name = "ACTIVE_START_DATE")
    @Index(name="SKU_ACTIVE_START_INDEX")
    @AdminPresentation(friendlyName = "销售开始日期", 
    				   order = 1000,
    				   group = ProductImpl.Presentation.Group.Name.ActiveDateRange, 
    				   groupOrder = ProductImpl.Presentation.Group.Order.ActiveDateRange,
    				   tooltip = "skuStartDateTooltip")
    protected Date activeStartDate;

    @Column(name = "ACTIVE_END_DATE")
    @Index(name="SKU_ACTIVE_END_INDEX")
    @AdminPresentation(friendlyName = "销售结束日期", 
    				   order = 2000, 
    				   group = ProductImpl.Presentation.Group.Name.ActiveDateRange, 
    				   groupOrder = ProductImpl.Presentation.Group.Order.ActiveDateRange,
    				   tooltip = "skuEndDateTooltip")
    protected Date activeEndDate;
    
    @Column(name = "IS_SECKILLING")
    @AdminPresentation(friendlyName = "是否为秒杀商品",
    					tab = ProductImpl.Presentation.Tab.Name.SencondsKill)
    protected boolean isSeckilling;
    
    @Column(name = "IS_CODE_SUPPORT")
    @AdminPresentation(friendlyName = "是否可用品荟码")
    protected boolean codeSupport;

    @Column(name = "IS_POINT_SUPPORT")
    @AdminPresentation(friendlyName = "是否可用积分")
    protected boolean pointSupport;

    @Column(name = "IS_PRESALE")
    @AdminPresentation(friendlyName = "是否预售")
    protected boolean presale;
    
    @Column(name = "SECKILLING_START_DATE")
    @AdminPresentation(friendlyName = "秒杀开始时间",
	tab = ProductImpl.Presentation.Tab.Name.SencondsKill)
    protected Date seckillingStartDate;
    
    @Column(name = "SECKILLING_END_DATE")
    @AdminPresentation(friendlyName = "秒杀结束时间",
	tab = ProductImpl.Presentation.Tab.Name.SencondsKill)
    protected Date seckillingEndDate;
    
    @Column(name = "LIMIT_NUM")
    @AdminPresentation(friendlyName = "购买限制")
    protected Integer limit;

    @Embedded
    protected Dimension dimension = new Dimension();

    @Embedded
    protected Weight weight = new Weight();

    @Transient
    protected DynamicSkuPrices dynamicPrices = null;

    @Column(name = "IS_MACHINE_SORTABLE")
    @AdminPresentation(friendlyName = "ProductImpl_Is_Product_Machine_Sortable", 
    				   order = 10000,
    				   tab = ProductImpl.Presentation.Tab.Name.Shipping, 
    				   tabOrder = ProductImpl.Presentation.Tab.Order.Shipping,
    				   group = ProductImpl.Presentation.Group.Name.Shipping, 
    				   groupOrder = ProductImpl.Presentation.Group.Order.Shipping)
    protected Boolean isMachineSortable = true;
    
    @Column(name = "IS_GROUP_ON")
    @AdminPresentation(friendlyName = "团购商品", 
    				   order = 10000,
    				   tab = "团购")
    protected Boolean isGroupOn = false;
    
    @OneToOne(targetEntity = GroupOnImpl.class, mappedBy = "sku")
    @AdminPresentation(friendlyName = "团购信息", 
    				   order = 10000,
    				   tab = "团购",excluded = true)
    protected GroupOn groupOn;
    
    @ManyToMany(targetEntity = MediaImpl.class, cascade={CascadeType.ALL})
    @JoinTable(name = "NPH_SKU_MEDIA_MAP", 
               inverseJoinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "MEDIA_ID"))
    @MapKeyColumn(name = "MAP_KEY")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blProducts")
    @AdminPresentationMap(friendlyName = "SkuImpl_Sku_Media",
        				  tab = ProductImpl.Presentation.Tab.Name.Media, 
        				  tabOrder = ProductImpl.Presentation.Tab.Order.Media,
        				  keyPropertyFriendlyName = "SkuImpl_Sku_Media_Key",
        				  deleteEntityUponRemove = true,
        				  mediaField = "url",
        				  forceFreeFormKeys = true)
    @AdminPresentationMapFields(
        mapDisplayFields = {
            @AdminPresentationMapField(
                    fieldName = "primary",
                    fieldPresentation = @AdminPresentation(fieldType = SupportedFieldType.MEDIA,
                            							   group = ProductImpl.Presentation.Group.Name.General,
                            							   groupOrder = ProductImpl.Presentation.Group.Order.General,
                            							   order = ProductImpl.Presentation.FieldOrder.PRIMARY_MEDIA,
                            							   friendlyName = "SkuImpl_Primary_Media")
            )
    })
    @BatchSize(size = 50)
    @ClonePolicyMap
    protected Map<String, Media> skuMedia = new HashMap<String, Media>();

    /**
     * This relationship will be non-null if and only if this Sku is contained in the list of
     * additional Skus for a Product (for Skus based on ProductOptions)
     */
    @ManyToOne(optional = true, targetEntity = ProductImpl.class, fetch=FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blProducts")
    @AdminPresentation(excluded=true)
    protected Product product;

    @OneToMany(mappedBy = "sku", targetEntity = SkuAttributeImpl.class, cascade = { CascadeType.ALL }, orphanRemoval = true)
    @Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blProducts")
    @MapKey(name="name")
    @BatchSize(size = 50)
    @AdminPresentationMap(friendlyName = "skuAttributesTitle", 
        		          tab = ProductImpl.Presentation.Tab.Name.Advanced, 
        		          tabOrder = ProductImpl.Presentation.Tab.Order.Advanced,
        		          deleteEntityUponRemove = true, 
        		          forceFreeFormKeys = true)
    @ClonePolicyMap
    protected Map<String, SkuAttribute> skuAttributes = new HashMap<String, SkuAttribute>();

    @ManyToMany(targetEntity = ProductOptionValueImpl.class)
    @JoinTable(name = "NPH_SKU_OPTION_VALUE_XREF", 
        joinColumns = @JoinColumn(name = "SKU_ID", referencedColumnName = "SKU_ID"), 
        inverseJoinColumns = @JoinColumn(name = "PRODUCT_OPTION_VALUE_ID",referencedColumnName = "PRODUCT_OPTION_VALUE_ID"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blProducts")
    @BatchSize(size = 50)
    @ClonePolicyCollection(deepClone = false)
    protected List<ProductOptionValue> productOptionValues = new ArrayList<ProductOptionValue>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = SkuFeeImpl.class)
    @JoinTable(name = "NPH_SKU_FEE_XREF",
            joinColumns = @JoinColumn(name = "SKU_ID", referencedColumnName = "SKU_ID", nullable = true),
            inverseJoinColumns = @JoinColumn(name = "SKU_FEE_ID", referencedColumnName = "SKU_FEE_ID", nullable = true))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blProducts")
    @BatchSize(size = 50)
    @ClonePolicyCollection
    protected List<SkuFee> fees = new ArrayList<SkuFee>();

    @ElementCollection
    @CollectionTable(name = "NPH_SKU_FULFILLMENT_FLAT_RATES", 
        joinColumns = @JoinColumn(name = "SKU_ID", referencedColumnName = "SKU_ID", nullable = true))
    @MapKeyJoinColumn(name = "FULFILLMENT_OPTION_ID", referencedColumnName = "FULFILLMENT_OPTION_ID")
    @MapKeyClass(FulfillmentOptionImpl.class)
    @Column(name = "RATE", precision = 19, scale = 5)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blProducts")
    @BatchSize(size = 50)
    @ClonePolicyMap
    protected Map<FulfillmentOption, BigDecimal> fulfillmentFlatRates = new HashMap<FulfillmentOption, BigDecimal>();

    @ManyToMany(targetEntity = FulfillmentOptionImpl.class)
    @JoinTable(name = "NPH_SKU_FULFILLMENT_EXCLUDED",
            joinColumns = @JoinColumn(name = "SKU_ID", referencedColumnName = "SKU_ID"),
            inverseJoinColumns = @JoinColumn(name = "FULFILLMENT_OPTION_ID",referencedColumnName = "FULFILLMENT_OPTION_ID"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blProducts")
    @BatchSize(size = 50)
    @ClonePolicyCollection
    protected List<FulfillmentOption> excludedFulfillmentOptions = new ArrayList<FulfillmentOption>();

    @Column(name = "INVENTORY_TYPE")
    @AdminPresentation(friendlyName = "SkuImpl_Sku_InventoryType",
        			   helpText = "inventoryTypeHelpText",
        			   tooltip = "skuInventoryTypeTooltip",
        			   order = 1000,
                        prominent = true,
                        gridOrder = 9,
        			   tab = ProductImpl.Presentation.Tab.Name.Inventory, 
        			   tabOrder = ProductImpl.Presentation.Tab.Order.Inventory,
        			   group = ProductImpl.Presentation.Group.Name.Inventory, 
        			   groupOrder = ProductImpl.Presentation.Group.Order.Inventory,
        			   fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, 
        			   broadleafEnumeration = "cn.globalph.b2c.inventory.service.type.InventoryType")
    protected String inventoryType;

    @Column(name = "FULFILLMENT_TYPE")
    /*@AdminPresentation(friendlyName = "SkuImpl_Sku_FulfillmentType",
                       order = 2000,
        			   tab = ProductImpl.Presentation.Tab.Name.Inventory, 
        			   tabOrder = ProductImpl.Presentation.Tab.Order.Inventory,
        			   group = ProductImpl.Presentation.Group.Name.Inventory, 
        			   groupOrder = ProductImpl.Presentation.Group.Order.Inventory,
        			   fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, 
        			   broadleafEnumeration = "cn.globalph.b2c.order.service.type.FulfillmentType")*/
    @AdminPresentation(excluded = true)
    protected String fulfillmentType;
    
    @OneToMany(targetEntity= ComparePriceImpl.class, mappedBy="sku")
    @AdminPresentationCollection(
            friendlyName = "比价",
            tab = ProductImpl.Presentation.Tab.Name.Advanced,
            tabOrder = ProductImpl.Presentation.Tab.Order.Advanced,
            order = ProductImpl.Presentation.Group.Order.Advanced,
            addType = AddMethodType.LOOKUP,
            manyToField = "allRoles",
            customCriteria = "includeFriendlyOnly",
            operationTypes = @AdminPresentationOperationTypes(removeType = OperationScope.NONDESTRUCTIVEREMOVE))
    protected List<ComparePrice> comparePrice = new ArrayList<ComparePrice>();
    
    @Column(name = "IS_OFF_LINE")
    @AdminPresentation(friendlyName = "现场活动", 
    				   order = 10000)
    protected boolean isOffline = false;
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isOnSale() {
        Money retailPrice = getRetailPrice();
        Money salePrice = getSalePrice();
        return (salePrice != null && !salePrice.isZero() && salePrice.lessThan(retailPrice));
    }

    @Override
    public Money getProductOptionValueAdjustments() {
        Money optionValuePriceAdjustments = null;
        if (getProductOptionValues() != null) {
            for (ProductOptionValue value : getProductOptionValues()) {
                if (value.getPriceAdjustment() != null) {
                    if (optionValuePriceAdjustments == null) {
                        optionValuePriceAdjustments = value.getPriceAdjustment();
                    } else {
                        optionValuePriceAdjustments = optionValuePriceAdjustments.add(value.getPriceAdjustment());
                    }
                }
            }
        }
        return optionValuePriceAdjustments;
    }

    @Override
    public Money getSalePrice() {
        Money returnPrice = null;
        Money optionValueAdjustments = null;

        if (SkuPricingConsiderationContext.hasDynamicPricing()) {
            // We have dynamic pricing, so we will pull the sale price from there
            if (dynamicPrices == null) {
                DefaultDynamicSkuPricingInvocationHandler handler = new DefaultDynamicSkuPricingInvocationHandler(this);
                Sku proxy = (Sku) Proxy.newProxyInstance(getClass().getClassLoader(), ClassUtils.getAllInterfacesForClass(getClass()), handler);

                dynamicPrices = SkuPricingConsiderationContext.getSkuPricingService().getSkuPrices(proxy, SkuPricingConsiderationContext.getSkuPricingConsiderationContext());
            }
            
            returnPrice = dynamicPrices.getSalePrice();
            optionValueAdjustments = dynamicPrices.getPriceAdjustment();
        } else {
            // We have an explicitly set sale price directly on this entity. We will not apply any adjustments
            returnPrice = new Money(salePrice);
        }
        
        if (optionValueAdjustments != null) {
            returnPrice = returnPrice.add(optionValueAdjustments);
        }

        return returnPrice;
    }

    @Override
    public boolean hasSalePrice() {
        return getSalePrice() != null;
    }


    @Override
    public void setSalePrice(Money salePrice) {
        this.salePrice = Money.toAmount(salePrice);
    }

    @Override
    public Money getRetailPrice() {
        Money tmpRetailPrice = getRetailPriceInternal();
        if (tmpRetailPrice == null) {
            throw new IllegalStateException("Retail price on Sku with id " + getId() + " was null");
        }
        return tmpRetailPrice;
     }

    /*
     * This allows us a way to determine or calculate the retail price. If one is not available this method will return null. 
     * This allows the call to hasRetailPrice() to determine if there is a retail price without the overhead of an exception. 
     */
    protected Money getRetailPriceInternal() {
        Money returnPrice = null;
        Money optionValueAdjustments = null;

        if (SkuPricingConsiderationContext.hasDynamicPricing()) {
            // We have dynamic pricing, so we will pull the retail price from there
            if (dynamicPrices == null) {
                DefaultDynamicSkuPricingInvocationHandler handler = new DefaultDynamicSkuPricingInvocationHandler(this);
                Sku proxy = (Sku) Proxy.newProxyInstance(getClass().getClassLoader(), ClassUtils.getAllInterfacesForClass(getClass()), handler);

                dynamicPrices = SkuPricingConsiderationContext.getSkuPricingService().getSkuPrices(proxy, SkuPricingConsiderationContext.getSkuPricingConsiderationContext());
            }
            
            returnPrice = dynamicPrices.getRetailPrice();
            optionValueAdjustments = dynamicPrices.getPriceAdjustment();
        } else {
            returnPrice = new Money(retailPrice);
        }
         
        
        if (returnPrice != null && optionValueAdjustments != null) {
            returnPrice = returnPrice.add(optionValueAdjustments);
        }
        
        return returnPrice;
    }

    @Override
    public boolean hasRetailPrice() {
        return getRetailPriceInternal() != null;
    }

    @Override
    public void setRetailPrice(Money retailPrice) {
        this.retailPrice = Money.toAmount(retailPrice);
    }

    @Override
    public Money getPrice() {
        return isOnSale() ? getSalePrice() : getRetailPrice();
    }

    @Override
    @Deprecated
    public Money getListPrice() {
        return getRetailPrice();
    }

    @Override
    @Deprecated
    public void setListPrice(Money listPrice) {
        this.retailPrice = Money.toAmount(listPrice);
    }
    @Override
    public Integer getSaleCount() {
		return saleCount;
	}

    @Override
	public void setSaleCount(Integer saleCount) {
		this.saleCount = saleCount;
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
    public String getDescription() {
         return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getLongDescription() {
        return longDescription;
    }

    @Override
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    @Override
    public String getOriginDescription() {
        return originDescription;
    }

    @Override
    public void setOriginDescription(String originDescription) {
        this.originDescription = originDescription;
    }
    
    @Override
    public Boolean isDiscountable() {
        return discountable == 'Y' ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
	public String getParamDescription() {
		return this.paramDescrition;
	}

	@Override
	public void setParamDescription(String paramDescription) {
		this.paramDescrition = paramDescription;
	}

	/*
     * This is to facilitate serialization to non-Java clients
     */
    public Boolean getDiscountable() {
        return isDiscountable();
    }

    @Override
    public void setDiscountable(Boolean discountable) {
        if (discountable == null) {
            this.discountable = null;
        } else {
            this.discountable = discountable ? 'Y' : 'N';
        }
    }

    @Override
    public Boolean isAvailable() {
        if (InventoryType.UNAVAILABLE.equals(getInventoryType())) {
            return false;
        }
        return true;
    }

    @Override
    public Date getActiveStartDate() {
        Date returnDate = null;
        if (SkuActiveDateConsiderationContext.hasDynamicActiveDates()) {
            returnDate = SkuActiveDateConsiderationContext.getSkuActiveDatesService().getDynamicSkuActiveStartDate(this);
           }

        if (returnDate == null) {
            return activeStartDate;
        } else {
            return returnDate;
        }
    }

    @Override
    public void setActiveStartDate(Date activeStartDate) {
        this.activeStartDate = activeStartDate;
    }

    @Override
    public Date getActiveEndDate() {
        Date returnDate = null;
        if (SkuActiveDateConsiderationContext.hasDynamicActiveDates()) {
            returnDate = SkuActiveDateConsiderationContext.getSkuActiveDatesService().getDynamicSkuActiveEndDate(this);
        }

        if (returnDate == null) {
            return activeEndDate;
        } else {
            return returnDate;
        }
    }

    @Override
    public void setActiveEndDate(Date activeEndDate) {
        this.activeEndDate = activeEndDate;
    }

    @Override
    public Dimension getDimension() {
         return dimension;
    }

    @Override
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    @Override
    public Weight getWeight() {
        return weight;
    }

    @Override
    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    @Override
    public boolean isActive() {
        if (LOG.isDebugEnabled()) {
            if (!DateUtil.isActive(getActiveStartDate(), getActiveEndDate(), true)) {
                LOG.debug("sku, " + id + ", inactive due to date");
            }
        }
        return DateUtil.isActive(getActiveStartDate(), getActiveEndDate(), true);
    }

    @Override
    public boolean isActive(Product product, Category category) {
        if (LOG.isDebugEnabled()) {
            if (!DateUtil.isActive(getActiveStartDate(), getActiveEndDate(), true)) {
                LOG.debug("sku, " + id + ", inactive due to date");
            } else if (product != null && !product.isActive()) {
                LOG.debug("sku, " + id + ", inactive due to product being inactive");
            } else if (category != null && !category.isActive()) {
                LOG.debug("sku, " + id + ", inactive due to category being inactive");
            }
        }
        return this.isActive() && (product == null || product.isActive()) && (category == null || category.isActive());
    }

    @Override
    public Map<String, Media> getSkuMedia() {
        return skuMedia;
    }

    @Override
    public void setSkuMedia(Map<String, Media> skuMedia) {
        this.skuMedia = skuMedia;
    }

    @Override
    public Product getProduct() {
        return this.product;
    }

    @Override
    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public List<ProductOptionValue> getProductOptionValues() {
        return productOptionValues;
    }

    @Override
    public void setProductOptionValues(List<ProductOptionValue> productOptionValues) {
        this.productOptionValues = productOptionValues;
    }

    @Override
    @Deprecated
    public Boolean isMachineSortable() {
        return isMachineSortable == null ? false : isMachineSortable;
    }

    @Override
    public Boolean getIsMachineSortable() {
        return isMachineSortable == null ? false : isMachineSortable;
    }

    @Override
    @Deprecated
    public void setMachineSortable(Boolean isMachineSortable) {
        this.isMachineSortable = isMachineSortable;
    }

    @Override
    public void setIsMachineSortable(Boolean isMachineSortable) {
        this.isMachineSortable = isMachineSortable;
    }

    @Override
    public List<SkuFee> getFees() {
        return fees;
    }

    @Override
    public void setFees(List<SkuFee> fees) {
        this.fees = fees;
    }

    @Override
    public Map<FulfillmentOption, BigDecimal> getFulfillmentFlatRates() {
        return fulfillmentFlatRates;
    }

    @Override
    public void setFulfillmentFlatRates(Map<FulfillmentOption, BigDecimal> fulfillmentFlatRates) {
        this.fulfillmentFlatRates = fulfillmentFlatRates;
    }

    @Override
    public List<FulfillmentOption> getExcludedFulfillmentOptions() {
        return excludedFulfillmentOptions;
    }

    @Override
    public void setExcludedFulfillmentOptions(List<FulfillmentOption> excludedFulfillmentOptions) {
        this.excludedFulfillmentOptions = excludedFulfillmentOptions;
    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.getInstance(this.inventoryType);
    }

    @Override
    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = (inventoryType == null) ? null : inventoryType.getType();
    }
    
    @Override
    public Integer getQuantityAvailable() {
        return this.quantityAvailable;
    }
    
    @Override
    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    @Override
    public FulfillmentType getFulfillmentType() {
        return FulfillmentType.getInstance(this.fulfillmentType);
    }

    @Override
    public void setFulfillmentType(FulfillmentType fulfillmentType) {
        this.fulfillmentType = fulfillmentType.getType();
    }

    @Override
    public Map<String, SkuAttribute> getSkuAttributes() {
        return skuAttributes;
    }

    @Override
    public void setSkuAttributes(Map<String, SkuAttribute> skuAttributes) {
        this.skuAttributes = skuAttributes;
    }
    
    @Override
    public void clearDynamicPrices() {
        this.dynamicPrices = null;
    }
   
    @Override
	public List<ComparePrice> getComparePrice() {
		return this.comparePrice;
	}
	
	@Override
	public void setComparePrice(List<ComparePrice> comparePrice) {
		this.comparePrice = comparePrice;
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
        SkuImpl other = (SkuImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!getName().equals(other.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

	@Override
	public boolean getIsGroupOn() {
		return this.isGroupOn;
	}

	@Override
	public void setIsGroupOn(boolean isGroupOn) {
		this.isGroupOn = isGroupOn;
	}

	@Override
	public GroupOn getGroupOn() {
		return this.groupOn;
	}

	@Override
	public void setGroupOn(GroupOn groupOn) {
		this.groupOn = groupOn;
	}

	@Override
	public void setIsSeckilling(boolean isSeckilling) {
		this.isSeckilling = isSeckilling;
	}

	@Override
	public boolean getIsSeckilling() {
		return this.isSeckilling;
	}

	@Override
	public void setSeckillingStartDate(Date seckillingStartDate) {
		this.seckillingStartDate = seckillingStartDate;
	}

	@Override
	public Date getSeckillingStartDate() {
		return this.seckillingStartDate;
	}

	@Override
	public void setSeckillingEndDate(Date seckillingEndDate) {
		this.seckillingEndDate = seckillingEndDate;
	}

	@Override
	public Date getSeckillingEndDate() {
		return this.seckillingEndDate;
	}

	@Override
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	@Override
	public Integer getLimit() {
		return this.limit;
	}
	
	@Override
	public boolean isSeckillingValid(){
		Date current = new Date(System.currentTimeMillis());
		return (current!=null && current.after(seckillingStartDate)) && (seckillingEndDate == null || current.before(seckillingEndDate));
	}
	
	@Override
	public Long getSeckillingStartRemainTime(){
		Long currentTimeMillis = System.currentTimeMillis();
		if(seckillingStartDate != null){
			return seckillingStartDate.getTime() - currentTimeMillis;
		}else{
			return -1L;
		}
	}
	
	@Override
	public Long getSeckillingEndRemainTime(){
		Long currentTimeMillis = System.currentTimeMillis();
		if(seckillingEndDate != null){
			return seckillingEndDate.getTime() - currentTimeMillis;
		}else{
			return -1L;
		}
	}

	@Override
	public boolean getIsOffline() {
		return this.isOffline;
	}

	@Override
	public void setIsOffline(boolean isOffline) {
		this.isOffline = isOffline;	
	}

	@Override
	public boolean isCodeSupport() {
		return this.codeSupport;
	}

	@Override
	public void setCodeSupport(boolean codeSupport) {
		this.codeSupport = codeSupport;	
	}
	
	@Override
	public boolean isPresale() {
		return this.presale;
	}

	@Override
	public void setPresale(boolean presale) {
		this.presale = presale;	
	}

    @Override
    public boolean isPointSupport() {
        return false;
    }

    @Override
    public void setPointCode(boolean pointCode) {
        this.pointSupport = pointCode;
    }
}
