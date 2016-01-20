package cn.globalph.b2c.product.domain.wrapper;

import cn.globalph.b2c.product.domain.SkuAttribute;
import cn.globalph.b2c.product.domain.wrap.SkuAttributeWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class SkuAttributeWrapper implements APIWrapper<SkuAttribute, SkuAttributeWrap>{

    @Override
    public SkuAttributeWrap wrapDetails(SkuAttribute model) {
    	SkuAttributeWrap wrap = new SkuAttributeWrap();
    	wrap.setId( model.getId() );
    	wrap.setSkuId( model.getSku().getId() );
    	wrap.setAttributeName( model.getName() );
    	wrap.setAttributeValue( model.getValue() );
    	return wrap;
    }

    @Override
    public SkuAttributeWrap wrapSummary(SkuAttribute model) {
        return wrapDetails(model);
    }
}
