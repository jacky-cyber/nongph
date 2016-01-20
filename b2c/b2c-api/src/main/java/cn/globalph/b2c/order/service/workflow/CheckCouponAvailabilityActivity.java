package cn.globalph.b2c.order.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.common.money.Money;
import cn.globalph.coupon.apply.OrderDetailToApplyCoupon;
import cn.globalph.coupon.domain.CustomerCoupon;
import cn.globalph.coupon.service.CouponService;
import cn.globalph.coupon.service.CustomerCouponService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;


/**
 * revert coupon
 */
public class CheckCouponAvailabilityActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {

    protected static final Log LOG = LogFactory.getLog(CheckCouponAvailabilityActivity.class);

    @Resource(name = "phCustomerCouponService")
    protected CustomerCouponService customerCouponService;
    @Resource(name = "phCouponService")
    protected CouponService couponService;
    @Resource(name = "blOrderService")
    protected OrderService orderService;
    @Autowired
    protected ApplicationContext applicationContext;

    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        if (order.getApplyCoupon() != null) {
            CustomerCoupon customerCoupon = order.getApplyCoupon();
            if (customerCoupon != null) {
            	OrderDetailToApplyCoupon orderDetailToApplyCoupon = orderService.generateOrderDetailToApplyCoupon(order);
            	if(!couponService.isAvailable(customerCoupon, orderDetailToApplyCoupon)){
            		order.setApplyCoupon(null);
            		order.setCouponDiscount(Money.ZERO);
                    customerCouponService.saveCustomerCoupon(customerCoupon);
                    orderService.save(order, true);
            	}

            }
        }
        return context;
    }
}

