package cn.globalph.cms.structure.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CMS_SC_TYPE")
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blCMSElements")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "StructuredContentTypeImpl_baseStructuredContentType")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class StructuredContentTypeImpl implements StructuredContentType, AdminMainEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "StructuredContentTypeId")
    @GenericGenerator(
        name="StructuredContentTypeId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="StructuredContentTypeImpl"),
            @Parameter(name="entity_name", value="cn.globalph.cms.structure.domain.StructuredContentTypeImpl")
        }
    )
    @Column(name = "SC_TYPE_ID")
    protected Long id;

    @Column (name = "NAME")
    @AdminPresentation(friendlyName = "StructuredContentTypeImpl_Name", order = 1, gridOrder = 1, group = "StructuredContentTypeImpl_Details", prominent = true)
    @Index(name="SC_TYPE_NAME_INDEX", columnNames={"NAME"})
    protected String name;

    @Column (name = "DESCRIPTION")
    protected String description;

    @ManyToOne(targetEntity = StructuredContentFieldTemplateImpl.class)
    @JoinColumn(name="SC_FLD_TMPLT_ID")
    protected StructuredContentFieldTemplate structuredContentFieldTemplate;

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
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public StructuredContentFieldTemplate getStructuredContentFieldTemplate() {
        return structuredContentFieldTemplate;
    }

    @Override
    public void setStructuredContentFieldTemplate(StructuredContentFieldTemplate scft) {
        this.structuredContentFieldTemplate = scft;
    }

    @Override
    public String getMainEntityName() {
        return getName();
    }
}

