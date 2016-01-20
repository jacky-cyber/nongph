package cn.globalph.coupon.strategy.impl;

import cn.globalph.common.sms.SMSUtil;
import cn.globalph.coupon.apply.OrderDetailToApplyCoupon;
import cn.globalph.coupon.apply.condition.CouponApplyCondition;
import cn.globalph.coupon.domain.Coupon;
import cn.globalph.coupon.domain.CustomerCoupon;
import cn.globalph.coupon.issue.event.CouponIssueEventSource;
import cn.globalph.coupon.service.CustomerCouponService;
import cn.globalph.coupon.status.CouponStatus;
import cn.globalph.coupon.strategy.CouponStrategy;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public abstract class AbstractCouponStrategy implements CouponStrategy {
    private static Log LOG = LogFactory.getLog(AbstractCouponStrategy.class);

	@Resource(name = "phCustomerCouponService")
	private CustomerCouponService customerCouponService;

	@Resource(name = "blCustomerService")
	private CustomerService customerService;
	
    @Override
    public void issue(Coupon coupon,CouponIssueEventSource source) {
        Long customerId = source.getCustomerId();
        Customer customer = customerService.getCustomerById(customerId);
        CustomerCoupon customerCoupon = customerCouponService.createNewCustomerCoupon();
        customerCoupon.setCoupon(coupon);
        customerCoupon.setCutomer(customer);
        if(source.getCouponIssueEvent().getNumber() == 0){
        	if(coupon.getNumber() == 0){
        		customerCoupon.setNumber(1);
        	}else{
        		customerCoupon.setNumber(coupon.getNumber());
        	}
        }else{
        	customerCoupon.setNumber(source.getCouponIssueEvent().getNumber());
        }
        customerCoupon.setStatus(CouponStatus.ACTIVE);
        customerCouponService.saveCustomerCoupon(customerCoupon);
        try {
            SMSUtil.sendMessage(customer.getPhone(), "您好，恭喜您获得品荟生活现金券，详情请进品荟生活-我的优惠券查看!");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Boolean isAvailable(Coupon coupon, OrderDetailToApplyCoupon order) {
        if (!coupon.isValidPeriod(new Date())) {
            LOG.debug("customer coupon " + coupon.getId() + " is expired");
            return false;
        }
        return checkApplyCondition(coupon,order);
    }

    private boolean checkApplyCondition(Coupon coupon,OrderDetailToApplyCoupon order){
        List<CouponApplyCondition> conditions = order.getConditions();
        for(CouponApplyCondition condition : conditions){
            if(!condition.verify(coupon)){
                LOG.debug("customer coupon " + coupon.getId() + " not fit condition " + condition.getName());
                return false;
            }
        }
        LOG.debug("customer coupon " + coupon.getId() + " fits all conditions");
        return true;
    }
}
