package cn.globalph.b2c.search.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_CAT_SEARCH_FACET_VALUE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blCategories")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE)
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class CategorySearchFacetValueImpl implements CategorySearchFacetValue{
	 private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "CategorySearchFacetValueId")
    @GenericGenerator(
        name="CategorySearchFacetValueId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="CategorySearchFacetValueImpl"),
            @Parameter(name="entity_name", value="cn.globalph.search.domain.CategorySearchFacetValueImpl")
        }
    )
    @Column(name = "CAT_SEARCH_FACET_VALUE_ID")
	protected Long id; 
    
    @Column(name="VALUE")
    @AdminPresentation(excluded = true)
    protected String value;
    
    @ManyToOne(targetEntity = CategorySearchFacetImpl.class)
    @JoinColumn(name = "CAT_SEARCH_FACET_XREF_ID")
    @AdminPresentation(excluded = true)
    protected CategorySearchFacet categorySearchFacet;
    
    @Column(name = "SEARCH_FIELD_ID")
    protected Long searchFieldId;
	
    @Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public CategorySearchFacet getCategorySearchFacet() {
		return this.categorySearchFacet;
	}

	@Override
	public void setCategorySearchFacet(CategorySearchFacet categorySearchFacet) {
		this.categorySearchFacet = categorySearchFacet;
	}

	@Override
	public Long getSearchFieldId() {
		return this.searchFieldId;
	}

	@Override
	public void setSearchFieldId(Long searchFieldId) {
		this.searchFieldId = searchFieldId;
	}
	
}
