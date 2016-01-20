package cn.globalph.b2c.order.domain.wrapper;

import cn.globalph.b2c.order.domain.wrap.CartMessageWrap;
import cn.globalph.b2c.order.service.call.ActivityMessageDTO;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class CartMessageWrapper implements APIWrapper<ActivityMessageDTO, CartMessageWrap> {

    @Override
    public CartMessageWrap wrapDetails(ActivityMessageDTO model) {
    	CartMessageWrap wrap = new CartMessageWrap();
    	wrap.setMessage( model.getMessage() );
    	wrap.setPriority( model.getPriority() );
    	wrap.setMessageType( model.getType() );
    	wrap.setErrorCode( model.getErrorCode() );
    	return wrap;
    }

    @Override
    public CartMessageWrap wrapSummary(ActivityMessageDTO model) {
        return wrapDetails(model);
    }

}
