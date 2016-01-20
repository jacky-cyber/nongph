package cn.globalph.b2c.product.domain.wrapper;

import cn.globalph.b2c.catalog.domain.RelatedProduct;
import cn.globalph.b2c.product.domain.wrap.RelatedProductWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class RelatedProductWrapper implements APIWrapper<RelatedProduct, RelatedProductWrap> {
	private ProductWrapper productWrapper;
	
    @Override
    public RelatedProductWrap wrapDetails(RelatedProduct model) {
    	RelatedProductWrap wrap = new RelatedProductWrap();
    	wrap.setId( model.getId() );
    	wrap.setSequence( model.getSequence() );
    	wrap.setPromotionalMessage( model.getPromotionMessage() );
        wrap.setProduct( productWrapper.wrapSummary(model.getRelatedProduct()) );
        return wrap;
    }

    @Override
    public RelatedProductWrap wrapSummary(RelatedProduct model) {
        return wrapDetails(model);
    }
}
