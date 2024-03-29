package cn.globalph.b2c.search.domain;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.common.presentation.override.AdminPresentationOverride;
import cn.globalph.common.presentation.override.AdminPresentationOverrides;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_SEARCH_FACET_RANGE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE)
@AdminPresentationOverrides({
        @AdminPresentationOverride(name = "priceList.friendlyName", value = @AdminPresentation(excluded = false, friendlyName = "PriceListImpl_Friendly_Name", order=1, group = "SearchFacetRangeImpl_Description", prominent=true, visibility = VisibilityEnum.FORM_HIDDEN))
})
public class SearchFacetRangeImpl implements SearchFacetRange,Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "SearchFacetRangeId")
    @GenericGenerator(
        name="SearchFacetRangeId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="SearchFacetRangeImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.search.domain.SearchFacetRangeImpl")
        }
    )
    @Column(name = "SEARCH_FACET_RANGE_ID")
    protected Long id;
    
    @ManyToOne(targetEntity = SearchFacetImpl.class)
    @JoinColumn(name = "SEARCH_FACET_ID")
    @Index(name="SEARCH_FACET_INDEX", columnNames={"SEARCH_FACET_ID"})
    @AdminPresentation(excluded = true, visibility = VisibilityEnum.HIDDEN_ALL)
    protected SearchFacet searchFacet = new SearchFacetImpl();
    
    @Column(name = "MIN_VALUE", precision=19, scale=5, nullable = false) 
    @AdminPresentation(friendlyName = "SearchFacetRangeImpl_minValue", order=2, group = "SearchFacetRangeImpl_Description", prominent=true)
    protected BigDecimal minValue;
    
    @Column(name = "MAX_VALUE", precision=19, scale=5)
    @AdminPresentation(friendlyName = "SearchFacetRangeImpl_maxValue", order=3, group = "SearchFacetRangeImpl_Description", prominent=true)
    protected BigDecimal maxValue;
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public SearchFacet getSearchFacet() {
        return searchFacet;
    }

    @Override
    public void setSearchFacet(SearchFacet searchFacet) {
        this.searchFacet = searchFacet;
    }

    @Override
    public BigDecimal getMinValue() {
        return minValue;
    }

    @Override
    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    @Override
    public BigDecimal getMaxValue() {
        return maxValue;
    }

    @Override
    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }
    
}
