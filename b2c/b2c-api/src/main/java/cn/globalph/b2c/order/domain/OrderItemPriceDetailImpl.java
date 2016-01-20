package cn.globalph.b2c.order.domain;

import java.util.ArrayList;
import java.util.List;

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

import cn.globalph.b2c.offer.domain.OrderItemPriceDetailAdjustment;
import cn.globalph.b2c.offer.domain.OrderItemPriceDetailAdjustmentImpl;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.client.AddMethodType;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.common.presentation.override.AdminPresentationMergeEntry;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverride;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverrides;
import cn.globalph.common.presentation.override.PropertyType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_ORDER_ITEM_PRICE_DTL")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
@AdminPresentationMergeOverrides(
    {
        @AdminPresentationMergeOverride(name = "", mergeEntries =
            @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.READONLY,
                                            booleanOverrideValue = true))
    }
)
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class OrderItemPriceDetailImpl implements OrderItemPriceDetail{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "OrderItemPriceDetailId")
    @GenericGenerator(
        name="OrderItemPriceDetailId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="OrderItemPriceDetailImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.order.domain.OrderItemPriceDetailImpl")
        }
    )
    @Column(name = "ORDER_ITEM_PRICE_DTL_ID")
    @AdminPresentation(friendlyName = "OrderItemPriceDetailImpl_Id", group = "OrderItemPriceDetailImpl_Primary_Key", visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @ManyToOne(targetEntity = OrderItemImpl.class)
    @JoinColumn(name = "ORDER_ITEM_ID")
    @AdminPresentation(excluded = true)
    protected OrderItem orderItem;
    
    /**
     * 对应的促销折扣
     */
    @OneToMany(mappedBy = "orderItemPriceDetail", targetEntity = OrderItemPriceDetailAdjustmentImpl.class, cascade = { CascadeType.ALL }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "blOrderElements")
    @AdminPresentationCollection(addType = AddMethodType.PERSIST, friendlyName = "OrderItemPriceDetailImpl_orderItemPriceDetailAdjustments")
    protected List<OrderItemPriceDetailAdjustment> orderItemPriceDetailAdjustments = new ArrayList<OrderItemPriceDetailAdjustment>();

    @Column(name = "QUANTITY", nullable=false)
    @AdminPresentation(friendlyName = "OrderItemPriceDetailImpl_quantity", order = 5, group = "OrderItemPriceDetailImpl_Pricing", prominent = true)
    protected int quantity;

    @Column(name = "USE_SALE_PRICE")
    @AdminPresentation(friendlyName = "OrderItemPriceDetailImpl_useSalePrice", order = 5, group = "OrderItemPriceDetailImpl_Pricing", prominent = true)
    protected Boolean useSalePrice = true;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
    public List<OrderItemPriceDetailAdjustment> getOrderItemPriceDetailAdjustments() {
        return orderItemPriceDetailAdjustments;
    }

    @Override
    public void setOrderItemAdjustments(List<OrderItemPriceDetailAdjustment> orderItemPriceDetailAdjustments) {
        this.orderItemPriceDetailAdjustments = orderItemPriceDetailAdjustments;
        
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    /**
     * 将对应的促销折扣相加，得到单个折扣总和
     */
    @Override
    public Money getAdjustmentValue() {
        Money adjustmentValue = Money.ZERO;
        for (OrderItemPriceDetailAdjustment adjustment : orderItemPriceDetailAdjustments) {
            adjustmentValue = adjustmentValue.add(adjustment.getValue());
        }
        return adjustmentValue;
    }
    
    /**
     * 对应数量的折扣总和
     */
    @Override
    public Money getTotalAdjustmentValue() {
        return getAdjustmentValue().multiply(quantity);
    }
    
    /**
     * 对应数量扣除折扣后的总价
     */
    @Override
    public Money getTotalAdjustedPrice() {
        Money basePrice = orderItem.getPriceBeforeAdjustments(getUseSalePrice());
        return basePrice.multiply(quantity).subtract(getTotalAdjustmentValue());
    }

    @Override
    public boolean getUseSalePrice() {
        if (useSalePrice == null) {
            return false;
        } else {
            return useSalePrice;
        }
    }

    @Override
    public void setUseSalePrice(boolean useSalePrice) {
        this.useSalePrice = Boolean.valueOf(useSalePrice);
    }
}
