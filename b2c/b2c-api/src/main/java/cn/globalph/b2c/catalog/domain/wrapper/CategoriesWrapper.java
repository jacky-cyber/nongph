package cn.globalph.b2c.catalog.domain.wrapper;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.wrap.CategoriesWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

import java.util.List;

public class CategoriesWrapper implements APIWrapper<List<Category>, CategoriesWrap> {

    private CategoryWrapper categoryWrapper;

    @Override
    public  CategoriesWrap wrapDetails(List<Category> cats) {
    	CategoriesWrap wrap = new CategoriesWrap();
        for (Category category : cats) {
            wrap.getCategories().add( categoryWrapper.wrapSummary(category) );
        }
        return wrap;
    }

    @Override
    public CategoriesWrap wrapSummary(List<Category> cats) {
        return wrapDetails(cats);
    }
}
