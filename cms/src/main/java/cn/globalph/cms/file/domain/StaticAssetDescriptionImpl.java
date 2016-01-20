package cn.globalph.cms.file.domain;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.openadmin.audit.AdminAuditable;
import cn.globalph.openadmin.audit.AdminAuditableListener;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CMS_STATIC_ASSET_DESC")
@EntityListeners(value = { AdminAuditableListener.class })
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blCMSElements")
public class StaticAssetDescriptionImpl implements StaticAssetDescription {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "StaticAssetDescriptionId")
    @GenericGenerator(
        name="StaticAssetDescriptionId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="StaticAssetDescriptionImpl"),
            @Parameter(name="entity_name", value="cn.globalph.cms.file.domain.StaticAssetDescriptionImpl")
        }
    )
    @Column(name = "STATIC_ASSET_DESC_ID")
    protected Long id;

    @Embedded
    @AdminPresentation(excluded = true)
    protected AdminAuditable auditable = new AdminAuditable();

    @Column (name = "DESCRIPTION")
    @AdminPresentation(friendlyName = "StaticAssetDescriptionImpl_Description", prominent = true)
    protected String description;

    @Column (name = "LONG_DESCRIPTION")
    @AdminPresentation(friendlyName = "StaticAssetDescriptionImpl_Long_Description", largeEntry = true, visibility = VisibilityEnum.GRID_HIDDEN)
    protected String longDescription;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
    public String getLongDescription() {
        return longDescription;
    }

    @Override
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    @Override
    public StaticAssetDescription cloneEntity() {
        StaticAssetDescriptionImpl newAssetDescription = new StaticAssetDescriptionImpl();
        newAssetDescription.description = description;
        newAssetDescription.longDescription = longDescription;

        return newAssetDescription;
    }

    @Override
    public AdminAuditable getAuditable() {
        return auditable;
    }

    @Override
    public void setAuditable(AdminAuditable auditable) {
        this.auditable = auditable;
    }
}

