package cn.globalph.coupon.issue.event.condition.impl;

import cn.globalph.coupon.issue.event.condition.CouponIssueCondition;
import cn.globalph.coupon.type.CouponAttributeNameType;

public class ProductIssueCondition extends CouponIssueCondition<String> {
    public ProductIssueCondition(String value) {
        super(CouponAttributeNameType.PRODUCT.getType(), value);
    }

    @Override
    public boolean doVerify(String configMinProduct) {
        return configMinProduct.equals(getValue());
    }
}
