package cn.globalph.b2c.order.domain.wrapper;

import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.order.domain.wrap.FulfillmentOptionWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class FulfillmentOptionWrapper implements APIWrapper<FulfillmentOption, FulfillmentOptionWrap> {
	
	private EnumerationTypeWrapper enumerationTypeWrapper;
	
    @Override
    public FulfillmentOptionWrap wrapDetails(FulfillmentOption model) {
    	FulfillmentOptionWrap wrap = new FulfillmentOptionWrap();
    	wrap.setId( model.getId() );
        if (model.getFulfillmentType() != null) {
            wrap.setFulfillmentType( enumerationTypeWrapper.wrapDetails(model.getFulfillmentType()) );
        }
        wrap.setName( model.getName() );
        wrap.setDescription( model.getLongDescription() );
        return wrap;
    }

    @Override
    public FulfillmentOptionWrap wrapSummary(FulfillmentOption model) {
        return wrapDetails(model);
    }
}
