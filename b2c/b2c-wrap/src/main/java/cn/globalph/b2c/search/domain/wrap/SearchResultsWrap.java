package cn.globalph.b2c.search.domain.wrap;

import cn.globalph.b2c.product.domain.wrap.ProductWrap;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "searchResults")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SearchResultsWrap{

    @XmlElement
    protected Integer page;

    /*
     * Indicates the requested or default page size.
     */
    @XmlElement
    protected Integer pageSize;

    /*
     * Indicates the actual results
     */
    @XmlElement
    protected Integer totalResults;

    /*
     * Indicates the number of pages
     */
    @XmlElement
    protected Integer totalPages;

    /*
     * List of products associated with a search
     */
    @XmlElementWrapper(name = "products")
    @XmlElement(name = "product")
    protected List<ProductWrap> products;

    /*
     * List of available facets to be used for searching
     */
    @XmlElementWrapper(name = "searchFacets")
    @XmlElement(name = "searchFacet")
    protected List<SearchFacetWrap> searchFacets;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public List<ProductWrap> getProducts() {
		return products;
	}

	public void setProducts(List<ProductWrap> products) {
		this.products = products;
	}

	public List<SearchFacetWrap> getSearchFacets() {
		return searchFacets;
	}

	public void setSearchFacets(List<SearchFacetWrap> searchFacets) {
		this.searchFacets = searchFacets;
	}
}
