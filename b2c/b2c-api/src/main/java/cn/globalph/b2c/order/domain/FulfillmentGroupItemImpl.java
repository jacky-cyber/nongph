package cn.globalph.b2c.order.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.order.service.type.FulfillmentGroupStatusType;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationToOneLookup;
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

import java.lang.reflect.Method;
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
@Table(name = "NPH_FULFILLMENT_GROUP_ITEM")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
@AdminPresentationMergeOverrides(
    {
        @AdminPresentationMergeOverride(name = "", mergeEntries =
            @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.READONLY,
                                            booleanOverrideValue = true))
    }
)
public class FulfillmentGroupItemImpl implements FulfillmentGroupItem, Cloneable {

    private static final Log LOG = LogFactory.getLog(FulfillmentGroupItemImpl.class);
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "FulfillmentGroupItemId")
    @GenericGenerator(
        name="FulfillmentGroupItemId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FulfillmentGroupItemImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.order.domain.FulfillmentGroupItemImpl")
        }
    )
    @Column(name = "FULFILLMENT_GROUP_ITEM_ID")
    protected Long id;

    @ManyToOne(targetEntity = FulfillmentGroupImpl.class, optional=false)
    @JoinColumn(name = "FULFILLMENT_GROUP_ID")
    @Index(name="FGITEM_FG_INDEX", columnNames={"FULFILLMENT_GROUP_ID"})
    protected FulfillmentGroup fulfillmentGroup;

    //this needs to stay OrderItem in order to provide backwards compatibility for those implementations that place a BundleOrderItem
    @ManyToOne(targetEntity = OrderItemImpl.class, optional=false)
    @JoinColumn(name = "ORDER_ITEM_ID")
    @AdminPresentation(friendlyName = "FulfillmentGroupItemImpl_Order_Item", prominent = true, order = 1000, gridOrder = 1000)
    @AdminPresentationToOneLookup()
    protected OrderItem orderItem;

    @Column(name = "QUANTITY", nullable=false)
    @AdminPresentation(friendlyName = "FulfillmentGroupItemImpl_Quantity", prominent = true, order = 2000, gridOrder = 2000)
    protected int quantity;

    @Column(name = "STATUS")
    @Index(name="FGITEM_STATUS_INDEX", columnNames={"STATUS"})
    @AdminPresentation(friendlyName = "FulfillmentGroupItemImpl_Status", prominent = true, order = 3000, gridOrder = 3000)
    private String status;

    @Column(name = "TOTAL_ITEM_AMOUNT", precision = 19, scale = 5)
    @AdminPresentation(friendlyName = "FulfillmentGroupItemImpl_Total_Item_Amount", order = 5000, fieldType = SupportedFieldType.MONEY)
    protected BigDecimal totalItemAmount;

    @Column(name = "PRORATED_ORDER_ADJ")
    @AdminPresentation(friendlyName = "FulfillmentGroupItemImpl_Prorated_Adjustment", order = 7000, fieldType = SupportedFieldType.MONEY)
    protected BigDecimal proratedOrderAdjustment;

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
    public OrderItem getOrderItem() {
        return orderItem;
    }

    @Override
    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
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
    public Money getRetailPrice() {
        return orderItem.getRetailPrice();
    }

    @Override
    public Money getSalePrice() {
        return orderItem.getSalePrice();
    }

    protected Money convertToMoney(BigDecimal amount) {
        return amount == null ? null : new Money(amount);
    }

    @Override
    public Money getTotalItemAmount() {
        return convertToMoney(totalItemAmount);
    }

    @Override
    public void setTotalItemAmount(Money amount) {
        totalItemAmount = amount.getAmount();
    }

    @Override
    public Money getProratedOrderAdjustmentAmount() {
        return convertToMoney(proratedOrderAdjustment);
    }

    @Override
    public void setProratedOrderAdjustmentAmount(Money proratedOrderAdjustment) {
        this.proratedOrderAdjustment = proratedOrderAdjustment.getAmount();
    }

    @Override
    public FulfillmentGroupStatusType getStatus() {
        return FulfillmentGroupStatusType.getInstance(this.status);
    }

    @Override
    public void setStatus(FulfillmentGroupStatusType status) {
        this.status = status.getType();
    }
    
    @Override
    public void removeAssociations() {
        if (getFulfillmentGroup() != null) {
            getFulfillmentGroup().getFulfillmentGroupItems().remove(this);
        }
        setFulfillmentGroup(null);
        setOrderItem(null);
    }

    public void checkCloneable(FulfillmentGroupItem fulfillmentGroupItem) throws CloneNotSupportedException, SecurityException, NoSuchMethodException {
        Method cloneMethod = fulfillmentGroupItem.getClass().getMethod("clone", new Class[]{});
        if (cloneMethod.getDeclaringClass().getName().startsWith("org.broadleafcommerce") && !orderItem.getClass().getName().startsWith("org.broadleafcommerce")) {
            //subclass is not implementing the clone method
            throw new CloneNotSupportedException("Custom extensions and implementations should implement clone in order to guarantee split and merge operations are performed accurately");
        }
    }

    @Override
    public FulfillmentGroupItem clone() {
        //this is likely an extended class - instantiate from the fully qualified name via reflection
        FulfillmentGroupItem clonedFulfillmentGroupItem;
        try {
            clonedFulfillmentGroupItem = (FulfillmentGroupItem) Class.forName(this.getClass().getName()).newInstance();
            try {
                checkCloneable(clonedFulfillmentGroupItem);
            } catch (CloneNotSupportedException e) {
                LOG.warn("Clone implementation missing in inheritance hierarchy outside of Broadleaf: " + clonedFulfillmentGroupItem.getClass().getName(), e);
            }

            clonedFulfillmentGroupItem.setFulfillmentGroup(getFulfillmentGroup());
            clonedFulfillmentGroupItem.setOrderItem(getOrderItem());
            clonedFulfillmentGroupItem.setQuantity(getQuantity());
            clonedFulfillmentGroupItem.setTotalItemAmount(getTotalItemAmount());
            if (getStatus() != null) {
                clonedFulfillmentGroupItem.setStatus(getStatus());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return clonedFulfillmentGroupItem;
    }

    @Override
    public boolean getHasProratedOrderAdjustments() {
        if (proratedOrderAdjustment != null) {
            return (proratedOrderAdjustment.compareTo(BigDecimal.ZERO) == 0);
        }
        return false;
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
        FulfillmentGroupItemImpl other = (FulfillmentGroupItemImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (orderItem == null) {
            if (other.orderItem != null) {
                return false;
            }
        } else if (!orderItem.equals(other.orderItem)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((orderItem == null) ? 0 : orderItem.hashCode());
        return result;
    }
}
