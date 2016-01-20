package cn.globalph.cms.file.domain;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.openadmin.audit.AdminAuditableListener;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(value = { AdminAuditableListener.class })
@Table(name = "CMS_IMG_STATIC_ASSET")
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blCMSElements")
public class ImageStaticAssetImpl extends StaticAssetImpl implements ImageStaticAsset {

    @Column(name ="WIDTH")
    @AdminPresentation(friendlyName = "ImageStaticAssetImpl_Width",
            order = Presentation.FieldOrder.LAST + 1000,
            tab = Presentation.Tab.Name.File_Details, tabOrder = Presentation.Tab.Order.File_Details,
            readOnly = true)
    protected Integer width;

    @Column(name ="HEIGHT")
    @AdminPresentation(friendlyName = "ImageStaticAssetImpl_Height",
            order = Presentation.FieldOrder.LAST + 2000,
            tab = Presentation.Tab.Name.File_Details, tabOrder = Presentation.Tab.Order.File_Details,
            readOnly = true)
    protected Integer height;

    @Override
    public Integer getWidth() {
        return width;
    }

    @Override
    public void setWidth(Integer width) {
        this.width = width;
    }

    @Override
    public Integer getHeight() {
        return height;
    }

    @Override
    public void setHeight(Integer height) {
        this.height = height;
    }

}
