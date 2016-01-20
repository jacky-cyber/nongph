package cn.globalph.b2c.offer.domain.wrapper;

import cn.globalph.b2c.offer.domain.Adjustment;
import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.wrap.AdjustmentWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class AdjustmentWrapper implements APIWrapper<Adjustment, AdjustmentWrap> {

    public AdjustmentWrap wrapDetails(Adjustment model) {
        if (model == null) {
            return null;
        }
        AdjustmentWrap wrap = new AdjustmentWrap();
        wrap.setId( model.getId() );
        wrap.setReason( model.getReason() );

        Offer offer = model.getOffer();
        if (offer != null) {
            if (model.getReason() == null) {
                wrap.setReason( "OFFER" );
            }
            wrap.setOfferid( offer.getId() );
            wrap.setMarketingMessage( offer.getMarketingMessage() );
            wrap.setDiscountType( offer.getDiscountType().getType() );
            wrap.setDiscountAmount( offer.getValue() );
        }

        wrap.setAdjustmentValue( model.getValue() );
        return wrap;
    }

    @Override
    public AdjustmentWrap wrapSummary(Adjustment model) {
        return wrapDetails(model);
    }
}
