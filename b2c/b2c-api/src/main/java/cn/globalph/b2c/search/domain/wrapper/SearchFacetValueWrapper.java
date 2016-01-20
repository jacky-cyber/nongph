package cn.globalph.b2c.search.domain.wrapper;

import cn.globalph.b2c.search.domain.SearchFacetResultDTO;
import cn.globalph.b2c.search.domain.wrap.SearchFacetValueWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class SearchFacetValueWrapper implements APIWrapper<SearchFacetResultDTO,SearchFacetValueWrap> {

    @Override
    public SearchFacetValueWrap wrapDetails(SearchFacetResultDTO model) {
    	SearchFacetValueWrap wrap = new SearchFacetValueWrap();
    	wrap.setActive( model.isActive() );
    	wrap.setValueKey( model.getValueKey() );
    	wrap.setQuantity( model.getQuantity() );
    	wrap.setValue( model.getValue() );
    	wrap.setMinValue( model.getMinValue() );
    	wrap.setMaxValue( model.getMaxValue() );
    	return wrap;
    }

    @Override
    public SearchFacetValueWrap wrapSummary(SearchFacetResultDTO model) {
        return wrapDetails(model);
    }
}
