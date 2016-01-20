package cn.globalph.b2c.search.domain.wrapper;

import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.wrap.ProductWrap;
import cn.globalph.b2c.product.domain.wrapper.ProductWrapper;
import cn.globalph.b2c.search.domain.SearchFacetDTO;
import cn.globalph.b2c.search.domain.SkuSearchResult;
import cn.globalph.b2c.search.domain.wrap.SearchFacetWrap;
import cn.globalph.b2c.search.domain.wrap.SearchResultsWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsWrapper implements APIWrapper<SkuSearchResult, SearchResultsWrap> {
	
	private ProductWrapper productWrapper;
	
	private SearchFacetWrapper facetWrapper;
	
    @Override
    public SearchResultsWrap wrapDetails(SkuSearchResult model) {
    	SearchResultsWrap wrap = new SearchResultsWrap();
    	wrap.setPage( model.getPage() );
        wrap.setPageSize( model.getPageSize() );
        wrap.setTotalResults( model.getTotalResults() );
        wrap.setTotalPages( model.getTotalPages() );

        if (model.getSkus() != null) {
            List<ProductWrap> products = new ArrayList<ProductWrap>();
            for (Sku sku : model.getSkus()) {
                products.add( productWrapper.wrapSummary(sku.getProduct() ) );
            }
            wrap.setProducts(products);
        }

        if (model.getFacets() != null) {
            List<SearchFacetWrap> searchFacets = new ArrayList<SearchFacetWrap>();
            for (SearchFacetDTO facet : model.getFacets()) {
                searchFacets.add( facetWrapper.wrapSummary(facet) );
            }
            wrap.setSearchFacets(searchFacets);
        }
        return wrap;
    }

    @Override
    public SearchResultsWrap wrapSummary(SkuSearchResult model) {
        return wrapDetails(model);
    }
}
