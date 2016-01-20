package cn.globalph.cms.file.domain;

import java.sql.Blob;

/**
 * 静态资产数据库存储
 * @author felix.wu
 *
 */
public interface StaticAssetStorage {

    Long getId();

    void setId(Long id);

    Blob getFileData();

    void setFileData(Blob fileData);

    public Long getStaticAssetId();

    public void setStaticAssetId(Long staticAssetId);

}
