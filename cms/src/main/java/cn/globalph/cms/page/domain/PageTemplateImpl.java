package cn.globalph.cms.page.domain;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import cn.globalph.cms.field.domain.FieldGroup;
import cn.globalph.cms.field.domain.FieldGroupImpl;
import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyCollection;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.VisibilityEnum;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CMS_PAGE_TMPLT")
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blCMSElements")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "PageTemplateImpl_basePageTemplate")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class PageTemplateImpl implements PageTemplate, AdminMainEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "PageTemplateId")
    @GenericGenerator(
        name="PageTemplateId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="PageTemplateImpl"),
            @Parameter(name="entity_name", value="cn.globalph.cms.page.domain.PageTemplateImpl")
        }
    )
    @Column(name = "PAGE_TMPLT_ID")
    @AdminPresentation(friendlyName = "PageTemplateImpl_Template_Id", 
        visibility = VisibilityEnum.HIDDEN_ALL, 
        readOnly = true)
    protected Long id;

    @Column (name = "TMPLT_NAME")
    @AdminPresentation(friendlyName = "PageTemplateImpl_Template_Name", 
        prominent = true, gridOrder = 1)
    protected String templateName;

    @Column (name = "TMPLT_DESCR")
    protected String templateDescription;

    @Column (name = "TMPLT_PATH")
    @AdminPresentation(friendlyName = "PageTemplateImpl_Template_Path", 
        visibility = VisibilityEnum.HIDDEN_ALL, 
        readOnly = true)
    protected String templatePath;

    @ManyToMany(targetEntity = FieldGroupImpl.class, cascade = {CascadeType.ALL})
    @JoinTable(name = "CMS_PGTMPLT_FLDGRP_XREF", joinColumns = @JoinColumn(name = "PAGE_TMPLT_ID",
            referencedColumnName = "PAGE_TMPLT_ID"),
            inverseJoinColumns = @JoinColumn(name = "FLD_GROUP_ID", referencedColumnName = "FLD_GROUP_ID"))
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCMSElements")
    @OrderColumn(name = "GROUP_ORDER")
    @BatchSize(size = 20)
    @ClonePolicyCollection(deepClone = false)
    protected List<FieldGroup> fieldGroups = new ArrayList<FieldGroup>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getTemplateName() {
        return templateName;
    }

    @Override
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public String getTemplateDescription() {
        return templateDescription;
    }

    @Override
    public void setTemplateDescription(String templateDescription) {
        this.templateDescription = templateDescription;
    }

    @Override
    public String getTemplatePath() {
        return templatePath;
    }

    @Override
    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    @Override
    public List<FieldGroup> getFieldGroups() {
        return fieldGroups;
    }

    @Override
    public void setFieldGroups(List<FieldGroup> fieldGroups) {
        this.fieldGroups = fieldGroups;
    }

    @Override
    public String getMainEntityName() {
        return getTemplateName();
    }
}

