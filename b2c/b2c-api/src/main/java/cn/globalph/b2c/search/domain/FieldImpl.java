package cn.globalph.b2c.search.domain;

import cn.globalph.b2c.search.solr.FieldType;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.client.VisibilityEnum;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_SEARCH_FIELD")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class FieldImpl implements Field,Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2915813511754425605L;

    @Id
    @GeneratedValue(generator = "FieldId")
    @GenericGenerator(
        name="FieldId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FieldImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.search.domain.FieldImpl")
        }
    )
    @Column(name = "FIELD_ID")
    @AdminPresentation(friendlyName = "FieldImpl_ID", group = "FieldImpl_descrpition",visibility=VisibilityEnum.HIDDEN_ALL)
    protected Long id;
    
    // This is a broadleaf enumeration
    @AdminPresentation(friendlyName = "FieldImpl_EntityType", group = "FieldImpl_descrpition", order = 2, prominent = true)
    @Column(name = "ENTITY_TYPE", nullable = false)
    @Index(name="ENTITY_TYPE_INDEX", columnNames={"ENTITY_TYPE"})
    protected String entityType;
    
    @Column(name = "PROPERTY_NAME", nullable = false)
    @AdminPresentation(friendlyName = "FieldImpl_propertyName", group = "FieldImpl_descrpition", order = 1, prominent = true)
    protected String propertyName;
    
    @Column(name = "ABBREVIATION")
    @AdminPresentation(friendlyName = "FieldImpl_abbreviation", group = "FieldImpl_descrpition", order = 3, prominent = true)
    protected String abbreviation;
    
    @Column(name = "SEARCHABLE")
    @AdminPresentation(friendlyName = "FieldImpl_searchable", group = "FieldImpl_descrpition", order = 4, prominent = true)
    protected Boolean searchable = false;
    
    // This is a broadleaf enumeration
    @Column(name = "FACET_FIELD_TYPE")
    @AdminPresentation(friendlyName = "FieldImpl_facetFieldType", group = "FieldImpl_descrpition", excluded = true)
    protected String facetFieldType;

    // This is a broadleaf enumeration
    @ElementCollection
    @CollectionTable(name="NPH_SEARCH_FIELD_TYPES", joinColumns=@JoinColumn(name="FIELD_ID"))
    @Column(name="SEARCHABLE_FIELD_TYPE")
    @Cascade(value={org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    @BatchSize(size = 50)
    protected List<String> searchableFieldTypes = new ArrayList<String>();
    
    @Column(name = "TRANSLATABLE")
    @AdminPresentation(friendlyName = "FieldImpl_translatable", group = "FieldImpl_description")
    protected Boolean translatable = false;
    
    @Override
    public String getQualifiedFieldName() {
        return getEntityType().getFriendlyType() + "." + propertyName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public FieldEntity getEntityType() {
        return FieldEntity.getInstance(entityType);
    }

    @Override
    public void setEntityType(FieldEntity entityType) {
        this.entityType = entityType.getType();
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String getAbbreviation() {
        return abbreviation;
    }

    @Override
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public Boolean getSearchable() {
        return searchable;
    }

    @Override
    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }
    
    @Override
    public FieldType getFacetFieldType() {
        return FieldType.getInstance(facetFieldType);
    }

    @Override
    public void setFacetFieldType(FieldType facetFieldType) {
        this.facetFieldType = facetFieldType == null ? null : facetFieldType.getType();
    }

    @Override
    public List<FieldType> getSearchableFieldTypes() {
        List<FieldType> fieldTypes = new ArrayList<FieldType>();
        for (String fieldType : searchableFieldTypes) {
            fieldTypes.add(FieldType.getInstance(fieldType));
        }
        return fieldTypes;
    }

    @Override
    public void setSearchableFieldTypes(List<FieldType> searchableFieldTypes) {
        List<String> fieldTypes = new ArrayList<String>();
        for (FieldType fieldType : searchableFieldTypes) {
            fieldTypes.add(fieldType.getType());
        }
        this.searchableFieldTypes = fieldTypes;
    }
    
    @Override
    public Boolean getTranslatable() {
        return translatable == null ? false : translatable;
    }

    @Override
    public void setTranslatable(Boolean translatable) {
        this.translatable = translatable;
    }

    @Override
    public List<SearchConfig> getSearchConfigs() {
        throw new UnsupportedOperationException("The default Field implementation does not support search configs");
    }

    @Override
    public void setSearchConfigs(List<SearchConfig> searchConfigs) {
        throw new UnsupportedOperationException("The default Field implementation does not support search configs");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Field other = (Field) obj;
        
        return getEntityType().getType().equals(other.getEntityType().getType()) && getPropertyName().equals(other.getPropertyName());
                
    }
}
