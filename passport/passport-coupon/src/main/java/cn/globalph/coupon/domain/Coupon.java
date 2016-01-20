package cn.globalph.coupon.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.globalph.coupon.issue.event.CouponIssueEventType;
import cn.globalph.coupon.type.CouponStrategyType;

public interface Coupon extends Serializable{
	public Long getId();

    public void setId(Long id);

    public CouponStrategyType getStrategy();

    public void setStrategy(CouponStrategyType strategy);

    public Date getStartDate();

    public void setStartDate(Date startDate);

    public Date getEndDate();

    public void setEndDate(Date endDate);

    public String getDesc();

    public void setDesc(String desc);
   
    public CouponIssueEventType getEvent();

    public void setEvent(CouponIssueEventType event);

    public int getIssuePriority();

    public void setIssuePriority(int issuePriority);

    public boolean isValidPeriod(Date checkDate);

    public int getNumber();

    public void setNumber(int number);

    public BigDecimal getValue();

    public void setValue(BigDecimal value);

    public List<CouponAttribute> getCouponAttributes();

    public void setCouponAttributes(List<CouponAttribute> couponAttributes);

    public Coupon addCouponAttribute(CouponAttribute couponAttribute);

    public String getAttrValue(String category, String attrName);
    
    public String getMinPayAmount();
    
    public String getScope();
    
    public void setScope(String scope);
}
