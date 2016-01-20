package cn.globalph.b2c.search.domain.wrapper;

import cn.globalph.b2c.search.domain.SearchFacetDTO;
import cn.globalph.b2c.search.domain.SearchFacetResultDTO;
import cn.globalph.b2c.search.domain.wrap.SearchFacetValueWrap;
import cn.globalph.b2c.search.domain.wrap.SearchFacetWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

import java.util.ArrayList;
import java.util.List;

public class SearchFacetWrapper implements APIWrapper<SearchFacetDTO, SearchFacetWrap> {

	private SearchFacetValueWrapper searchFacetValueWrapper;
	
    @Override
    public SearchFacetWrap wrapDetails(SearchFacetDTO model) {
    	SearchFacetWrap wrap = new SearchFacetWrap();
    	wrap.setFieldName( model.getFacet().getField().getAbbreviation() );
    	wrap.setActive( model.isActive() );

        if (model.getFacetValues() != null) {
        	List<SearchFacetValueWrap> values = new ArrayList<SearchFacetValueWrap>();
            for (SearchFacetResultDTO result : model.getFacetValues()) {
                values.add( searchFacetValueWrapper.wrapSummary(result) );
            }
            wrap.setValues(values);
        }
        return wrap;
    }

    @Override
    public SearchFacetWrap wrapSummary(SearchFacetDTO model) {
        return wrapDetails(model);
    }
}
