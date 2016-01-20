package cn.globalph.b2c.catalog.domain.wrapper;

import cn.globalph.b2c.catalog.domain.CategoryAttribute;
import cn.globalph.b2c.catalog.domain.wrap.CategoryAttributeWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class CategoryAttributeWrapper implements APIWrapper<CategoryAttribute, CategoryAttributeWrap>{

    @Override
    public CategoryAttributeWrap wrapDetails(CategoryAttribute model) {
    	CategoryAttributeWrap wrap = new CategoryAttributeWrap();
    	wrap.setId( model.getId() );
    	wrap.setCategoryId( model.getCategory().getId() );
    	wrap.setAttributeName( model.getName() );
    	wrap.setAttributeValue( model.getValue() );
    	return wrap;
    }

    @Override
    public CategoryAttributeWrap wrapSummary(CategoryAttribute model) {
        return wrapDetails(model);
    }
}
