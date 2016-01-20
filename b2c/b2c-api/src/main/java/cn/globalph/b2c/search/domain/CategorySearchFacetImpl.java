package cn.globalph.b2c.search.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.CategoryImpl;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.AddMethodType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_CAT_SEARCH_FACET_XREF")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blCategories")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE)
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class CategorySearchFacetImpl implements CategorySearchFacet {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "CategorySearchFacetId")
    @GenericGenerator(
        name="CategorySearchFacetId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="CategorySearchFacetImpl"),
            @Parameter(name="entity_name", value="cn.globalph.search.domain.CategorySearchFacetImpl")
        }
    )
    @Column(name = "CATEGORY_SEARCH_FACET_ID")
    protected Long id;
    
    @ManyToOne(targetEntity = CategoryImpl.class)
    @JoinColumn(name = "CATEGORY_ID")
    @AdminPresentation(excluded = true)
    protected Category category;
    
    @ManyToOne(targetEntity = SearchFacetImpl.class)
    @JoinColumn(name = "SEARCH_FACET_ID")
    protected SearchFacet searchFacet;
    
    @Column(name = "SEQUENCE")
    @AdminPresentation(friendlyName = "CategorySearchFacetImpl_sequence")
    protected BigDecimal sequence;

    @OneToMany(mappedBy = "categorySearchFacet", targetEntity = CategorySearchFacetValueImpl.class, cascade = {CascadeType.ALL})
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    @AdminPresentationCollection(addType = AddMethodType.PERSIST, friendlyName = "newRangeTitle")
    protected List<CategorySearchFacetValue> categorySearchFacetValues = new ArrayList<CategorySearchFacetValue>();
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public void setCategory(Category category) {
        this.category = category;
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
    public BigDecimal getSequence() {
        return sequence;
    }

    @Override
    public void setSequence(BigDecimal sequence) {
        this.sequence = sequence;
    }

	@Override
	public List<CategorySearchFacetValue> getCategorySearchFacetValues() {
		return this.categorySearchFacetValues;
	}

	@Override
	public void setCategorySearchFacetValues(
			List<CategorySearchFacetValue> categorySearchFacetValues) {
		this.categorySearchFacetValues = categorySearchFacetValues;
	}
}
