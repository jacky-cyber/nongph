package cn.globalph.coupon.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import cn.globalph.common.money.Money;
import cn.globalph.coupon.apply.OrderDetailToApplyCoupon;
import cn.globalph.coupon.apply.condition.impl.ProductApplyCondition;
import cn.globalph.coupon.dao.CouponDao;
import cn.globalph.coupon.dao.CustomerCouponDao;
import cn.globalph.coupon.domain.Coupon;
import cn.globalph.coupon.domain.CustomerCoupon;
import cn.globalph.coupon.issue.event.CouponIssueEvent;
import cn.globalph.coupon.issue.event.CouponIssueEventSource;
import cn.globalph.coupon.issue.event.CouponIssueEventType;
import cn.globalph.coupon.status.CouponStatus;
import cn.globalph.coupon.strategy.CouponStrategy;
import cn.globalph.passport.dao.CustomerDao;
import cn.globalph.passport.domain.Customer;

@Service("phCouponService")
public class CouponServiceImpl implements CouponService {

	protected static final Log LOG = LogFactory.getLog(CouponServiceImpl.class);

    @Resource(name="phCouponDao")
    protected CouponDao couponDao;
    
	@Resource(name = "phCustomerCouponDao")
	CustomerCouponDao customerCouponDao;
	
	@Resource(name = "blCustomerDao")
	CustomerDao customerDao;
	
	
	@Autowired
	private ApplicationContext applicationContext;
      
	@Override
	public List<Coupon> findAllCoupons() {
		return couponDao.findAllCoupons();
	}

	@Override
	public Coupon getCouponById(Long couponId) {
		return couponDao.getCouponById(couponId);
	}

	
	@Override
	public List<CustomerCoupon> getCurrentAvailableCouponByCustomerId(Long customerId, OrderDetailToApplyCoupon order) {
        List<CustomerCoupon> customerCoupons =  customerCouponDao.findActiveCouponByCustomerId(customerId);
        List<CustomerCoupon> returnCustomerCoupons = new ArrayList<CustomerCoupon>();
        Date current = new Date(System.currentTimeMillis());
		for(CustomerCoupon customerCoupon : customerCoupons){
        	if(customerCoupon.getStatus() != CouponStatus.ACTIVE) {
        		continue;
        	}else{
        		if(checkCoupon(customerCoupon, current, order)){
        			returnCustomerCoupons.add(customerCoupon);
        		}
        	}
        } 
		return returnCustomerCoupons;
	}
	
	private Boolean checkCoupon(CustomerCoupon customerCoupon, Date checkDate, OrderDetailToApplyCoupon order){
		if(customerCoupon.getNumber() == 0){
			customerCoupon.setStatus(CouponStatus.USED);
			customerCouponDao.saveCustomerCoupon(customerCoupon);
			return false;
		}
		
		Coupon coupon = customerCoupon.getCoupon();
		if(coupon.isValidPeriod(checkDate)){
			 CouponStrategy couponStrategy = applicationContext.getBean(coupon.getStrategy().getType(), CouponStrategy.class);
		     if(couponStrategy.isAvailable(coupon, order)){
		    	 return true;
		     }else{
		    	 return false;
		     }
		}else{
			customerCoupon.setStatus(CouponStatus.OVER_TIME);
			customerCouponDao.saveCustomerCoupon(customerCoupon);
			return false;
		}
	}
	
	
	@Override
	public boolean isAvailable(CustomerCoupon customerCoupon, OrderDetailToApplyCoupon order){
		if(customerCoupon.getStatus() != CouponStatus.ACTIVE){
			LOG.debug("customer coupon " + customerCoupon.getId() + " is not active");
			return false;
		}else{
			if(customerCoupon.getNumber() == 0){
				LOG.debug("customer coupon " + customerCoupon.getId() + " is used");
				customerCoupon.setStatus(CouponStatus.USED);
				customerCouponDao.saveCustomerCoupon(customerCoupon);
				return false;
			}
		}
	    CouponStrategy couponStrategy = applicationContext.getBean(customerCoupon.getCoupon().getStrategy().getType(), CouponStrategy.class);
	    return couponStrategy.isAvailable(customerCoupon.getCoupon(),order);
	}
	
	@Override
	public BigDecimal getDiscount(Coupon coupon, OrderDetailToApplyCoupon order){
		CouponStrategy couponStrategy = applicationContext.getBean(coupon.getStrategy().getType(), CouponStrategy.class);
		return couponStrategy.getDiscount(coupon, order);
	}

	@Override
	public Boolean sendCustomerCoupon(Long customerId, Integer number) {
		Customer customer = customerDao.get(customerId);
		if(customer == null) return false;
        applicationContext.publishEvent(
                new CouponIssueEvent(CouponIssueEventType.SEND_COUPON_EVENT.getType(), number,
                        new CouponIssueEventSource(customerId,
                        new Date(System.currentTimeMillis())
                        )
                )
        );
        
        return true;
	}
}
