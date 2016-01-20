package cn.globalph.b2c.search.domain;

import java.io.Serializable;

/**
 * @author qiutian
 */
public interface CategorySearchFacetValue extends Serializable{
	public Long getId();
	public void setId(Long id);
	public String getValue();
	public void setValue(String value);
	public CategorySearchFacet getCategorySearchFacet();
	public void setCategorySearchFacet(CategorySearchFacet categorySearchFacet);
	public Long getSearchFieldId();
	public void setSearchFieldId(Long searchFieldId);
}
