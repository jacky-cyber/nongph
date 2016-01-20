package cn.globalph.coupon.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerImpl;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "PH_CUSTOMER_COUPON")
@AdminPresentationClass(friendlyName = "用户优惠券",populateToOneFields = PopulateToOneFieldsEnum.TRUE)
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class CustomerCouponImpl implements CustomerCoupon {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@ManyToOne(targetEntity = CustomerImpl.class, optional = false)
	@JoinColumn(name = "CUSTOMER_ID")
	protected Customer customer;
	
	@ManyToOne(targetEntity = CouponImpl.class, optional = false)
	@JoinColumn(name = "COUPON_ID")
	protected Coupon coupon;
	
	@Column(name = "STATUS")
	protected Character status;
	
	@Column(name = "NUMBER")
	protected Integer number;
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setCutomer(Customer customer) {
		this.customer =  customer;
	}

	@Override
	public Customer getCustomer() {
		return this.customer;
	}

	@Override
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	@Override
	public Coupon getCoupon() {
		return this.coupon;
	}

	@Override
	public void setStatus(Character status) {
		this.status = status;
	}

	@Override
	public Character getStatus() {
		return this.status;
	}

	@Override
	public Integer getNumber() {
		return this.number;
	}

	@Override
	public void setNumber(Integer number) {
		this.number = number;
	}
}
