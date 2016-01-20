package cn.globalph.cms.file.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CMS_STATIC_ASSET_STRG")
public class StaticAssetStorageImpl implements StaticAssetStorage {

    @Id
    @GeneratedValue(generator = "StaticAssetStorageId")
    @GenericGenerator(
        name="StaticAssetStorageId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="StaticAssetStorageImpl"),
            @Parameter(name="entity_name", value="cn.globalph.cms.file.domain.StaticAssetStorageImpl")
        }
    )
    @Column(name = "STATIC_ASSET_STRG_ID")
    protected Long id;

    @Column(name ="STATIC_ASSET_ID", nullable = false)
    @Index(name="STATIC_ASSET_ID_INDEX", columnNames={"STATIC_ASSET_ID"})
    protected Long staticAssetId;

    @Column (name = "FILE_DATA", length = Integer.MAX_VALUE - 1)
    @Lob
    protected Blob fileData;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Blob getFileData() {
        return fileData;
    }

    @Override
    public void setFileData(Blob fileData) {
        this.fileData = fileData;
    }

    @Override
    public Long getStaticAssetId() {
        return staticAssetId;
    }

    @Override
    public void setStaticAssetId(Long staticAssetId) {
        this.staticAssetId = staticAssetId;
    }
}
