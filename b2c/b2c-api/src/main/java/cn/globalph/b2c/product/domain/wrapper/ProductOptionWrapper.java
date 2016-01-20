package cn.globalph.b2c.product.domain.wrapper;

import cn.globalph.b2c.product.domain.ProductOption;
import cn.globalph.b2c.product.domain.ProductOptionValue;
import cn.globalph.b2c.product.domain.wrap.ProductOptionValueWrap;
import cn.globalph.b2c.product.domain.wrap.ProductOptionWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

import java.util.ArrayList;
import java.util.List;

public class ProductOptionWrapper implements APIWrapper<ProductOption, ProductOptionWrap> {
    
	private ProductOptionValueWrapper productOptionValueWrapper;
	
    @Override
    public ProductOptionWrap wrapDetails(ProductOption model) {
    	ProductOptionWrap wrap = new ProductOptionWrap(); 
    	wrap.setAttributeName( "productOption." + model.getAttributeName() );
    	wrap.setLabel( model.getLabel() );
    	wrap.setRequired( model.getRequired() );
        if (model.getType() != null) {
            wrap.setProductOptionType( model.getType().getType() );
        }
        if (model.getProductOptionValidationStrategyType() != null) {
        	wrap.setProductOptionValidationStrategyType( model.getProductOptionValidationStrategyType().getType() );
        }
        if (model.getProductOptionValidationStrategyType() != null) {
            wrap.setProductOptionValidationType( model.getProductOptionValidationType().getType() );
        }
        wrap.setValidationString( model.getValidationString() ); 
        List<ProductOptionValue> optionValues = model.getAllowedValues();
        if (optionValues != null) {
            ArrayList<ProductOptionValueWrap> allowedValueWraps = new ArrayList<ProductOptionValueWrap>();
            for (ProductOptionValue value : optionValues) {
                allowedValueWraps.add( productOptionValueWrapper.wrapSummary(value));
            }
            wrap.setAllowedValues( allowedValueWraps );
        }
        return wrap;
    }

    @Override
    public ProductOptionWrap wrapSummary(ProductOption model) {
        return wrapDetails(model);
    }
}
