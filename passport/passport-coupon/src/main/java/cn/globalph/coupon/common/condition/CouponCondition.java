package cn.globalph.coupon.common.condition;

import cn.globalph.coupon.domain.Coupon;

public abstract class CouponCondition<T> {
    private String name;
    private T value;

    public CouponCondition(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }


    public boolean verify(Coupon coupon) {
        String configParameter = getConfigParameter(coupon,getCategory(),getName());
        return configParameter == null || doVerify(configParameter);
    }

    public String getConfigParameter(Coupon coupon,String category,String attrName){
        return coupon.getAttrValue(category, attrName);
    }

    public abstract String getCategory();
    public abstract boolean doVerify(String configParameter);
}
