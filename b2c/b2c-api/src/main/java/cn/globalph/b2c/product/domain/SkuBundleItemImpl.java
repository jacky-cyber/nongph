package cn.globalph.b2c.product.domain;

import cn.globalph.b2c.catalog.service.dynamic.DefaultDynamicSkuPricingInvocationHandler;
import cn.globalph.b2c.catalog.service.dynamic.DynamicSkuPrices;
import cn.globalph.b2c.catalog.service.dynamic.SkuPricingConsiderationContext;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationToOneLookup;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.RequiredOverride;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.client.VisibilityEnum;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "NPH_BUNDLE_SKU_ITEM")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blProducts")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.FALSE)
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class SkuBundleItemImpl implements SkuBundleItem {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator = "SkuBundleItemId")
    @GenericGenerator(
        name="SkuBundleItemId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name = "segment_value", value = "SkuBundleItemImpl"),
            @Parameter(name = "entity_name", value = "cn.globalph.b2c.product.domain.SkuBundleItemImpl")
        }
    )
    @Column(name = "SKU_BUNDLE_ITEM_ID")
    @AdminPresentation(friendlyName = "SkuBundleItemImpl_ID", visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @Column(name = "QUANTITY", nullable=false)
    @AdminPresentation(friendlyName = "bundleItemQuantity", prominent = true,
        requiredOverride = RequiredOverride.REQUIRED)
    protected Integer quantity;

    @Column(name = "ITEM_SALE_PRICE", precision=19, scale=5)
    @AdminPresentation(friendlyName = "bundleItemSalePrice", prominent = true,
        tooltip="bundleItemSalePriceTooltip", 
        fieldType = SupportedFieldType.MONEY)
    protected BigDecimal itemSalePrice;

    @ManyToOne(targetEntity = SkuBundleImpl.class, optional = false)
    @JoinColumn(name = "BUNDLE_SKU_ID", referencedColumnName = "SKU_ID")
    protected SkuBundle bundle;

    @ManyToOne(targetEntity = SkuImpl.class, optional = false)
    @JoinColumn(name = "SKU_ID", referencedColumnName = "SKU_ID")
    @AdminPresentation(friendlyName = "Sku", prominent = true, 
        order = 0, gridOrder = 0)
    @AdminPresentationToOneLookup()
    protected Sku sku;

    @Transient
    protected DynamicSkuPrices dynamicPrices = null;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
   
    public Money getDynamicSalePrice(Sku sku, BigDecimal salePrice) {   
        Money returnPrice = null;
        
        if (SkuPricingConsiderationContext.hasDynamicPricing()) {
            if (dynamicPrices != null) {
                returnPrice = dynamicPrices.getSalePrice();
            } else {
                DefaultDynamicSkuPricingInvocationHandler handler = new DefaultDynamicSkuPricingInvocationHandler(sku, salePrice);
                Sku proxy = (Sku) Proxy.newProxyInstance(sku.getClass().getClassLoader(), ClassUtils.getAllInterfacesForClass(sku.getClass()), handler);
                dynamicPrices = SkuPricingConsiderationContext.getSkuPricingService().getSkuPrices(proxy, SkuPricingConsiderationContext.getSkuPricingConsiderationContext());
                returnPrice = dynamicPrices.getSalePrice();
            }
        } else {
            if (salePrice != null) {
                returnPrice = new Money(salePrice);
            }
        }
        
        return returnPrice;   
    }
    @Override
    public void setSalePrice(Money salePrice) {
        if (salePrice != null) {
            this.itemSalePrice = salePrice.getAmount();
        } else {
            this.itemSalePrice = null;
        }
    }


    @Override
    public Money getSalePrice() {
        if (itemSalePrice == null) {
            return sku.getSalePrice();
        } else {
            return getDynamicSalePrice(sku, itemSalePrice);
        }
    }

    @Override
    public Money getRetailPrice() {
         return sku.getRetailPrice();
     }

    @Override
    public SkuBundle getBundle() {
        return bundle;
    }

    @Override
    public void setBundle(SkuBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public Sku getSku() {
        return sku;
    }

    @Override
    public void setSku(Sku sku) {
        this.sku = sku;
    }
    
    @Override
    public void clearDynamicPrices() {
        this.dynamicPrices = null;
    }
}
