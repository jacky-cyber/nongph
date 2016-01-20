package cn.globalph.coupon.issue.event;

import cn.globalph.coupon.issue.event.condition.CouponIssueCondition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponIssueEventSource {
    private Long customerId;
    private Date couponEventDate;
    private CouponIssueEvent couponIssueEvent;
    private List<CouponIssueCondition> conditions = new ArrayList<CouponIssueCondition>();

    public CouponIssueEventSource(Long customerId, Date couponEventDate) {
        this.customerId = customerId;
        this.couponEventDate = couponEventDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getCouponEventDate() {
        return couponEventDate;
    }

    public void setCouponEventDate(Date couponEventDate) {
        this.couponEventDate = couponEventDate;
    }

    public CouponIssueEvent getCouponIssueEvent() {
        return couponIssueEvent;
    }

    public void setCouponIssueEvent(CouponIssueEvent couponIssueEvent) {
        this.couponIssueEvent = couponIssueEvent;
    }

    public List<CouponIssueCondition> getConditions() {
        return conditions;
    }

    public CouponIssueEventSource addCondition(CouponIssueCondition condition){
        condition.setCouponIssueEventSource(this);
        this.conditions.add(condition);
        return this;
    }
}
