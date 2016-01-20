package cn.globalph.coupon.issue.event;

import cn.globalph.coupon.domain.Coupon;
import cn.globalph.coupon.issue.event.condition.CouponIssueCondition;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class CouponIssueEvent extends ApplicationEvent {
    private String name;
    private boolean isSupportedPriority;
    private Integer number = 0;
    public CouponIssueEvent(String name, CouponIssueEventSource source) {
        super(source);
        source.setCouponIssueEvent(this);
        this.name = name;
        this.isSupportedPriority = true;
        
    }
    
    public CouponIssueEvent(String name, boolean isSupportPriority, CouponIssueEventSource source) {
        super(source);
        source.setCouponIssueEvent(this);
        this.name = name;
        this.isSupportedPriority = isSupportPriority;
        
    }
    
    public CouponIssueEvent(String name, Integer number, CouponIssueEventSource source) {
        super(source);
        source.setCouponIssueEvent(this);
        this.name = name;
        this.isSupportedPriority = true;
        this.number = number;
        
    }

    public boolean isSupportedEvent(Coupon coupon){
        return (coupon.getEvent().getType().equals(name) &&
                coupon.isValidPeriod(getSource().getCouponEventDate())) &&
                checkEventCondition(coupon);
    }

    private boolean checkEventCondition(Coupon coupon){
        List<CouponIssueCondition> conditions = getSource().getConditions();
        for(CouponIssueCondition condition : conditions){
            if(!condition.verify(coupon)){
                return false;
            }
        }
        return true;
    }

    @Override
    public CouponIssueEventSource getSource() {
        return (CouponIssueEventSource)super.getSource();
    }

    public String getName() {
        return name;
    }
    
    public boolean isSupportedPriority(){
    	return isSupportedPriority;
    }
    
    public Integer getNumber(){
    	return this.number;
    }
}
