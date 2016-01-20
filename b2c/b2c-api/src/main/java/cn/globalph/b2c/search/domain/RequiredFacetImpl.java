package cn.globalph.b2c.search.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

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
@Table(name = "NPH_SEARCH_FACET_XREF")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
public class RequiredFacetImpl implements RequiredFacet {

    protected static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "RequiredFacetId")
    @GenericGenerator(
        name="RequiredFacetId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="RequiredFacetImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.search.domain.RequiredFacetImpl")
        }
    )
    @Column(name = "ID")
    protected Long id;

    @ManyToOne(targetEntity = SearchFacetImpl.class)
    @JoinColumn(name = "SEARCH_FACET_ID")
    protected SearchFacet searchFacet;

    @ManyToOne(targetEntity = SearchFacetImpl.class, optional=false)
    @JoinColumn(name = "REQUIRED_FACET_ID", referencedColumnName = "SEARCH_FACET_ID")
    protected SearchFacet requiredFacet;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public SearchFacet getRequiredFacet() {
        return requiredFacet;
    }

    @Override
    public void setRequiredFacet(SearchFacet requiredFacet) {
        this.requiredFacet = requiredFacet;
    }

    @Override
    public SearchFacet getSearchFacet() {
        return searchFacet;
    }

    @Override
    public void setSearchFacet(SearchFacet searchFacet) {
        this.searchFacet = searchFacet;
    }
}
