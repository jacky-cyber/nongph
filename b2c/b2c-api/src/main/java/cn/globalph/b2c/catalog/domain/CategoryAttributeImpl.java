package cn.globalph.b2c.catalog.domain;

import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.client.VisibilityEnum;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
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
@Table(name="NPH_CATEGORY_ATTRIBUTE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCategories")
@AdminPresentationClass(friendlyName = "baseCategoryAttribute")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps = true)
})
public class CategoryAttributeImpl implements CategoryAttribute {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator= "CategoryAttributeId")
    @GenericGenerator(
        name="CategoryAttributeId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="CategoryAttributeImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.catalog.domain.CategoryAttributeImpl")
        }
    )
    @Column(name = "CATEGORY_ATTRIBUTE_ID")
    protected Long id;
    
    @Column(name = "NAME", nullable=false)
    @Index(name="CATEGORYATTRIBUTE_NAME_INDEX", columnNames={"NAME"})
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    protected String name;

    @Column(name = "VALUE")
    @AdminPresentation(friendlyName = "ProductAttributeImpl_Attribute_Value", order=2, group = "ProductAttributeImpl_Description", prominent=true)
    protected String value;

    @Column(name = "SEARCHABLE")
    @AdminPresentation(excluded = true)
    protected Boolean searchable = false;
    
    @ManyToOne(targetEntity = CategoryImpl.class, optional=false)
    @JoinColumn(name = "CATEGORY_ID")
    @Index(name="CATEGORYATTRIBUTE_INDEX", columnNames={"CATEGORY_ID"})
    protected Category category;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Boolean getSearchable() {
        if (searchable == null) {
            return Boolean.FALSE;
        } else {
            return searchable;
        }
    }

    @Override
    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return value;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CategoryAttributeImpl other = (CategoryAttributeImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (category == null) {
            if (other.category != null)
                return false;
        } else if (!category.equals(other.category))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

}
