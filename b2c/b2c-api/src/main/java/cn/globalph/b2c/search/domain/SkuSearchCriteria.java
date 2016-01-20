package cn.globalph.b2c.search.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Container that holds additional criteria to consider when performing searches for Products
 * 
 * @author felix.wu
 */
public class SkuSearchCriteria {
    
    public static String PAGE_SIZE_STRING = "pageSize";
    public static String PAGE_NUMBER = "page";
    public static String SORT_STRING = "sort";
    public static String QUERY_STRING = "q";
    
    protected Integer page = 1;
    protected Integer pageSize;
    protected String sortQuery;
    protected Integer totalPage;
    
    public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	protected Map<String, String[]> filterCriteria = new HashMap<String, String[]>();
    
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

    public String getSortQuery() {
        return sortQuery;
    }
    
    public void setSortQuery(String sortQuery) {
        this.sortQuery = sortQuery;
    }

    public Map<String, String[]> getFilterCriteria() {
        return filterCriteria;
    }

    public void setFilterCriteria(Map<String, String[]> filterCriteria) {
        this.filterCriteria = filterCriteria;
    }
    
}
