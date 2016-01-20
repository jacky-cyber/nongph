package cn.globalph.b2c.catalog.service.dynamic;

import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.SkuImpl;
import cn.globalph.common.money.Money;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.math.BigDecimal;

public class DefaultDynamicSkuPricingInvocationHandler implements InvocationHandler {

    private Sku delegate;
    private Money retailPrice;
    private Money salePrice;
    
    public DefaultDynamicSkuPricingInvocationHandler(Sku sku) {
        this.delegate = sku;
        try {
            Field retail = getSingleField(delegate.getClass(), "retailPrice");
            retail.setAccessible(true);
            Object retailVal = retail.get(delegate);
            retailPrice = retailVal==null?null:new Money((BigDecimal) retailVal);
            Field sale = getSingleField(delegate.getClass(), "salePrice");
            sale.setAccessible(true);
            Object saleVal = sale.get(delegate);
            salePrice = saleVal==null?null:new Money((BigDecimal) saleVal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This is used with SkuBundleItem to allow the bundle override price.
     *
     * @param sku
     * @param salePriceOverride
     */
    public DefaultDynamicSkuPricingInvocationHandler(Sku sku, BigDecimal salePriceOverride) {
        this(sku);

        if (salePriceOverride != null) {
            salePrice = new Money(salePriceOverride);
        }
    }
    /**
     * This is used with SkuBundleItem to allow the bundle override price.
     *
     * @param sku
     * @param salePriceOverride
     */
    public DefaultDynamicSkuPricingInvocationHandler(BigDecimal salePriceOverride) {
        this(new SkuImpl());

        if (salePriceOverride != null) {
            salePrice = new Money(salePriceOverride);
        }
    }
    /**
     * Used to add ProductOptionValue price adjustments to the proxy Sku
     * 
     * @param sku
     * @param adjustments - the sum total of the ProductOptionValue price adjustments. If null, this
     * functions the same as the default constructor. This value is added to both the salePrice and retailPrice
     */
    public DefaultDynamicSkuPricingInvocationHandler(Sku sku, Money adjustments) {
        this(sku);

        if (adjustments != null) {
            salePrice = (salePrice == null) ? adjustments : salePrice.add(adjustments);
            retailPrice = (retailPrice == null) ? adjustments : retailPrice.add(adjustments);
        }
    }
    
    private Field getSingleField(Class<?> clazz, String fieldName) throws IllegalStateException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException nsf) {
            // Try superclass
            if (clazz.getSuperclass() != null) {
                return getSingleField(clazz.getSuperclass(), fieldName);
            }

            return null;
        }
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("getRetailPrice")) {
            return retailPrice;
        } else if (method.getName().equals("getSalePrice")) {
            return salePrice;
        } else {
            return method.invoke(delegate, args);
        }
    }

    public void reset() {
        delegate = null;
        retailPrice = null;
        salePrice = null;
    }
}
