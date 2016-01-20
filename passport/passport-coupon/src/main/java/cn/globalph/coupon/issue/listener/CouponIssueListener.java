package cn.globalph.coupon.issue.listener;

import cn.globalph.coupon.issue.event.CouponIssueEvent;
import cn.globalph.coupon.issue.CouponIssueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CouponIssueListener implements ApplicationListener<CouponIssueEvent> {
    @Autowired
    private CouponIssueHandler couponIssueHandler;

    @Override
    public void onApplicationEvent(CouponIssueEvent event) {
        couponIssueHandler.issue(event);
    }

}
