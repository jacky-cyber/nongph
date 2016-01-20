package cn.globalph.b2c.order.domain;

import cn.globalph.b2c.coupon.domain.CouponCode;
import cn.globalph.b2c.coupon.domain.CouponCodeImpl;
import cn.globalph.b2c.offer.domain.*;
import cn.globalph.b2c.order.service.call.ActivityMessageDTO;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.OrderPaymentImpl;
import cn.globalph.b2c.product.domain.GroupOn;
import cn.globalph.b2c.product.domain.GroupOnImpl;
import cn.globalph.b2c.product.domain.Provider;
import cn.globalph.b2c.product.domain.ProviderImpl;
import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.audit.Auditable;
import cn.globalph.common.audit.AuditableListener;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.money.Money;
import cn.globalph.common.persistence.PreviewStatus;
import cn.globalph.common.persistence.Previewable;
import cn.globalph.common.presentation.*;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.coupon.domain.CustomerCoupon;
import cn.globalph.coupon.domain.CustomerCouponImpl;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerImpl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.*;

@Entity
@EntityListeners(value = { AuditableListener.class, OrderPersistedEntityListener.class })
@Table(name = "NPH_ORDER")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
//@AdminPresentationMergeOverrides(
//    {
//        @AdminPresentationMergeOverride(name = "", mergeEntries =
//            @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.READONLY,
//                                            booleanOverrideValue = true))
//    }
//)
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "OrderImpl_baseOrder")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.PREVIEW, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class OrderImpl implements Order, AdminMainEntity, Previewable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    protected Long id;

    @Embedded
    protected Auditable auditable = new Auditable();

    @Embedded
    protected PreviewStatus previewable = new PreviewStatus();

    @Column(name = "NAME")
    @Index(name="ORDER_NAME_INDEX", columnNames={"NAME"})
    @AdminPresentation(friendlyName = "OrderImpl_Order_Name", group = Presentation.Group.Name.General,
            order=Presentation.FieldOrder.NAME, prominent=false, groupOrder = Presentation.Group.Order.General,
            gridOrder = 2000,readOnly=true)
    protected String name;

    @ManyToOne(targetEntity = CustomerImpl.class, optional=false)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    @Index(name="ORDER_CUSTOMER_INDEX", columnNames={"CUSTOMER_ID"})
    @AdminPresentation(friendlyName = "OrderImpl_Customer", group = Presentation.Group.Name.General,
            order = Presentation.FieldOrder.CUSTOMER, groupOrder = Presentation.Group.Order.General, readOnly = true)
    @AdminPresentationToOneLookup()
    protected Customer customer;

    @OneToOne(mappedBy = "order", targetEntity = OrderAddressImpl.class, optional = true)
    @JoinColumn(name = "ORDER_ADDRESS_ID", nullable = true)
    @AdminPresentation(friendlyName = "OrderImpl_Order_Address", tab = Presentation.Tab.Name.OrderAddress,
            tabOrder = Presentation.Tab.Order.OrderAddress)
    protected OrderAddress orderAddress;

    @Column(name = "ORDER_STATUS")
    @Index(name="ORDER_STATUS_INDEX", columnNames={"ORDER_STATUS"})
    @AdminPresentation(friendlyName = "OrderImpl_Order_Status", group = Presentation.Group.Name.General,
            order=Presentation.FieldOrder.STATUS, prominent=true, fieldType=SupportedFieldType.BROADLEAF_ENUMERATION,
            broadleafEnumeration="cn.globalph.b2c.order.service.type.OrderStatus",
            groupOrder = Presentation.Group.Order.General, gridOrder = 3000)
    protected String status;

    @Column(name = "TOTAL_SHIPPING", precision=19, scale=5)
    @AdminPresentation(excluded = true)
    protected BigDecimal totalFulfillmentCharges;

    @Column(name = "ORDER_SUBTOTAL", precision=19, scale=5)
    @AdminPresentation(friendlyName = "OrderImpl_Order_Subtotal", group = Presentation.Group.Name.General,
            order=Presentation.FieldOrder.SUBTOTAL, fieldType=SupportedFieldType.MONEY,
            groupOrder = Presentation.Group.Order.General,readOnly=true)
    protected BigDecimal subTotal;

    @Column(name = "ORDER_TOTAL", precision=19, scale=5)
    @AdminPresentation(friendlyName = "OrderImpl_Order_Total", group = Presentation.Group.Name.General,
            order=Presentation.FieldOrder.TOTAL, fieldType= SupportedFieldType.MONEY, prominent=true,
            groupOrder = Presentation.Group.Order.General,
            gridOrder = 4000,readOnly=true)
    protected BigDecimal total;
    
    @Column(name = "ORDER_COUPON_DISCOUNT", precision=19, scale=5)
    @AdminPresentation(friendlyName = "OrderImpl_Order_Coupon_Discount", group = Presentation.Group.Name.General,
            order = Presentation.FieldOrder.TOTAL, fieldType = SupportedFieldType.MONEY, prominent = false,
            groupOrder = Presentation.Group.Order.General,
            gridOrder = 4000,readOnly=true)
    protected BigDecimal couponDiscount;
    
    @Column(name = "ORDER_COUPON_CODE_DISCOUNT", precision=19, scale=5)
    @AdminPresentation(friendlyName = "优惠码优惠金额", group = Presentation.Group.Name.General,
            order = Presentation.FieldOrder.TOTAL, fieldType = SupportedFieldType.MONEY, prominent = false,
            groupOrder = Presentation.Group.Order.General,
            gridOrder = 4000,readOnly=true)
    protected BigDecimal couponCodeDiscount;

    /*add jenny 15/09/14 s*/
    @Column(name = "ORDER_DEDUCTIONBONUS", precision = 19, scale = 5)
    @AdminPresentation(friendlyName = "抵扣积分数", group = Presentation.Group.Name.General,
                order = Presentation.FieldOrder.TOTAL, fieldType = SupportedFieldType.MONEY, prominent = false,
                groupOrder = Presentation.Group.Order.General,
                gridOrder = 4000, readOnly = true)
    protected Integer deductionBonusPoint = 0;
    /*add jenny 15/09/14 e*/

    @Column(name = "SUBMIT_DATE")
    @AdminPresentation(friendlyName = "OrderImpl_Order_Submit_Date", group = Presentation.Group.Name.General,
            order=Presentation.FieldOrder.SUBMITDATE, groupOrder = Presentation.Group.Order.General, prominent = true,
            gridOrder = 5000,readOnly=true)
    protected Date submitDate;

    @Column(name = "confirm_date")
    @AdminPresentation(friendlyName = "OrderImpl_Order_Confirm_Date", group = Presentation.Group.Name.General,
            order=Presentation.FieldOrder.CONFIRMDATE, groupOrder = Presentation.Group.Order.General, prominent = true,
            gridOrder = 5001,readOnly=true)
    protected Date confirmDate;
    
    @Column(name = "COMPLETE_DATE")
    @AdminPresentation(friendlyName = "订单完成时间", group = Presentation.Group.Name.General,
            order=Presentation.FieldOrder.CONFIRMDATE, groupOrder = Presentation.Group.Order.General, prominent = true,
            gridOrder = 5002,readOnly=true)
    protected Date completeDate;

    @Column(name = "IS_REVIEW")
    protected Boolean isReview = false;

    @Column(name = "is_delivery_info_sent")
    @AdminPresentation(friendlyName = "已发清单", group = Presentation.Group.Name.General,
            order = Presentation.FieldOrder.SUBMITDATE, groupOrder = Presentation.Group.Order.General, prominent = true,
            gridOrder = 5100)
    protected Boolean isDeliveryInfoSent = false;

    @Column(name = "is_shipped")
    @AdminPresentation(friendlyName = "已发货", group = Presentation.Group.Name.General,
            order = Presentation.FieldOrder.SUBMITDATE, groupOrder = Presentation.Group.Order.General, prominent = true,
            gridOrder = 5200)
    protected Boolean isShipped = false;

    @Column(name = "delivery_type")
    @AdminPresentation(friendlyName = "配送方式", order = 7000,
            tab = "OrderImpl_Order_Address_Tab", group = "OrderImpl_Express",
            prominent = true, gridOrder = 30,
            fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
            broadleafEnumeration = "cn.globalph.b2c.delivery.DeliveryType")
    protected String deliveryType;

    @Column(name = "ORDER_NUMBER")
    @Index(name="ORDER_NUMBER_INDEX", columnNames={"ORDER_NUMBER"})
    @AdminPresentation(friendlyName = "OrderImpl_Order_Number", group = Presentation.Group.Name.General,
            order=Presentation.FieldOrder.ORDERNUMBER, prominent=true, groupOrder = Presentation.Group.Order.General,
            gridOrder = 1, readOnly = true)
    private String orderNumber;

    @OneToOne(targetEntity = CustomerCouponImpl.class, optional = true)
    @JoinColumn(name = "APPLY_COUPON_ID", nullable = true)
    @AdminPresentation(excluded=true)
    protected CustomerCoupon applyCoupon;
    
    @OneToOne(targetEntity = CouponCodeImpl.class, optional = true)
    @JoinColumn(name = "APPLY_COUPON_CODE_ID", nullable = true)
    @AdminPresentation(excluded=true)
    protected CouponCode couponCode;
    
    @OneToOne(targetEntity = ProviderImpl.class, optional = true)
    @JoinColumn(name = "PROVIDER_ID", nullable = true)
    @AdminPresentation(excluded=true)
    protected Provider provider;

    @ManyToOne(targetEntity = OrderImpl.class)
    @JoinColumn(name = "PARENT_ORDER_ID")
    protected Order order;

    @OneToMany(mappedBy = "order", targetEntity = OrderImpl.class)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "blOrderElements")
    @AdminPresentationCollection(friendlyName = "OrderImpl_Suborders",
            tab = Presentation.Tab.Name.OrderItems, tabOrder = Presentation.Tab.Order.OrderItems)
    protected List<Order> suborderList = new ArrayList<>();

    @OneToMany(mappedBy = "order", targetEntity = OrderItemImpl.class, cascade = {CascadeType.ALL})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
    @AdminPresentationCollection(friendlyName="OrderImpl_Order_Items",
            tab = Presentation.Tab.Name.OrderItems, tabOrder = Presentation.Tab.Order.OrderItems,readOnly=true)
    protected List<OrderItem> orderItems = new ArrayList<OrderItem>();

    @OneToMany(mappedBy = "order", targetEntity = FulfillmentGroupImpl.class, cascade = {CascadeType.ALL})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
//    @AdminPresentationCollection(friendlyName="OrderImpl_Fulfillment_Groups",
//                tab = Presentation.Tab.Name.FulfillmentGroups, tabOrder = Presentation.Tab.Order.FulfillmentGroups)
    protected List<FulfillmentGroup> fulfillmentGroups = new ArrayList<FulfillmentGroup>();

    @OneToMany(mappedBy = "order", targetEntity = OrderAdjustmentImpl.class, cascade = { CascadeType.ALL },
            orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
    @AdminPresentationCollection(friendlyName="OrderImpl_Adjustments",
                tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
                order = Presentation.FieldOrder.ADJUSTMENTS,readOnly=true)
    protected List<OrderAdjustment> orderAdjustments = new ArrayList<OrderAdjustment>();

    @OneToMany(mappedBy = "order", targetEntity = CandidateOrderOfferImpl.class, cascade = { CascadeType.ALL },
            orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
    protected List<CandidateOrderOffer> candidateOrderOffers = new ArrayList<CandidateOrderOffer>();

    @OneToMany(mappedBy = "order", targetEntity = OrderPaymentImpl.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
    @AdminPresentationCollection(friendlyName="OrderImpl_Payments",
                tab = Presentation.Tab.Name.Payment, tabOrder = Presentation.Tab.Order.Payment,readOnly=true)
    protected List<OrderPayment> payments = new ArrayList<OrderPayment>();

    @OneToMany(mappedBy = "order", targetEntity = OrderLogImpl.class)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "blOrderElements")
    @AdminPresentationCollection(friendlyName = "OrderImpl_OrderLogs",
            tab = Presentation.Tab.Name.OrderLogs, tabOrder = Presentation.Tab.Order.OrderLogs)
    protected List<OrderLog> orderLogs = new ArrayList<OrderLog>();

    @ManyToMany(targetEntity=OfferInfoImpl.class)
    @JoinTable(name = "NPH_ADDITIONAL_OFFER_INFO", joinColumns = @JoinColumn(name = "BLC_ORDER_ORDER_ID",
            referencedColumnName = "ORDER_ID"), inverseJoinColumns = @JoinColumn(name = "OFFER_INFO_ID",
            referencedColumnName = "OFFER_INFO_ID"))
    @MapKeyJoinColumn(name = "OFFER_ID")
    @MapKeyClass(OfferImpl.class)
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
    @BatchSize(size = 50)
    protected Map<Offer, OfferInfo> additionalOfferInformation = new HashMap<Offer, OfferInfo>();

    @OneToOne(mappedBy = "order", targetEntity = RefundImpl.class)
    @AdminPresentation(excluded=true)
    protected Refund refund;
    
    @Transient
    protected List<ActivityMessageDTO> orderMessages;

    @Transient
    private Money pickupOff;

    @Column(name = "IS_GROUP_ON")
    @AdminPresentation(friendlyName = "是否团购")
    private Boolean isGroupOn = false;
    
    @ManyToOne(targetEntity = GroupOnImpl.class)
    @JoinColumn(name = "GROUP_ON_ID")
    @AdminPresentation(friendlyName = "团购活动")
    @AdminPresentationToOneLookup
    private GroupOn groupOn;

    /*add by jenny s*/
    @Column(name = "REMARK")
    private String remark;
    /*end*/
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Auditable getAuditable() {
        return auditable;
    }

    @Override
    public void setAuditable(Auditable auditable) {
        this.auditable = auditable;
    }

    @Override
    public Money getSubTotal() {
        return subTotal == null ? null : new Money(subTotal);
    }

    @Override
    public void setSubTotal(Money subTotal) {
        this.subTotal = Money.toAmount(subTotal);
    }
    
    /**
     * 订单总额，将订单项相加
     */
    @Override
    public Money calculateSubTotal() {
        Money calculatedSubTotal = Money.ZERO;
        for (OrderItem orderItem : orderItems) {
        	if(!orderItem.isUsed()) continue;
            calculatedSubTotal = calculatedSubTotal.add(orderItem.getTotalPrice());
        }
        return calculatedSubTotal;
     }

    @Override
    public void assignOrderItemsFinalPrice() {
        for (OrderItem orderItem : orderItems) {
        	if(!orderItem.isUsed()) continue;
            orderItem.assignFinalPrice();
        }
    }

    @Override
    public Money getTotal() {
        return total == null ? null : new Money(total);
    }

    @Override
    public Money getTotalAfterAppliedPayments() {
        Money myTotal = getTotal();
        if (myTotal == null) {
            return null;
        }
        Money totalPayments = Money.ZERO;
        for (OrderPayment payment : getPayments()) {
            if (payment.isActive() && payment.getAmount() != null) {
                totalPayments = totalPayments.add(payment.getAmount());
            }
        }
        return myTotal.subtract(totalPayments);
    }

    @Override
    public void setTotal(Money orderTotal) {
        this.total = Money.toAmount(orderTotal);
    }

    @Override
    public Boolean getPreview() {
        if (previewable == null) {
            previewable = new PreviewStatus();
        }
        return previewable.getPreview();
    }

    @Override
    public void setPreview(Boolean preview) {
        if (previewable == null) {
            previewable = new PreviewStatus();
        }
        previewable.setPreview(preview);
    }

    @Override
    public Date getSubmitDate() {
        return submitDate;
    }

    @Override
    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    @Override
    public Date getConfirmDate() {
        return confirmDate;
    }

    @Override
    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }
    
    @Override
    public Date getCompleteDate() {
        return completeDate;
    }

    @Override
    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public OrderAddress getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(OrderAddress orderAddress) {
        this.orderAddress = orderAddress;
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.getInstance(status);
    }

    @Override
    public void setStatus(OrderStatus status) {
        this.status = status.getType();
    }

    @Override
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    @Override
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    @Override
    public List<FulfillmentGroup> getFulfillmentGroups() {
        return fulfillmentGroups;
    }

    @Override
    public void setFulfillmentGroups(List<FulfillmentGroup> fulfillmentGroups) {
        this.fulfillmentGroups = fulfillmentGroups;
    }

    @Override
    public void setCandidateOrderOffers(List<CandidateOrderOffer> candidateOrderOffers) {
        this.candidateOrderOffers = candidateOrderOffers;
    }

    @Override
    public List<CandidateOrderOffer> getCandidateOrderOffers() {
        return candidateOrderOffers;
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
    public Money getTotalFulfillmentCharges() {
        return totalFulfillmentCharges == null ? null : new Money(totalFulfillmentCharges);
    }

    @Override
    public void setTotalFulfillmentCharges(Money totalFulfillmentCharges) {
        this.totalFulfillmentCharges = Money.toAmount(totalFulfillmentCharges);
    }

    @Override
    public List<OrderPayment> getPayments() {
        return payments;
    }

    @Override
    public void setPayments(List<OrderPayment> payments) {
        this.payments = payments;
    }
    
    @Override
    public List<OrderAdjustment> getOrderAdjustments() {
        return this.orderAdjustments;
    }

    protected void setOrderAdjustments(List<OrderAdjustment> orderAdjustments) {
        this.orderAdjustments = orderAdjustments;
    }

    @Override
    public boolean containsSku(Long skuId) {
        for (OrderItem orderItem : getOrderItems()) {
            if (orderItem.getSku() != null && orderItem.getSku().getId().equals(skuId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getOrderNumber() {
        return orderNumber;
    }

    @Override
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String getFulfillmentStatus() {
        return null;
    }

    @Override
    public Map<Offer, OfferInfo> getAdditionalOfferInformation() {
        return additionalOfferInformation;
    }

    @Override
    public void setAdditionalOfferInformation(Map<Offer, OfferInfo> additionalOfferInformation) {
        this.additionalOfferInformation = additionalOfferInformation;
    }

    @Override
    public Money getItemAdjustmentsValue() {
        Money itemAdjustmentsValue = Money.ZERO;
        for (OrderItem orderItem : orderItems) {
            itemAdjustmentsValue = itemAdjustmentsValue.add(orderItem.getTotalAdjustmentValue());
        }
        return itemAdjustmentsValue;
    }
    
    @Override
    public Money getFulfillmentGroupAdjustmentsValue() {
        Money adjustmentValue = Money.ZERO;
        for (FulfillmentGroup fulfillmentGroup : fulfillmentGroups) {
            adjustmentValue = adjustmentValue.add(fulfillmentGroup.getFulfillmentGroupAdjustmentsValue());
        }
        return adjustmentValue;
    }

    @Override
    public Money getOrderAdjustmentsValue() {
        Money orderAdjustmentsValue = Money.ZERO;
        for (OrderAdjustment orderAdjustment : orderAdjustments) {
            orderAdjustmentsValue = orderAdjustmentsValue.add(orderAdjustment.getValue());
           }
        return orderAdjustmentsValue;
     }

    @Override
    public Money getTotalAdjustmentsValue() {
        Money totalAdjustmentsValue = getItemAdjustmentsValue();
        totalAdjustmentsValue = totalAdjustmentsValue.add(getOrderAdjustmentsValue());
        totalAdjustmentsValue = totalAdjustmentsValue.add(getFulfillmentGroupAdjustmentsValue());
        return totalAdjustmentsValue;
     }

    @Override
    public boolean updatePrices() {
        boolean updated = false;
        for (OrderItem orderItem : orderItems) {
            if (orderItem.updateSaleAndRetailPrices()) {
                updated = true;
            }
        }
        return updated;
     }

    @Override
    public boolean finalizeItemPrices() {
        boolean updated = false;
        for (OrderItem orderItem : orderItems) {
            orderItem.finalizePrice();
        }
        return updated;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (OrderItem doi : getOrderItems()) {
            count += doi.getQuantity();
           }
        return count;
    }

    @Override
    public boolean getHasOrderAdjustments() {
        Money orderAdjustmentsValue = getOrderAdjustmentsValue();
        if (orderAdjustmentsValue != null) {
            return (orderAdjustmentsValue.compareTo(BigDecimal.ZERO) != 0);
        }
        return false;
    }

    @Override
    public String getMainEntityName() {
        String customerName = null;
        String orderNumber = getOrderNumber();
        if (!StringUtils.isEmpty(getCustomer().getName())) {
            customerName = getCustomer().getName();
        }
        if (!StringUtils.isEmpty(orderNumber) && !StringUtils.isEmpty(customerName)) {
            return orderNumber + " - " + customerName;
        }
        if (!StringUtils.isEmpty(orderNumber)) {
            return orderNumber;
        }
        if (!StringUtils.isEmpty(customerName)) {
            return customerName;
        }
        return "";
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
        OrderImpl other = (OrderImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (customer == null) {
            if (other.customer != null) {
                return false;
            }
        } else if (!customer.equals(other.customer)) {
            return false;
        }
        Date myDateCreated = auditable != null ? auditable.getDateCreated() : null;
        Date otherDateCreated = other.auditable != null ? other.auditable.getDateCreated() : null;
        if (myDateCreated == null) {
            if (otherDateCreated != null) {
                return false;
            }
        } else if (!myDateCreated.equals(otherDateCreated)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((customer == null) ? 0 : customer.hashCode());
        Date myDateCreated = auditable != null ? auditable.getDateCreated() : null;
        result = prime * result + ((myDateCreated == null) ? 0 : myDateCreated.hashCode());
        return result;
    }

    @Override
    public List<ActivityMessageDTO> getOrderMessages() {
        if (this.orderMessages == null) {
            this.orderMessages = new ArrayList<ActivityMessageDTO>();
        }
        return this.orderMessages;
    }

    @Override
    public void setOrderMessages(List<ActivityMessageDTO> orderMessages) {
        this.orderMessages = orderMessages;
    }
    
    @Override
    public int getUsedItemsNum(){
    	if(orderItems == null) return 0;
    	int n = 0;
    	for(OrderItem orderItem : orderItems){
    		if(orderItem.isUsed()) n++;
    	}
    	return n;
    }

    public static class Presentation {
        public static class Tab {
            public static class Name {
                //                public static final String Suborder = "OrderImpl_Suborder_Tab";
                public static final String OrderAddress = "OrderImpl_Order_Address_Tab";
                public static final String OrderItems = "OrderImpl_Order_Items_Tab";
                //                public static final String FulfillmentGroups = "OrderImpl_Fulfillment_Groups_Tab";
                public static final String OrderLogs = "OrderImpl_Order_Logs_Tab";
                public static final String Payment = "OrderImpl_Payment_Tab";
                public static final String Advanced = "OrderImpl_Advanced_Tab";
            }

            public static class Order {
                public static final int OrderItems = 2000;
                public static final int OrderLogs = 3000;
                public static final int OrderAddress = 3000;
                public static final int FulfillmentGroups = 3000;
                public static final int Payment = 4000;
                public static final int Advanced = 5000;
            }
        }

        public static class Group {
            public static class Name {
                public static final String General = "OrderImpl_Order";
            }

            public static class Order {
                public static final int General = 1000;
            }
        }

        public static class FieldOrder {
            public static final int NAME = 1000;
            public static final int CUSTOMER = 6000;
            public static final int TOTAL = 3000;
            public static final int STATUS = 1000;
            public static final int SUBTOTAL = 5000;
            public static final int ORDERNUMBER = 2000;
            public static final int TOTALTAX = 7000;
            public static final int TOTALFGCHARGES = 8000;
            public static final int SUBMITDATE = 9000;
            public static final int CONFIRMDATE = 9001;
            public static final int EMAILADDRESS = 10000;
            public static final int ADDRESS = 10000;

            public static final int ADJUSTMENTS = 1000;
            public static final int OFFERCODES = 2000;
        }
    }

	@Override
	public CustomerCoupon getApplyCoupon() {
		return this.applyCoupon;
	}

	@Override
	public void setApplyCoupon(CustomerCoupon coupon) {
		this.applyCoupon = coupon;	
	}

	@Override
	public List<OrderItem> getUsedOrderItems() {
		List<OrderItem> usedOrderItems = new ArrayList<OrderItem>();
		for(OrderItem orderItem : orderItems){
			if(orderItem.isUsed()){
				usedOrderItems.add(orderItem);
			}
		}
		return usedOrderItems;
	}
	
	@Override
	public Money getCouponDiscount() {
		return couponDiscount == null ? Money.ZERO : new Money(couponDiscount);
	}
	
	@Override
	public void setCouponDiscount(Money couponDiscount){
		 this.couponDiscount = Money.toAmount(couponDiscount);
	 }

	@Override
	public boolean isReview() {
        return this.isReview;
    }

	@Override
    public void setReview(boolean isReview) {
        this.isReview = isReview;

    }

    @Override
    public Boolean isDeliveryInfoSent() {
        return this.isDeliveryInfoSent;
    }

    @Override
    public void setDeliveryInfoSent(Boolean isDeliveryInfoSent) {
        this.isDeliveryInfoSent = isDeliveryInfoSent;
    }

    @Override
    public Boolean isShipped() {
        return isShipped;
    }

    @Override
    public void setShipped(Boolean isShipped) {
        this.isShipped = isShipped;
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
    public List<Order> getSuborderList() {
        return suborderList;
    }

    @Override
    public void setSuborderList(List<Order> suborderList) {
        this.suborderList = suborderList;
    }

    @Override
    public List<OrderLog> getOrderLogs() {
        return orderLogs;
    }

    @Override
    public void setOrderLogs(List<OrderLog> orderLogs) {
        this.orderLogs = orderLogs;
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
    public Money getPickupOff() {
        return this.pickupOff;
    }

    @Override
    public void setPickupOff(Money money) {
        this.pickupOff = money;
    }

    @Override
    public Provider getProvider() {
        return provider;
    }

    @Override
    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public Boolean isRefundOrderAvailable() {
    	
    	if(getRefund() != null){
    		return false;
    	}
    	
    	if(getStatus().equals(OrderStatus.CONFIRMED) && !isShipped()){
    		return true;
    	}
    	
    	return false;
	}
    
    @Override
    public Boolean isRefundAvailable() {
    	boolean refundAvailable = false;
    	
    	if(getStatus().equals(OrderStatus.CONFIRMED) && isShipped){
    		refundAvailable = true;
    	}
    	
    	if(getStatus().equals(OrderStatus.COMPLETED)){
    		if(completeDate != null){
    			Long time = System.currentTimeMillis() - completeDate.getTime();
    			long _24h = 24 * 3600 * 1000;
    			if(time < _24h){
    				refundAvailable = true;
    			}
    		}
    	}
    	
    	if(refundAvailable){
    		for(OrderItem oi : getOrderItems()){
				if(oi.getRefund() != null){
					return false;
				}
			}
    	}
    	
    	return refundAvailable;
	}
	
	@Override
	public Boolean getHasRefundItem() {
		for(OrderItem oi : getOrderItems()){
			if(oi.getRefund() != null){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Boolean getIsGroupOn() {
		return this.isGroupOn;
	}

	@Override
	public void setIsGroupOn(Boolean isGroupOn) {
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
	public boolean isCouponAvailable() {
		List<OrderItem> uois = getUsedOrderItems();
		if(uois != null) {
			for(OrderItem orderItem : uois){
				if(orderItem.getSku().getIsSeckilling()){
					return false;
				}
				
			}
		}
		
		return true;
	}
	
	@Override
	public boolean isCouponCodeAvailable() {
		List<OrderItem> uois = getUsedOrderItems();
		if(uois != null) {
			for(OrderItem orderItem : uois){
				if(!orderItem.getSku().isCodeSupport()){
					return false;
				}
				
			}
		}
		
		return true;
	}
	
	@Override
	public boolean getNeedAddress() {
		List<OrderItem> usedOrderItems = getUsedOrderItems();
		for(OrderItem usedOrderItem : usedOrderItems){
			if(!usedOrderItem.getSku().getIsOffline()){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public CouponCode getCouponCode() {
		return this.couponCode;
	}

	@Override
	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
	}

    /*add by jenny s*/
    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String getRemark() {
        return this.remark;
    }

    @Override
    public void setDeductionBonusPoint(Integer deductionBonusPoint) {
        this.deductionBonusPoint = deductionBonusPoint;
    }

    @Override
    public Integer getDeductionBonusPoint() {
        return deductionBonusPoint;
    }

    /*end*/
    @Override
	public Money getCouponCodeDiscount() {
		return couponCodeDiscount == null ? Money.ZERO : new Money(couponCodeDiscount); 
	}

	@Override
	public void setCouponCodeDiscount(Money couponCodeDiscount) {
		this.couponCodeDiscount = Money.toAmount(couponCodeDiscount);
	}

	@Override
	public void setRefund(Refund refund) {
		this.refund = refund;
	}

	@Override
	public Refund getRefund() {
		return this.refund;
	}

}
