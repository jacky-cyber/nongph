package cn.globalph.b2c.coupon.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import cn.globalph.b2c.coupon.type.CouponCodeType;
import cn.globalph.coupon.domain.Coupon;
import cn.globalph.coupon.domain.CouponImpl;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerImpl;

@Entity
@Table(name = "PH_COUPON_CODE")
public class CouponCodeImpl implements CouponCode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COUPON_CODE_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(targetEntity = CustomerImpl.class)
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;
	
	@OneToOne(targetEntity = CouponImpl.class)
	@JoinColumn(name = "COUPON_ID")
	private Coupon coupon;
	
	@Column(name = "START_DATE")
	private Date startDate;
	
	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "COUPON_AMOUNT", scale = 9, precision = 3)
	private BigDecimal couponAmount;
	
	@Column(name = "COUPON_AMOUNT_TWO", scale = 9, precision = 3)
	private BigDecimal couponAmountTwo;
	
	@Column(name = "COUPON_CODE")
	private String couponCode;
	
	@Column(name = "STATUS")
	private String status;
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Customer getCustomer() {
		return this.customer;
	}

	@Override
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public Date getStartDate() {
		return this.startDate;
	}

	@Override
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public Date getEndDate() {
		return this.endDate;
	}

	@Override
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public Boolean isValid() {
		Date now = new Date(System.currentTimeMillis());
		return (startDate == null || now.after(startDate)) && (endDate == null || now.before(endDate));
	}

	@Override
	public CouponCodeType getStatus() {
		return CouponCodeType.getInstance(status);
	}

	@Override
	public void setStatus(CouponCodeType status) {
		this.status = status.getType();
	}

	@Override
	public String getCouponCode() {
		return this.couponCode;
	}

	@Override
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	@Override
	public BigDecimal getCouponAmount() {
		return this.couponAmount;
	}

	@Override
	public void setCouponAmount(BigDecimal couponAmount) {
		this.couponAmount = couponAmount;
	}

	@Override
	public BigDecimal getCouponAmountTwo() {
		return this.couponAmountTwo;
	}

	@Override
	public void setCouponAmountTwo(BigDecimal couponAmountTwo) {
		this.couponAmountTwo = couponAmountTwo;
	}
	
	@Override
	public BigDecimal getDiscountAmount(Long customerId){
		if(getCustomer().getId().equals(customerId)){
			return this.couponAmount;
		}else{
			return this.couponAmountTwo;
		}
	}

	@Override
	public Coupon getCoupon() {
		return this.coupon;
	}

	@Override
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}
}
