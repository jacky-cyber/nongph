package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.common.money.Money;
import cn.globalph.coupon.apply.OrderDetailToApplyCoupon;
import cn.globalph.coupon.domain.Coupon;
import cn.globalph.coupon.domain.CustomerCoupon;
import cn.globalph.coupon.service.CouponService;
import cn.globalph.coupon.service.CustomerCouponService;
import cn.globalph.coupon.status.CouponStatus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;


/**
 * revert coupon
 */
public class CouponActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    protected static final Log LOG = LogFactory.getLog(CouponActivity.class);

    @Resource(name = "phCustomerCouponService")
    protected CustomerCouponService customerCouponService;
    @Resource(name = "phCouponService")
    protected CouponService couponService;
    @Resource(name = "blOrderService")
    protected OrderService orderService;
    @Autowired
    protected ApplicationContext applicationContext;

    private Boolean isRevert = false;

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        if (order.getApplyCoupon() != null) {
            CustomerCoupon customerCoupon = order.getApplyCoupon();
            if (customerCoupon != null) {
                if (!isRevert) {
                	OrderDetailToApplyCoupon orderDetailToApplyCoupon = orderService.generateOrderDetailToApplyCoupon(order);
                	if(couponService.isAvailable(customerCoupon, orderDetailToApplyCoupon)){
	                	if(customerCoupon.getNumber() > 1){
	                		customerCoupon.setNumber(customerCoupon.getNumber() - 1);
	                	}else{
	                		customerCoupon.setNumber(0);
	                		customerCoupon.setStatus(CouponStatus.USED);
	                	}
                	}else{
                		order.setApplyCoupon(null);
                		order.setCouponDiscount(Money.ZERO);
                	}
                } else {
                    customerCoupon.setStatus(CouponStatus.ACTIVE);
                    customerCoupon.setNumber(customerCoupon.getNumber() + 1);
                }
                customerCouponService.saveCustomerCoupon(customerCoupon);
                orderService.save(order, true);
            }
        }
        return context;
    }

    public void setIsRevert(Boolean isRevert) {
        this.isRevert = isRevert;
    }
}

