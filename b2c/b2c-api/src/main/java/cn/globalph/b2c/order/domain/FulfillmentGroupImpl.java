package cn.globalph.b2c.order.domain;

import cn.globalph.b2c.offer.domain.CandidateFulfillmentGroupOffer;
import cn.globalph.b2c.offer.domain.CandidateFulfillmentGroupOfferImpl;
import cn.globalph.b2c.offer.domain.FulfillmentGroupAdjustment;
import cn.globalph.b2c.offer.domain.FulfillmentGroupAdjustmentImpl;
import cn.globalph.b2c.order.service.type.FulfillmentGroupStatusType;
import cn.globalph.b2c.order.service.type.FulfillmentType;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.override.AdminPresentationMergeEntry;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverride;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverrides;
import cn.globalph.common.presentation.override.PropertyType;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.AddressImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_FULFILLMENT_GROUP")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
@AdminPresentationMergeOverrides(
    {
        @AdminPresentationMergeOverride(name = "", mergeEntries =
                    @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.READONLY,
                                                    booleanOverrideValue = true)),
        @AdminPresentationMergeOverride(name = "status", mergeEntries =
                    @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.READONLY,
                                                    booleanOverrideValue = false)),                                                   
        @AdminPresentationMergeOverride(name = "personalMessage", mergeEntries = {
                @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.TAB,
                        overrideValue = FulfillmentGroupImpl.Presentation.Tab.Name.Advanced),
                @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.TABORDER,
                        intOverrideValue = FulfillmentGroupImpl.Presentation.Tab.Order.Advanced)
        }),
        @AdminPresentationMergeOverride(name = "address", mergeEntries = {
                @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.TAB,
                        overrideValue = FulfillmentGroupImpl.Presentation.Tab.Name.Address),
                @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.TABORDER,
                        intOverrideValue = FulfillmentGroupImpl.Presentation.Tab.Order.Address)
        }),
        @AdminPresentationMergeOverride(name = "address.isDefault", mergeEntries = {
                @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.EXCLUDED,
                        booleanOverrideValue = true)
        }),
        @AdminPresentationMergeOverride(name = "address.isActive", mergeEntries = {
                @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.EXCLUDED,
                        booleanOverrideValue = true)
        }),
        @AdminPresentationMergeOverride(name = "address.isBusiness", mergeEntries = {
                @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.EXCLUDED,
                        booleanOverrideValue = true)
        })
    }
)
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "FulfillmentGroupImpl_baseFulfillmentGroup")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class FulfillmentGroupImpl implements FulfillmentGroup{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "FulfillmentGroupId")
    @GenericGenerator(
        name="FulfillmentGroupId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FulfillmentGroupImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.order.domain.FulfillmentGroupImpl")
        }
    )
    @Column(name = "FULFILLMENT_GROUP_ID")
    protected Long id;

    @Column(name = "REFERENCE_NUMBER")
    @Index(name="FG_REFERENCE_INDEX", columnNames={"REFERENCE_NUMBER"})
    @AdminPresentation(friendlyName = "FulfillmentGroupImpl_FG_Reference_Number", order=Presentation.FieldOrder.REFNUMBER,
            groupOrder = Presentation.Group.Order.General)
    protected String referenceNumber;
    
    @Column(name = "RETAIL_PRICE", precision=19, scale=5)
    @AdminPresentation(friendlyName = "FulfillmentGroupImpl_Retail_Shipping_Price", order=Presentation.FieldOrder.RETAIL,
            group = Presentation.Group.Name.Pricing, groupOrder = Presentation.Group.Order.Pricing,
            tab = Presentation.Tab.Name.Pricing, tabOrder = Presentation.Tab.Order.Pricing,
            fieldType=SupportedFieldType.MONEY)
    protected BigDecimal retailFulfillmentPrice;

    @Column(name = "SALE_PRICE", precision=19, scale=5)
    @AdminPresentation(friendlyName = "FulfillmentGroupImpl_Sale_Shipping_Price", order=Presentation.FieldOrder.SALE,
            group = Presentation.Group.Name.Pricing, groupOrder = Presentation.Group.Order.Pricing,
            tab = Presentation.Tab.Name.Pricing, tabOrder = Presentation.Tab.Order.Pricing,
            fieldType=SupportedFieldType.MONEY)
    protected BigDecimal saleFulfillmentPrice;

    @Column(name = "PRICE", precision=19, scale=5)
    @AdminPresentation(friendlyName = "FulfillmentGroupImpl_Shipping_Price", order=Presentation.FieldOrder.PRICE,
            group = Presentation.Group.Name.Pricing, groupOrder = Presentation.Group.Order.Pricing,
            tab = Presentation.Tab.Name.Pricing, tabOrder = Presentation.Tab.Order.Pricing,
            fieldType=SupportedFieldType.MONEY)
    protected BigDecimal fulfillmentPrice;

    @Column(name = "TYPE")
    @AdminPresentation(friendlyName = "FulfillmentGroupImpl_FG_Type", order=Presentation.FieldOrder.TYPE,
            fieldType=SupportedFieldType.BROADLEAF_ENUMERATION,
            broadleafEnumeration="cn.globalph.b2c.order.service.type.FulfillmentType",
            prominent = true, gridOrder = 3000)
    protected String type;
    
    @Column(name = "MERCHANDISE_TOTAL", precision=19, scale=5)
    @AdminPresentation(friendlyName = "FulfillmentGroupImpl_FG_Merchandise_Total", order=Presentation.FieldOrder.MERCHANDISETOTAL,
            group = Presentation.Group.Name.Pricing, groupOrder = Presentation.Group.Order.Pricing,
            tab = Presentation.Tab.Name.Pricing, tabOrder = Presentation.Tab.Order.Pricing,
            fieldType=SupportedFieldType.MONEY)
    protected BigDecimal merchandiseTotal;

    @Column(name = "TOTAL", precision=19, scale=5)
    @AdminPresentation(friendlyName = "FulfillmentGroupImpl_FG_Total", order=Presentation.FieldOrder.TOTAL,
            group = Presentation.Group.Name.Pricing, groupOrder = Presentation.Group.Order.Pricing,
            tab = Presentation.Tab.Name.Pricing, tabOrder = Presentation.Tab.Order.Pricing,
            fieldType= SupportedFieldType.MONEY, prominent = true, gridOrder = 2000)
    protected BigDecimal total;

    @Column(name = "STATUS")
    @Index(name="FG_STATUS_INDEX", columnNames={"STATUS"})
    @AdminPresentation(friendlyName = "FulfillmentGroupImpl_FG_Status", order=Presentation.FieldOrder.STATUS,
            fieldType=SupportedFieldType.BROADLEAF_ENUMERATION,
            broadleafEnumeration="cn.globalph.b2c.order.service.type.FulfillmentGroupStatusType",
            prominent = true, gridOrder = 4000)
    protected String status;
    
    @ManyToOne(targetEntity = FulfillmentOptionImpl.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "FULFILLMENT_OPTION_ID")
    protected FulfillmentOption fulfillmentOption;
    
    @ManyToOne(targetEntity = OrderImpl.class, optional=false)
    @JoinColumn(name = "ORDER_ID")
    @Index(name="FG_ORDER_INDEX", columnNames={"ORDER_ID"})
    @AdminPresentation(excluded = true)
    protected Order order;
    
    @Column(name = "FULFILLMENT_GROUP_SEQUNCE")
    protected Integer sequence;

    @ManyToOne(targetEntity = AddressImpl.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "ADDRESS_ID")
    @Index(name="FG_ADDRESS_INDEX", columnNames={"ADDRESS_ID"})
    protected Address address;
        
    @OneToMany(mappedBy = "fulfillmentGroup", targetEntity = FulfillmentGroupItemImpl.class, cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
    @AdminPresentationCollection(friendlyName="FulfillmentGroupImpl_Items",
            tab = Presentation.Tab.Name.Items, tabOrder = Presentation.Tab.Order.Items)
    protected List<FulfillmentGroupItem> fulfillmentGroupItems = new ArrayList<FulfillmentGroupItem>();
    
    @OneToMany(mappedBy = "fulfillmentGroup", targetEntity = FulfillmentGroupFeeImpl.class, cascade = { CascadeType.ALL },
            orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "blOrderElements")
    @AdminPresentationCollection(friendlyName="FulfillmentGroupImpl_Fees",
            tab = Presentation.Tab.Name.Pricing, tabOrder = Presentation.Tab.Order.Pricing)
    protected List<FulfillmentGroupFee> fulfillmentGroupFees = new ArrayList<FulfillmentGroupFee>();
        
    @OneToMany(mappedBy = "fulfillmentGroup", targetEntity = CandidateFulfillmentGroupOfferImpl.class, cascade = { CascadeType.ALL },
            orphanRemoval = true)
    @Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
    protected List<CandidateFulfillmentGroupOffer> candidateOffers = new ArrayList<CandidateFulfillmentGroupOffer>();

    @OneToMany(mappedBy = "fulfillmentGroup", targetEntity = FulfillmentGroupAdjustmentImpl.class, cascade = { CascadeType.ALL },
            orphanRemoval = true)
    @Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
    @AdminPresentationCollection(friendlyName="FulfillmentGroupImpl_Adjustments",
            tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced)
    protected List<FulfillmentGroupAdjustment> fulfillmentGroupAdjustments = new ArrayList<FulfillmentGroupAdjustment>();

    @Column(name = "SHIPPING_OVERRIDE")
    protected Boolean shippingOverride;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
    public FulfillmentOption getFulfillmentOption() {
        return fulfillmentOption;
    }

    @Override
    public void setFulfillmentOption(FulfillmentOption fulfillmentOption) {
        this.fulfillmentOption = fulfillmentOption;
    }

    @Override
    public String getReferenceNumber() {
        return referenceNumber;
    }

    @Override
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Override
    public List<FulfillmentGroupItem> getFulfillmentGroupItems() {
        return fulfillmentGroupItems;
    }
    
    @Override
    public List<OrderItem> getOrderItems() {
        List<OrderItem> discreteOrderItems = new ArrayList<OrderItem>();
        for (FulfillmentGroupItem fgItem : fulfillmentGroupItems) {
            OrderItem orderItem = fgItem.getOrderItem();
            discreteOrderItems.add(orderItem);
           }
        return discreteOrderItems;
    }

    @Override
    public void setFulfillmentGroupItems(List<FulfillmentGroupItem> fulfillmentGroupItems) {
        this.fulfillmentGroupItems = fulfillmentGroupItems;
    }

    @Override
    public void addFulfillmentGroupItem(FulfillmentGroupItem fulfillmentGroupItem) {
        if (this.fulfillmentGroupItems == null) {
            this.fulfillmentGroupItems = new Vector<FulfillmentGroupItem>();
        }
        this.fulfillmentGroupItems.add(fulfillmentGroupItem);

    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public Money getRetailFulfillmentPrice() {
        return retailFulfillmentPrice == null ? null :
                new Money(retailFulfillmentPrice);
    }

    @Override
    public void setRetailFulfillmentPrice(Money retailFulfillmentPrice) {
        this.retailFulfillmentPrice = Money.toAmount(retailFulfillmentPrice);
    }

    @Override
    public FulfillmentType getType() {
        return FulfillmentType.getInstance(type);
    }

    @Override
    public void setType(FulfillmentType type) {
        this.type = type == null ? null : type.getType();
    }

    @Override
    public void addCandidateFulfillmentGroupOffer(CandidateFulfillmentGroupOffer candidateOffer) {
        candidateOffers.add(candidateOffer);
    }

    @Override
    public List<CandidateFulfillmentGroupOffer> getCandidateFulfillmentGroupOffers() {
        return candidateOffers;
    }

    @Override
    public void setCandidateFulfillmentGroupOffer(List<CandidateFulfillmentGroupOffer> candidateOffers) {
        this.candidateOffers = candidateOffers;

    }

    @Override
    public void removeAllCandidateOffers() {
        if (candidateOffers != null) {
            for (CandidateFulfillmentGroupOffer offer : candidateOffers) {
                offer.setFulfillmentGroup(null);
            }
            candidateOffers.clear();
        }
    }

    @Override
    public List<FulfillmentGroupAdjustment> getFulfillmentGroupAdjustments() {
        return this.fulfillmentGroupAdjustments;
    }
    
    @Override
    public Money getFulfillmentGroupAdjustmentsValue() {
        Money adjustmentsValue = Money.ZERO;
        for (FulfillmentGroupAdjustment adjustment : fulfillmentGroupAdjustments) {
            adjustmentsValue = adjustmentsValue.add(adjustment.getValue());
        }
        return adjustmentsValue;
    }

    @Override
    public void removeAllAdjustments() {
        if (fulfillmentGroupAdjustments != null) {
            for (FulfillmentGroupAdjustment adjustment : fulfillmentGroupAdjustments) {
                adjustment.setFulfillmentGroup(null);
            }
            fulfillmentGroupAdjustments.clear();
        }
    }

    @Override
    public void setFulfillmentGroupAdjustments(List<FulfillmentGroupAdjustment> fulfillmentGroupAdjustments) {
        this.fulfillmentGroupAdjustments = fulfillmentGroupAdjustments;
    }

    @Override
    public Money getSaleFulfillmentPrice() {
        return saleFulfillmentPrice == null ? null : new Money(saleFulfillmentPrice);
    }

    @Override
    public void setSaleFulfillmentPrice(Money saleFulfillmentPrice) {
        this.saleFulfillmentPrice = Money.toAmount(saleFulfillmentPrice);
    }

    @Override
    public Money getFulfillmentPrice() {
        return fulfillmentPrice == null ? null : new Money(fulfillmentPrice);
    }

    @Override
    public void setFulfillmentPrice(Money fulfillmentPrice) {
        this.fulfillmentPrice = Money.toAmount(fulfillmentPrice);
    }

    @Override
    public Money getMerchandiseTotal() {
        return merchandiseTotal == null ? null : new Money(merchandiseTotal);
    }

    @Override
    public void setMerchandiseTotal(Money merchandiseTotal) {
        this.merchandiseTotal = Money.toAmount(merchandiseTotal);
    }

    @Override
    public Money getTotal() {
        return total == null ? null : new Money(total);
    }

    @Override
    public void setTotal(Money orderTotal) {
        this.total = Money.toAmount(orderTotal);
    }
    
    @Override
    public FulfillmentGroupStatusType getStatus() {
        return FulfillmentGroupStatusType.getInstance(status);
    }

    @Override
    public void setStatus(FulfillmentGroupStatusType status) {
        this.status = status.getType();
    }

    @Override
    public List<FulfillmentGroupFee> getFulfillmentGroupFees() {
        return fulfillmentGroupFees;
    }

    @Override
    public void setFulfillmentGroupFees(List<FulfillmentGroupFee> fulfillmentGroupFees) {
        this.fulfillmentGroupFees = fulfillmentGroupFees;
    }

    @Override
    public void addFulfillmentGroupFee(FulfillmentGroupFee fulfillmentGroupFee) {
        if (fulfillmentGroupFees == null) {
            fulfillmentGroupFees = new ArrayList<FulfillmentGroupFee>();
        }
        fulfillmentGroupFees.add(fulfillmentGroupFee);
    }

    @Override
    public void removeAllFulfillmentGroupFees() {
        if (fulfillmentGroupFees != null) {
            fulfillmentGroupFees.clear();
        }
    }

    @Override
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Override
    public Integer getSequence() {
        return this.sequence;
    }

    @Override
    public Boolean getShippingOverride() {
        return shippingOverride == null ? false : shippingOverride;
    }

    @Override
    public void setShippingOverride(Boolean shippingOverride) {
        this.shippingOverride = shippingOverride;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((fulfillmentGroupItems == null) ? 0 : fulfillmentGroupItems.hashCode());
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
        FulfillmentGroupImpl other = (FulfillmentGroupImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        if (fulfillmentGroupItems == null) {
            if (other.fulfillmentGroupItems != null) {
                return false;
            }
        } else if (!fulfillmentGroupItems.equals(other.fulfillmentGroupItems)) {
            return false;
        }
        return true;
    }

    public static class Presentation {
        public static class Tab {
            public static class Name {
                public static final String Items = "FulfillmentGroupImpl_Items_Tab";
                public static final String Pricing = "FulfillmentGroupImpl_Pricing_Tab";
                public static final String Address = "FulfillmentGroupImpl_Address_Tab";
                public static final String Advanced = "FulfillmentGroupImpl_Advanced_Tab";
            }

            public static class Order {
                public static final int Items = 2000;
                public static final int Pricing = 3000;
                public static final int Address = 4000;
                public static final int Advanced = 5000;
            }
        }

        public static class Group {
            public static class Name {
                public static final String Pricing = "FulfillmentGroupImpl_Pricing";
            }

            public static class Order {
                public static final int General = 1000;
                public static final int Pricing = 2000;
            }
        }

        public static class FieldOrder {
            public static final int REFNUMBER = 3000;
            public static final int STATUS = 4000;
            public static final int TYPE = 5000;
            public static final int DELIVERINSTRUCTION = 6000;
            public static final int PRIMARY = 7000;
            public static final int PHONE = 8000;

            public static final int RETAIL = 1000;
            public static final int SALE = 2000;
            public static final int PRICE = 3000;
            public static final int ITEMTAX = 4000;
            public static final int FEETAX = 5000;
            public static final int FGTAX = 6000;
            public static final int TOTALTAX = 7000;
            public static final int MERCHANDISETOTAL = 8000;
            public static final int TOTAL = 9000;
            public static final int TAXABLE = 10000;
        }
    }
}
