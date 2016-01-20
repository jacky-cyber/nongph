package cn.globalph.coupon.issue.event.condition;

import cn.globalph.coupon.common.condition.CouponCondition;
import cn.globalph.coupon.issue.event.CouponIssueEventSource;

public abstract class CouponIssueCondition<T> extends CouponCondition<T> {

    private CouponIssueEventSource couponIssueEventSource;

    public CouponIssueCondition(String name, T value) {
        super(name,value);
    }

    public CouponIssueEventSource getCouponIssueEventSource() {
        return couponIssueEventSource;
    }

    public void setCouponIssueEventSource(CouponIssueEventSource couponIssueEventSource) {
        this.couponIssueEventSource = couponIssueEventSource;
    }

    @Override
    public String getCategory() {
        return getCouponIssueEventSource().getCouponIssueEvent().getName();
    }

}
