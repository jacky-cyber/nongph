package cn.globalph.coupon.apply.condition.impl;

import java.math.BigDecimal;

import cn.globalph.coupon.apply.condition.CouponApplyCondition;
import cn.globalph.coupon.type.CouponAttributeNameType;

public class CategoryApplyCondition extends CouponApplyCondition<String> {
    public CategoryApplyCondition(String value) {
        super(CouponAttributeNameType.CATEGORY.getType(), value);
    }

    @Override
    public boolean doVerify(String configMinCategory) {
        int cn = configMinCategory.indexOf("|");
    	String cp = configMinCategory.substring(0, cn);
    	String cm = configMinCategory.substring(cn+1);
    	
    	if(getValue().indexOf("|") != -1 ){
	    	int n = getValue().indexOf("|");
	    	String p = getValue().substring(0, n);
	    	String m = getValue().substring(n + 1);
	    	
	    	BigDecimal bm = new BigDecimal(m);
	    	BigDecimal bcm = new BigDecimal(cm);
	    	
	        return !p.equals(cp) || (p.equals(cp) && bm.compareTo(bcm) >=0);
    	}else if(getValue().indexOf(",") != -1){
    		String[] ids= getValue().split(",");
    		for(String id : ids){
    			if(id.equals(cp)) return true;
    		}
    		return false;
    	}else{
    		return true;
    	}
    }
}