package cn.globalph.b2c.order.domain.wrapper;

import cn.globalph.b2c.order.domain.wrap.EnumerationTypeWrap;
import cn.globalph.common.EnumerationType;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class EnumerationTypeWrapper implements APIWrapper<EnumerationType, EnumerationTypeWrap> {

    @Override
    public EnumerationTypeWrap wrapDetails(EnumerationType model) {
        if (model == null) 
        	return null;
        EnumerationTypeWrap wrap = new EnumerationTypeWrap();
        wrap.setFriendlyName( model.getFriendlyType() );
        wrap.setType( model.getType() );
        return wrap;
    }

    @Override
    public EnumerationTypeWrap wrapSummary(EnumerationType model) {
        return wrapDetails(model);
    }
}
