package cn.globalph.coupon.service;

import cn.globalph.common.sms.SMSUtil;
import cn.globalph.common.util.TransactionUtils;
import cn.globalph.coupon.dao.CouponDao;
import cn.globalph.coupon.dao.CustomerCouponDao;
import cn.globalph.coupon.domain.Coupon;
import cn.globalph.coupon.domain.CustomerCoupon;
import cn.globalph.coupon.status.CouponStatus;
import cn.globalph.passport.dao.CustomerDao;
import cn.globalph.passport.domain.Customer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("phCustomerCouponService")
public class CustomerCouponServiceImpl implements CustomerCouponService {
	private static Log LOG = LogFactory.getLog(CustomerCouponServiceImpl.class);
	
	@Resource(name = "phCustomerCouponDao")
	CustomerCouponDao customerCouponDao;
	
	@Resource(name = "phCouponDao")
	CouponDao couponDao;
	
	@Resource(name = "blCustomerDao")
	CustomerDao customerDao;
	
	@Override
	@Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
	public CustomerCoupon saveCustomerCoupon(CustomerCoupon customerCoupon) {
		return customerCouponDao.persist(customerCoupon);
	}

	@Override
	public CustomerCoupon createNewCustomerCoupon() {
		return customerCouponDao.createCustomerCoupon();
	}
	
	@Override
	public List<CustomerCoupon> findActiveCouponByCustomerId(Long customerId){
		return customerCouponDao.findActiveCouponByCustomerId(customerId);
	}
	
	@Override
	public CustomerCoupon findCustomerCouponByCustomerIdAndCouponId(Long customerId, Long couponId){
		return customerCouponDao.findCustomerCouponByCustomerIdAndCouponId(customerId, couponId);
	}

	@Override
	public CustomerCoupon findCustomerCouponById(Long customerCouponId) {
		return customerCouponDao.get(customerCouponId);
	}
	
	@Override
	@Transactional("blTransactionManager")
	public CustomerCoupon sendCustomerCoupon(Long customerId, Long couponId){
		Coupon coupon = couponDao.getCouponById(couponId);
		Customer customer = customerDao.get(customerId);
		if(coupon == null || customer == null){
			LOG.warn("coupon or customer is null.");
			return null;
		}
		Date now = new Date(System.currentTimeMillis());
		if(now.after(coupon.getStartDate()) && now.before(coupon.getEndDate())){
			CustomerCoupon customerCoupon = customerCouponDao.createCustomerCoupon();
			customerCoupon.setCoupon(coupon);
			customerCoupon.setCutomer(customer);
			customerCoupon.setNumber(coupon.getNumber());
			customerCoupon.setStatus(CouponStatus.ACTIVE);
			customerCoupon = customerCouponDao.saveCustomerCoupon(customerCoupon);
			try {
				SMSUtil.sendMessage(customer.getPhone(), "您好，恭喜您获得品荟生活现金券，详情请进品荟生活-我的优惠券查看!");
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			return customerCoupon;
		}else{
			LOG.warn("coupon can not be used now");
			return null;
		}

	}
	
	@Override
	@Transactional("blTransactionManager")
	public Integer findActiveCustomerCouponCount(Long customerId){
		List<CustomerCoupon> customerCoupons = customerCouponDao.findActiveCouponByCustomerId(customerId);
		if(customerCoupons == null || customerCoupons.size() == 0){
			return 0;
		}
		int removed = 0;
		Date checkDate = new Date(System.currentTimeMillis());
		for(CustomerCoupon customerCoupon : customerCoupons){
			if(customerCoupon.getNumber() == 0){
				customerCoupon.setStatus(CouponStatus.USED);
				customerCouponDao.saveCustomerCoupon(customerCoupon);
				removed++ ;
				continue;
			}
			
			Coupon coupon = customerCoupon.getCoupon();
			if(!coupon.isValidPeriod(checkDate)){
				customerCoupon.setStatus(CouponStatus.OVER_TIME);
				customerCouponDao.saveCustomerCoupon(customerCoupon);
				removed++;
				continue;
			}
		}
		
		return customerCoupons.size() - removed;
	}
}
