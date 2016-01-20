package cn.globalph.coupon.domain;

import java.io.Serializable;

import cn.globalph.coupon.type.CouponAttributeCategoryType;
import cn.globalph.coupon.type.CouponAttributeNameType;

public interface CouponAttribute extends Serializable{
    public Long getId();
    
    public void setId(Long id);
	
	public CouponAttributeCategoryType getCategory();

    public void setCategory(CouponAttributeCategoryType category);

    public CouponAttributeNameType getAttr();

    public void setAttr(CouponAttributeNameType attr);

    public String getAttrValue();

    public void setAttrValue(String attrValue);
    
    public Coupon getCoupon();
    
    public void setCoupon(Coupon coupon);
}
