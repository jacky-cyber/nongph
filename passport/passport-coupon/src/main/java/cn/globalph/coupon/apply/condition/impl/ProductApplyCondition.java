package cn.globalph.coupon.apply.condition.impl;

import java.math.BigDecimal;

import cn.globalph.coupon.apply.condition.CouponApplyCondition;
import cn.globalph.coupon.type.CouponAttributeNameType;

public class ProductApplyCondition extends CouponApplyCondition<String> {
    public ProductApplyCondition(String value) {
        super(CouponAttributeNameType.PRODUCT.getType(), value);
    }

    @Override
    public boolean doVerify(String configMinProduct) {
    	int cn = configMinProduct.indexOf("|");
    	String cp = configMinProduct.substring(0, cn);
    	String cm = configMinProduct.substring(cn+1);
    	
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
