package cn.globalph.cms.field.domain;

import cn.globalph.common.extensibility.jpa.clone.ClonePolicyCollection;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Created by bpolster.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CMS_FLD_GROUP")
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blCMSElements")
public class FieldGroupImpl implements FieldGroup {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "FieldGroupId")
    @GenericGenerator(
        name="FieldGroupId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FieldGroupImpl"),
            @Parameter(name="entity_name", value="cn.globalph.cms.field.domain.FieldGroupImpl")
        }
    )
    @Column(name = "FLD_GROUP_ID")
    protected Long id;

    @Column (name = "NAME")
    protected String name;

    @Column (name = "INIT_COLLAPSED_FLAG")
    protected Boolean initCollapsedFlag = false;

    @OneToMany(mappedBy = "fieldGroup", targetEntity = FieldDefinitionImpl.class, cascade = {CascadeType.ALL})
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCMSElements")
    @OrderBy("fieldOrder")
    @BatchSize(size = 20)
    @ClonePolicyCollection(deepClone = false)
    protected List<FieldDefinition> fieldDefinitions = new ArrayList<FieldDefinition>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
    public Boolean getInitCollapsedFlag() {
        return initCollapsedFlag;
    }

    @Override
    public void setInitCollapsedFlag(Boolean initCollapsedFlag) {
        this.initCollapsedFlag = initCollapsedFlag;
    }

    @Override
    public List<FieldDefinition> getFieldDefinitions() {
        return fieldDefinitions;
    }

    @Override
    public void setFieldDefinitions(List<FieldDefinition> fieldDefinitions) {
        this.fieldDefinitions = fieldDefinitions;
    }

}

