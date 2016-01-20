package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.coupon.domain.CouponCode;
import cn.globalph.b2c.coupon.service.CouponCodeService;
import cn.globalph.b2c.coupon.type.CouponCodeType;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.common.money.Money;
import cn.globalph.coupon.domain.Coupon;
import cn.globalph.coupon.service.CustomerCouponService;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerMessageService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;

/**
 * coupon code
 */
public class CouponCodeActivity extends
		BaseActivity<ProcessContext<CheckoutSeed>> {

	protected static final Log LOG = LogFactory.getLog(CouponActivity.class);

	@Resource(name = "phCouponCodeService")
	protected CouponCodeService couponCodeService;
	@Resource(name = "blOrderService")
	protected OrderService orderService;
	@Resource(name = "phCustomerCouponService")
	protected CustomerCouponService customerCouponService;
	@Resource(name = "phCustomerMessageService")
	protected CustomerMessageService customerMessageService;
	// 取消订单
	private Boolean isCancelOrder = false;
	// 付款完成
	private Boolean isConfirmOrder = false;
	// 提交订单
	private Boolean isCheckout = false;

	@Override
	public ProcessContext<CheckoutSeed> execute(
			ProcessContext<CheckoutSeed> context) throws Exception {
		if (isCheckout)
			return checkout(context);
		if (isConfirmOrder)
			return confirmOrder(context);
		if (isCancelOrder)
			return cancelOrder(context);
		return context;
	}

	// 取消订单
	private ProcessContext<CheckoutSeed> cancelOrder(
			ProcessContext<CheckoutSeed> context) throws PricingException {
		Order order = context.getSeedData().getOrder();
		CouponCode couponCode = order.getCouponCode();
		if (couponCode != null) {
			if (couponCode.isValid()) {
				couponCode.setStatus(CouponCodeType.ACTIVE);
			} else {
				couponCode.setStatus(CouponCodeType.OVER_TIME);
			}
			couponCodeService.saveCouponCode(couponCode);
		}

		return context;
	}

	// 付款完成
	private ProcessContext<CheckoutSeed> confirmOrder(
			ProcessContext<CheckoutSeed> context) throws PricingException {
		Order order = context.getSeedData().getOrder();
		CouponCode couponCode = order.getCouponCode();
		if (couponCode != null) {
			Customer orderCustomer = order.getCustomer();
			Customer couponCodeCustomer = couponCode.getCustomer();
			Coupon coupon = couponCode.getCoupon();
			if (orderCustomer == null || couponCodeCustomer == null
					|| coupon == null) {
				return context;
			}
			// 赠送优惠券
			if (!orderCustomer.getId().equals(couponCodeCustomer.getId())) {
				customerCouponService.sendCustomerCoupon(
						couponCodeCustomer.getId(), coupon.getId());
				customerMessageService.sendMessageToCustomer(
						couponCodeCustomer, "品荟码使用情况", "您的品荟码"
								+ couponCode.getCouponCode()
								+ "已被使用，由于您是分享给其他用户使用，您将获得一张品荟码分享奖励优惠券，请注意查收！");
			} else {
				customerMessageService.sendMessageToCustomer(
						couponCodeCustomer, "品荟码使用情况", "您的品荟码"
								+ couponCode.getCouponCode() + "已被您本人使用！");
			}
			
		}

		return context;
	}

	// 提交订单
	private ProcessContext<CheckoutSeed> checkout(
			ProcessContext<CheckoutSeed> context) throws PricingException {
		Order order = context.getSeedData().getOrder();
		if (order.getCouponCode() != null) {
			CouponCode couponCode = order.getCouponCode();
			if (couponCode.isValid()
					&& couponCode.getStatus().equals(CouponCodeType.ACTIVE)) {
				couponCode.setStatus(CouponCodeType.HAS_USED);
			} else {
				order.setCouponCode(null);
				order.setCouponCodeDiscount(Money.ZERO);
			}
			couponCodeService.saveCouponCode(couponCode);
			orderService.save(order, true);
		}

		return context;
	}

	public void setIsCancelOrder(Boolean isCancelOrder) {
		this.isCancelOrder = isCancelOrder;
	}

	public void setIsConfirmOrder(Boolean isConfirmOrder) {
		this.isConfirmOrder = isConfirmOrder;
	}

	public void setIsCheckout(Boolean isCheckout) {
		this.isCheckout = isCheckout;
	}
}
