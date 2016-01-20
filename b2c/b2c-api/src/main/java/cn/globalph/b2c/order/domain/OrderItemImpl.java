package cn.globalph.b2c.order.domain;

import cn.globalph.b2c.offer.domain.CandidateItemOffer;
import cn.globalph.b2c.offer.domain.CandidateItemOfferImpl;
import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OrderItemPriceDetailAdjustment;
import cn.globalph.b2c.order.service.type.OrderItemType;
import cn.globalph.b2c.product.domain.PickupAddress;
import cn.globalph.b2c.product.domain.PickupAddressImpl;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.SkuImpl;
import cn.globalph.b2c.rating.domain.ReviewDetail;
import cn.globalph.b2c.rating.domain.ReviewDetailImpl;
import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.*;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.common.presentation.override.AdminPresentationMergeEntry;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverride;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverrides;
import cn.globalph.common.presentation.override.PropertyType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "NPH_ORDER_ITEM")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
@AdminPresentationMergeOverrides(
    {
        @AdminPresentationMergeOverride(name = "", mergeEntries =
            @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.READONLY,
                                            booleanOverrideValue = true))
    }
)
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "OrderItemImpl_baseOrderItem")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class OrderItemImpl implements OrderItem, Cloneable, AdminMainEntity{

    private static final Log LOG = LogFactory.getLog(OrderItemImpl.class);
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "OrderItemId")
    @GenericGenerator(
        name="OrderItemId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="OrderItemImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.order.domain.OrderItemImpl")
        }
    )
    @Column(name = "ORDER_ITEM_ID")
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @ManyToOne(targetEntity = OrderImpl.class, optional=false)
    @JoinColumn(name = "ORDER_ID")
    @Index(name="ORDERITEM_ORDER_INDEX", columnNames={"ORDER_ID"})
    @AdminPresentation(excluded = true, prominent = false, readOnly = true)
    @AdminPresentationToOneLookup()
    protected Order order;

    @Column(name = "PRICE", precision = 19, scale = 5)
    @AdminPresentation(friendlyName = "OrderItemImpl_Item_Price", order = Presentation.FieldOrder.PRICE,
            group = Presentation.Group.Name.Pricing, groupOrder = Presentation.Group.Order.Pricing,
            fieldType = SupportedFieldType.MONEY, prominent = true, gridOrder = 3000)
    protected BigDecimal price;

    @Column(name = "QUANTITY", nullable = false)
    @AdminPresentation(friendlyName = "OrderItemImpl_Item_Quantity", order = Presentation.FieldOrder.QUANTITY,
            group = Presentation.Group.Name.Pricing, groupOrder = Presentation.Group.Order.Pricing,
            prominent = true, gridOrder = 2000)
    protected int quantity;

    @Column(name = "RETAIL_PRICE", precision=19, scale=5)
    @AdminPresentation(friendlyName = "OrderItemImpl_Item_Retail_Price", order = Presentation.FieldOrder.RETAILPRICE,
            group = Presentation.Group.Name.Pricing, groupOrder = Presentation.Group.Order.Pricing,
            fieldType = SupportedFieldType.MONEY, prominent = true, gridOrder = 4000)
    protected BigDecimal retailPrice;

    @Column(name = "SALE_PRICE", precision=19, scale=5)
    @AdminPresentation(friendlyName = "OrderItemImpl_Item_Sale_Price", order = Presentation.FieldOrder.SALEPRICE,
            group = Presentation.Group.Name.Pricing, groupOrder = Presentation.Group.Order.Pricing,
            fieldType = SupportedFieldType.MONEY)
    protected BigDecimal salePrice;

    @Column(name = "NAME")
    @AdminPresentation(friendlyName = "OrderItemImpl_Item_Name", order=Presentation.FieldOrder.NAME,
            group = Presentation.Group.Name.Description, prominent=true, gridOrder = 1000,
            groupOrder = Presentation.Group.Order.Description)
    protected String name;
    
    @ManyToOne(targetEntity = SkuImpl.class, optional=false)
    @JoinColumn(name = "SKU_ID", nullable = false)
    @Index(name="DISCRETE_SKU_INDEX", columnNames={"SKU_ID"})
    @AdminPresentation(friendlyName = "DiscreteOrderItemImpl_Sku", order=200,
            group = OrderItemImpl.Presentation.Group.Name.Catalog, groupOrder = OrderItemImpl.Presentation.Group.Order.Catalog,excluded=true)
    @AdminPresentationToOneLookup()
    protected Sku sku;
    
    @OneToMany(mappedBy = "orderItem", targetEntity = OrderItemQualifierImpl.class, cascade = { CascadeType.ALL },
            orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "blOrderElements")
    protected List<OrderItemQualifier> orderItemQualifiers = new ArrayList<OrderItemQualifier>();

    @OneToMany(mappedBy = "orderItem", targetEntity = CandidateItemOfferImpl.class, cascade = { CascadeType.ALL },
            orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "blOrderElements")
    protected List<CandidateItemOffer> candidateItemOffers = new ArrayList<CandidateItemOffer>();

    @OneToMany(mappedBy = "orderItem", targetEntity = OrderItemPriceDetailImpl.class, cascade = { CascadeType.ALL },
            orphanRemoval = true)
    @Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
    @AdminPresentationCollection(friendlyName="OrderItemImpl_Price_Details", order = Presentation.FieldOrder.PRICEDETAILS,
                    tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced)
    protected List<OrderItemPriceDetail> orderItemPriceDetails = new ArrayList<OrderItemPriceDetail>();
    
    @Column(name = "ORDER_ITEM_TYPE")
    @Index(name="ORDERITEM_TYPE_INDEX", columnNames={"ORDER_ITEM_TYPE"})
    protected String orderItemType;

    @Column(name = "RETAIL_PRICE_OVERRIDE")
    protected Boolean retailPriceOverride;

    @Column(name = "SALE_PRICE_OVERRIDE")
    protected Boolean salePriceOverride;

    @Column(name = "DISCOUNTS_ALLOWED")
    @AdminPresentation(friendlyName = "OrderItemImpl_Discounts_Allowed", order=Presentation.FieldOrder.DISCOUNTALLOWED,
            tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced)
    protected Boolean discountsAllowed;
    
    @Column(name = "IS_USED")
    @AdminPresentation(friendlyName = "是否结算", order = 7000,
            tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced)
    protected Boolean isUsed = true;

    @Column(name = "IS_REVIEW")
    @AdminPresentation(friendlyName = "是否已评", order = 7000,
            tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced)
    protected Boolean isReview = false;

    @Column(name = "delivery_type")
    @AdminPresentation(friendlyName = "收货方式", order = 7000,
            tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
            prominent = true, gridOrder = 30,
            fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
            broadleafEnumeration = "cn.globalph.b2c.delivery.DeliveryType")
    protected String deliveryType;

    @ManyToOne(targetEntity = PickupAddressImpl.class, optional = true)
    @JoinColumn(name = "pickup_address_id", nullable = true)
    @AdminPresentation(friendlyName = "自提地址", tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,excluded=true)
    @AdminPresentationToOneLookup()
    protected PickupAddress pickupAddress;
    
    @OneToOne(mappedBy = "orderItem", targetEntity = ReviewDetailImpl.class)
    @AdminPresentation(excluded=true)
    protected ReviewDetail reviewDetail;
    
    @OneToOne(mappedBy = "orderItem", targetEntity = RefundImpl.class)
    @AdminPresentation(excluded=true)
    protected Refund refund;
    
    @Override
    public Money getRetailPrice() {
        if (retailPrice == null) {
            updateSaleAndRetailPrices();
        }
        return convertToMoney(retailPrice);
    }

    @Override
    public void setRetailPrice(Money retailPrice) {
        this.retailPrice = Money.toAmount(retailPrice);
    }

    @Override
    public Money getSalePrice() {
        if (salePrice == null) {
            updateSaleAndRetailPrices();
        }
        if (salePrice != null) {
            Money returnPrice = convertToMoney(salePrice);
            if (retailPrice != null && returnPrice.greaterThan(getRetailPrice())) {
                return getRetailPrice();
            } else {
                return returnPrice;
            }
        } else {
            return getRetailPrice();
        }
    }

    @Override
    public void setSalePrice(Money salePrice) {
        this.salePrice = Money.toAmount(salePrice);
    }

    @Override
    public Money getPrice() {
        return getAveragePrice();
    }

    @Override
    public void setPrice(Money finalPrice) {
        setRetailPrice(finalPrice);
        setSalePrice(finalPrice);
        setRetailPriceOverride(true);
        setSalePriceOverride(true);
        setDiscountingAllowed(false);
        this.price = Money.toAmount(finalPrice);
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public List<CandidateItemOffer> getCandidateItemOffers() {
        return candidateItemOffers;
    }

    @Override
    public void setCandidateItemOffers(List<CandidateItemOffer> candidateItemOffers) {
        this.candidateItemOffers = candidateItemOffers;
    }

    @Override
    public Order getOrder() {
        return order;
    }

    @Override
    public void setOrder(Order order) {
        this.order = order;
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<OrderItemQualifier> getOrderItemQualifiers() {
        return this.orderItemQualifiers;
    }

    @Override
    public void setOrderItemQualifiers(List<OrderItemQualifier> orderItemQualifiers) {
        this.orderItemQualifiers = orderItemQualifiers;
    }

    @Override
    public Money getAdjustmentValue() {
        return getAverageAdjustmentValue();
    }

    @Override
    public OrderItemType getOrderItemType() {
        return convertOrderItemType(orderItemType);
    }

    @Override
    public void setOrderItemType(OrderItemType orderItemType) {
        this.orderItemType = orderItemType.getType();
    }

    @Override
    public boolean getIsOnSale() {
        if (getSalePrice() != null) {
            return !getSalePrice().equals(getRetailPrice());
        } else {
            return false;
        }
    }

    @Override
    public boolean getIsDiscounted() {
        if (getPrice() != null) {
            return !getPrice().equals(getRetailPrice());
        } else {
            return false;
        }
    }

    @Override
    public boolean updateSaleAndRetailPrices() {
        if (salePrice == null) {
            salePrice = retailPrice;
        }
        return false;
    }
    
    @Override
    public void finalizePrice() {
        price = getAveragePrice().getAmount();
    }

    @Override
    public void assignFinalPrice() {
        Money finalPrice = getTotalPrice().divide(quantity);
        price = finalPrice.getAmount();
    }

    @Override
    public Money getPriceBeforeAdjustments(boolean allowSalesPrice) {
        boolean retailPriceOverride = false;
        
        for (OrderItemPriceDetail oipd : getOrderItemPriceDetails()) {
            if (oipd.getUseSalePrice() == false) {
                retailPriceOverride = true;
                break;
            }
        }
        
        if (allowSalesPrice && !retailPriceOverride) {
            return getSalePrice();
        } else {
            return getRetailPrice();
        }
    }
    
    @Override
    public void addCandidateItemOffer(CandidateItemOffer candidateItemOffer) {
        getCandidateItemOffers().add(candidateItemOffer);
    }
    
    @Override
    public void removeAllCandidateItemOffers() {
        if (getCandidateItemOffers() != null) {
            for (CandidateItemOffer candidate : getCandidateItemOffers()) {
                candidate.setOrderItem(null);
            }
            getCandidateItemOffers().clear();
        }
    }
    
    @Override
    public void setOrderItemPriceDetails(List<OrderItemPriceDetail> orderItemPriceDetails) {
        this.orderItemPriceDetails = orderItemPriceDetails;
    }

    @Override
    public boolean isDiscountingAllowed() {
        if (discountsAllowed == null) {
            return true;
        } else {
            return discountsAllowed.booleanValue();
        }
    }

    @Override
    public void setDiscountingAllowed(boolean discountsAllowed) {
        this.discountsAllowed = discountsAllowed;
    }

    @Override
    public Money getAveragePrice() {
        if (quantity == 0) {
            return price == null ? null : new Money(price);
        }
        return getTotalPrice().divide(quantity);
     }

    @Override
    public Money getAverageAdjustmentValue() {
        if (quantity == 0) {
            return null;
        }
        return getTotalAdjustmentValue().divide(quantity);
    }

    @Override
    public Money getTotalAdjustmentValue() {
        Money totalAdjustmentValue = Money.ZERO;
        List<OrderItemPriceDetail> priceDetails = getOrderItemPriceDetails();
        if (priceDetails != null) {
            for (OrderItemPriceDetail priceDetail : getOrderItemPriceDetails()) {
                totalAdjustmentValue = totalAdjustmentValue.add(priceDetail.getTotalAdjustmentValue());
            }
        }

        return totalAdjustmentValue;
    }

    @Override
    public Money getTotalPrice() {
        Money returnValue = convertToMoney(BigDecimal.ZERO);
        if (orderItemPriceDetails != null && orderItemPriceDetails.size() > 0) {
            for (OrderItemPriceDetail oipd : orderItemPriceDetails) {
                returnValue = returnValue.add(oipd.getTotalAdjustedPrice());
            }
        } else {
            if (price != null) {
                returnValue = convertToMoney(price).multiply(quantity);
            } else {
                return getSalePrice().multiply(quantity);
            }
        }

        return returnValue;
    }

    @Override
    public Money getTotalPriceBeforeAdjustments(boolean allowSalesPrice) {
        return getPriceBeforeAdjustments(allowSalesPrice).multiply(getQuantity());
    }

    @Override
    public void setRetailPriceOverride(boolean override) {
        this.retailPriceOverride = Boolean.valueOf(override);
    }

    @Override
    public boolean isRetailPriceOverride() {
        if (retailPriceOverride == null) {
            return false;
        } else {
            return retailPriceOverride.booleanValue();
        }
    }

    @Override
    public void setSalePriceOverride(boolean override) {
        this.salePriceOverride = Boolean.valueOf(override);
    }

    @Override
    public boolean isSalePriceOverride() {
        if (salePriceOverride == null) {
            return false;
        } else {
            return salePriceOverride.booleanValue();
        }
    }

    @Override
    public List<OrderItemPriceDetail> getOrderItemPriceDetails() {
        return orderItemPriceDetails;
    }
    
    public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	@Override
    public String getMainEntityName() {
        return getName();
    }

    public void checkCloneable(OrderItem orderItem) throws CloneNotSupportedException, SecurityException, NoSuchMethodException {
        Method cloneMethod = orderItem.getClass().getMethod("clone", new Class[]{});
        if (cloneMethod.getDeclaringClass().getName().startsWith("org.broadleafcommerce") &&
                !orderItem.getClass().getName().startsWith("org.broadleafcommerce")) {
            //subclass is not implementing the clone method
            throw new CloneNotSupportedException("Custom extensions and implementations should implement clone in " +
                    "order to guarantee split and merge operations are performed accurately");
        }
    }

    protected Money convertToMoney(BigDecimal amount) {
        return amount == null ? null : new Money(amount);
    }

    protected OrderItemType convertOrderItemType(String type) {
        return OrderItemType.getInstance(type);
    }
    
    @Override
    public OrderItem clone() {
        //this is likely an extended class - instantiate from the fully qualified name via reflection
        OrderItemImpl clonedOrderItem;
        try {
            clonedOrderItem = (OrderItemImpl) Class.forName(this.getClass().getName()).newInstance();
            try {
                checkCloneable(clonedOrderItem);
            } catch (CloneNotSupportedException e) {
                LOG.warn("Clone implementation missing in inheritance hierarchy outside of Broadleaf: " +
                        clonedOrderItem.getClass().getName(), e);
            }
            if (candidateItemOffers != null) {
                for (CandidateItemOffer candidate : candidateItemOffers) {
                    CandidateItemOffer clone = candidate.clone();
                    clone.setOrderItem(clonedOrderItem);
                    clonedOrderItem.getCandidateItemOffers().add(clone);
                }
            }
            
            clonedOrderItem.setName(name);
            clonedOrderItem.setOrder(order);
            clonedOrderItem.setOrderItemType(convertOrderItemType(orderItemType));
            clonedOrderItem.setQuantity(quantity);
            clonedOrderItem.retailPrice = retailPrice;
            clonedOrderItem.salePrice = salePrice;
            clonedOrderItem.discountsAllowed = discountsAllowed;
            clonedOrderItem.salePriceOverride = salePriceOverride;
            clonedOrderItem.retailPriceOverride = retailPriceOverride;
        } catch (Exception e) {
            throw new RuntimeException(e);
           }
        
        return clonedOrderItem;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((order == null) ? 0 : order.hashCode());
        result = prime * result + ((orderItemType == null) ? 0 : orderItemType.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + quantity;
        result = prime * result + ((retailPrice == null) ? 0 : retailPrice.hashCode());
        result = prime * result + ((salePrice == null) ? 0 : salePrice.hashCode());
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
        OrderItemImpl other = (OrderItemImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }
        
        if (order == null) {
            if (other.order != null) {
                return false;
            }
        } else if (!order.equals(other.order)) {
            return false;
        }
        if (orderItemType == null) {
            if (other.orderItemType != null) {
                return false;
            }
        } else if (!orderItemType.equals(other.orderItemType)) {
            return false;
        }

        if (price == null) {
            if (other.price != null) {
                return false;
            }
        } else if (!price.equals(other.price)) {
            return false;
        }
        if (quantity != other.quantity) {
            return false;
        }
        if (retailPrice == null) {
            if (other.retailPrice != null) {
                return false;
            }
        } else if (!retailPrice.equals(other.retailPrice)) {
            return false;
        }
        if (salePrice == null) {
            if (other.salePrice != null) {
                return false;
            	}
        } else if (!salePrice.equals(other.salePrice)) {
            return false;
           }
        return true;
    }

    public static class Presentation {
        public static class Tab {
            public static class Name {
                public static final String Advanced = "OrderImpl_Advanced";
            }

            public static class Order {
                public static final int Advanced = 2000;
            }
        }

        public static class Group {
            public static class Name {
                public static final String Description = "OrderItemImpl_Description";
                public static final String Pricing = "OrderItemImpl_Pricing";
                public static final String Catalog = "OrderItemImpl_Catalog";
            }

            public static class Order {
                public static final int Description = 1000;
                public static final int Pricing = 2000;
                public static final int Catalog = 3000;
            }
        }

        public static class FieldOrder {
            public static final int NAME = 1000;
            public static final int PRICE = 2000;
            public static final int QUANTITY = 3000;
            public static final int RETAILPRICE = 4000;
            public static final int SALEPRICE = 5000;
            public static final int CATEGORY = 1000;
            public static final int PRICEDETAILS = 1000;
            public static final int ADJUSTMENTS = 2000;
            public static final int DISCOUNTALLOWED = 3000;
        }
    }

    @Override
    public boolean isSkuActive() {
        //abstract method, by default return true
        return true;
    }
    
    public List<Offer> getAppliedOffers(){
    	List<Offer> result = new ArrayList<Offer>();
    	for( OrderItemPriceDetail pd : getOrderItemPriceDetails() ) {
    		for( OrderItemPriceDetailAdjustment pa :pd.getOrderItemPriceDetailAdjustments() )
    			result.add( pa.getOffer() );
    	}
    	return result;
    }

    @Override
	public Boolean isUsed() {
		return isUsed;
	}

    @Override
	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	@Override
	public Boolean isReview() {
		return isReview;
	}

	@Override
	public void setIsReview(Boolean isReview) {
		this.isReview = isReview;
		
	}

    @Override
    public int compareTo(OrderItem o) {
        return this.getSku().getId().compareTo(o.getSku().getId());
    }
    
    @Override
    public void setReviewDetail(ReviewDetail reviewDetail){
    	this.reviewDetail = reviewDetail;
    }
    
    @Override
    public ReviewDetail getReviewDetail(){
    	return this.reviewDetail;
    }

    @Override
    public String getDeliveryType() {
        return deliveryType;
    }

    @Override
    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    @Override
    public PickupAddress getPickupAddress() {
        return pickupAddress;
    }

    @Override
    public void setPickupAddress(PickupAddress pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

	@Override
	public Refund getRefund() {
		return this.refund;
	}

	@Override
	public void setRefund(Refund refund) {
		this.refund = refund;
	}
    
}
