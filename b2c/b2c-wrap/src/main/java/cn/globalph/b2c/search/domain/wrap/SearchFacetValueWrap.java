package cn.globalph.b2c.search.domain.wrap;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "searchFacetValue")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SearchFacetValueWrap{

    /*
     * Indicates if this facet value was active in a current search.
     */
    @XmlElement
    protected Boolean active = Boolean.FALSE;

    /*
     * A value. If the min and max values are populated, this should be null.
     */
    @XmlElement
    protected String value;

    /*
     * The value that should be passed in when using a search facet to filter a search. 
     * For example, a value key may be something like: "range[0.00000:5.00000]". This would 
     * be the value passed in as a query parameter (e.g. price=range[0.00000:5.00000]).  Or this could 
     * be a simple value if not min and max values are used.
     */
    @XmlElement
    protected String valueKey;

    /*
     * Indicates how many results are associated with this facet value.
     */
    @XmlElement
    protected Integer quantity;

    /*
     * Min value of a range. Should be null if value is not null.
     */
    @XmlElement
    protected BigDecimal minValue;

    /*
     * Max value of a range. Should be null if value is not null.
     */
    @XmlElement
    protected BigDecimal maxValue;

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueKey() {
		return valueKey;
	}

	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getMinValue() {
		return minValue;
	}

	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}
}
