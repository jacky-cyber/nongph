package cn.globalph.b2c.product.domain;

import cn.globalph.common.extensibility.jpa.clone.ClonePolicyCollection;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "NPH_BUNDLE_SKU")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blProducts")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "ProductImpl_bundleProduct")
public class SkuBundleImpl extends SkuImpl implements SkuBundle {

    private static final long serialVersionUID = 1L;

    @Column(name = "AUTO_BUNDLE")
    @AdminPresentation(excluded = true)
    protected Boolean autoBundle = false;

    @Column(name = "ITEMS_PROMOTABLE")
    @AdminPresentation(excluded = true)
    protected Boolean itemsPromotable = false;

    @Column(name = "BUNDLE_PROMOTABLE")
    @AdminPresentation(excluded = true)
    protected Boolean bundlePromotable = false;

    @Column(name = "BUNDLE_PRIORITY")
    @AdminPresentation(excluded = true, friendlyName = "productBundlePriority", group="productBundleGroup")
    protected int priority=99;

    @OneToMany(mappedBy = "bundle", targetEntity = SkuBundleItemImpl.class, cascade = { CascadeType.ALL })
    @Cascade(value = { org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blProducts")
    @BatchSize(size = 50)
    @AdminPresentationCollection(friendlyName = "skuBundleItemsTitle")
    @ClonePolicyCollection
    protected List<SkuBundleItem> skuBundleItems = new ArrayList<SkuBundleItem>();
    
    @Override
    public boolean isOnSale() {
        Money retailPrice = getRetailPrice();
        Money salePrice = getSalePrice();
        return (salePrice != null && !salePrice.isZero() && salePrice.lessThan(retailPrice));
    }

    @Override
    public Money getBundleItemsRetailPrice() {
        Money price = new Money(BigDecimal.ZERO);
        for (SkuBundleItem item : getSkuBundleItems()) {
            price = price.add(item.getRetailPrice().multiply(item.getQuantity()));
           }
        return price;
     }

    @Override
    public Money getBundleItemsSalePrice() {
        Money price = new Money(BigDecimal.ZERO);
        for (SkuBundleItem item : getSkuBundleItems()){
            if (item.getSalePrice() != null) {
                price = price.add(item.getSalePrice().multiply(item.getQuantity()));
            } else {
                price = price.add(item.getRetailPrice().multiply(item.getQuantity()));
            }
        }
        return price;
    }

    @Override
    public Boolean getAutoBundle() {
        return autoBundle == null ? false : autoBundle;
    }

    @Override
    public void setAutoBundle(Boolean autoBundle) {
        this.autoBundle = autoBundle;
    }

    @Override
    public Boolean getItemsPromotable() {
        return itemsPromotable == null ? false : itemsPromotable;
    }

    @Override
    public void setItemsPromotable(Boolean itemsPromotable) {
        this.itemsPromotable = itemsPromotable;
    }

    @Override
    public Boolean getBundlePromotable() {
        return bundlePromotable == null ? false : bundlePromotable;
    }

    @Override
    public void setBundlePromotable(Boolean bundlePromotable) {
        this.bundlePromotable = bundlePromotable;
    }

    @Override
    public List<SkuBundleItem> getSkuBundleItems() {
        return skuBundleItems;
    }

    @Override
    public void setSkuBundleItems(List<SkuBundleItem> skuBundleItems) {
        this.skuBundleItems = skuBundleItems;
    }

    @Override
    public Integer getPriority() {
        return priority;
    }

    @Override
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public BigDecimal getPotentialSavings() {

        if (skuBundleItems != null) {
            Money totalNormalPrice = Money.ZERO;
            Money totalBundlePrice = Money.ZERO;

            for (SkuBundleItem skuBundleItem : skuBundleItems) {

                Sku sku = skuBundleItem.getSku();

                if (sku != null && sku.getRetailPrice() != null) {
                    totalNormalPrice = totalNormalPrice.add(sku.getRetailPrice().multiply(skuBundleItem.getQuantity()));
                	 }
            	}

            if (getSalePrice() != null) {
                totalBundlePrice = getSalePrice();
            } else {
                totalBundlePrice = getRetailPrice();
            	}
            return totalNormalPrice.subtract(totalBundlePrice).getAmount();

           }
        return BigDecimal.ZERO;

    }

}
