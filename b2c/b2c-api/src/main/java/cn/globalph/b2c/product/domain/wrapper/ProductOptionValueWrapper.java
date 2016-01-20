package cn.globalph.b2c.product.domain.wrapper;

import cn.globalph.b2c.product.domain.ProductOptionValue;
import cn.globalph.b2c.product.domain.wrap.ProductOptionValueWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class ProductOptionValueWrapper implements APIWrapper<ProductOptionValue, ProductOptionValueWrap> {
        
    @Override
    public ProductOptionValueWrap wrapDetails(ProductOptionValue model) {
    	ProductOptionValueWrap wrap = new ProductOptionValueWrap();
    	wrap.setAttributeValue( model.getAttributeValue() );
    	wrap.setPriceAdjustment( model.getPriceAdjustment() );
    	wrap.setProductOptionId( model.getProductOption().getId() );
    	return wrap;
    }

    @Override
    public ProductOptionValueWrap wrapSummary(ProductOptionValue model) {
        return wrapDetails(model);
    }
}
