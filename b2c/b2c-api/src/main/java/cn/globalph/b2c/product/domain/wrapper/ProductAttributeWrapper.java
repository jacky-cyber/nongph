package cn.globalph.b2c.product.domain.wrapper;

import cn.globalph.b2c.product.domain.ProductAttribute;
import cn.globalph.b2c.product.domain.wrap.ProductAttributeWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class ProductAttributeWrapper implements APIWrapper<ProductAttribute, ProductAttributeWrap>{

    @Override
    public ProductAttributeWrap wrapDetails(ProductAttribute model) {
    	ProductAttributeWrap wrap = new ProductAttributeWrap();
    	wrap.setId( model.getId() );
    	wrap.setProductId( model.getProduct().getId() );
    	wrap.setAttributeName( model.getName() );
    	wrap.setAttributeValue( model.getValue() );
    	return wrap;
    }

    @Override
    public ProductAttributeWrap wrapSummary(ProductAttribute model) {
        return wrapDetails(model);
    }
}
