package cn.globalph.coupon.issue;

import cn.globalph.coupon.issue.event.CouponIssueEvent;
import cn.globalph.coupon.domain.Coupon;
import cn.globalph.coupon.service.CouponService;
import cn.globalph.coupon.strategy.CouponStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.util.List;

import javax.annotation.Resource;

@Component(value = "phCouponIssueHandler")
public class CouponIssueHandler {
    @Autowired
    private ApplicationContext applicationContext;
    
    @Resource(name = "phCouponService")
    private CouponService couponService;

    public void issue(CouponIssueEvent event){
        List<Coupon> coupons = couponService.findAllCoupons(); //get coupon by priority desc order
        if(event.isSupportedPriority()){
	        Coupon availableCoupon = null;
	        for(Coupon coupon : coupons){
	            if(event.isSupportedEvent(coupon)){
	                if(availableCoupon == null || coupon.getIssuePriority() >= availableCoupon.getIssuePriority()) {
	                    availableCoupon  = coupon;
	                }
	            }
	        }
	        if(availableCoupon != null){
	            CouponStrategy couponStrategy = applicationContext.getBean(availableCoupon.getStrategy().getType(),CouponStrategy.class);
	            couponStrategy.issue(availableCoupon, event.getSource());
	
	        }
        }else{
	        for(Coupon coupon : coupons){
	            if(event.isSupportedEvent(coupon)){
		            CouponStrategy couponStrategy = applicationContext.getBean(coupon.getStrategy().getType(),CouponStrategy.class);
		            couponStrategy.issue(coupon, event.getSource());
	            }
	        }
        }
    }



}
