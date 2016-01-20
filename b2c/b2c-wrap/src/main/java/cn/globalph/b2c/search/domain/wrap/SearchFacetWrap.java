package cn.globalph.b2c.search.domain.wrap;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This wrapper provides information about the search facets available 
 * for use with a search.
 * 
 * Search facets are typically returned from a catalog search as part of the result. 
 * You can use facets to narrow a search.
 * 
 */
@XmlRootElement(name = "searchFacet")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SearchFacetWrap{

    /*
     * Name of the field. (e.g. you might have a field name 
     * like "price" or "description")
     */
    @XmlElement
    protected String fieldName;

    /*
     * Indicates if this facet is active in the current search.
     */
    @XmlElement
    protected Boolean active = Boolean.FALSE;

    /*
     * List of values that are appropriate for this facet.
     */
    @XmlElement
    protected List<SearchFacetValueWrap> values;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<SearchFacetValueWrap> getValues() {
		return values;
	}

	public void setValues(List<SearchFacetValueWrap> values) {
		this.values = values;
	}
}
