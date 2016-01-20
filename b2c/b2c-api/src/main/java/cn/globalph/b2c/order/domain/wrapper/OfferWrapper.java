package cn.globalph.b2c.order.domain.wrapper;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.order.domain.wrap.OfferWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class OfferWrapper implements APIWrapper<Offer, OfferWrap> {
	
	private EnumerationTypeWrapper enumerationTypeWrapper;
	
    @Override
    public OfferWrap wrapDetails(Offer model) {
    	OfferWrap wrap = wrapSummary(model);
    	wrap.setStartDate( model.getStartDate().toString() );
    	wrap.setEndDate( model.getStartDate().toString() );
    	wrap.setDescription( model.getDescription() );
    	wrap.setMaxUses( model.getMaxUses() );
    	return wrap;
    }

    @Override
    public OfferWrap wrapSummary(Offer model) {
    	OfferWrap wrap = new OfferWrap();
    	wrap.setAutomatic( model.isAutomaticallyApplied() );
    	wrap.setOfferType( enumerationTypeWrapper.wrapDetails(model.getType() ) );
    	wrap.setDiscountType( enumerationTypeWrapper.wrapDetails(model.getDiscountType() ) );
    	wrap.setOfferId( model.getId() );
    	wrap.setMarketingMessage( model.getMarketingMessage() );
    	wrap.setName( model.getName() );
    	return wrap;
    }
}
