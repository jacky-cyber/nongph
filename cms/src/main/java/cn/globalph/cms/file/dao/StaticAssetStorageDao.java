package cn.globalph.cms.file.dao;

import java.io.IOException;
import java.sql.Blob;

import cn.globalph.cms.file.domain.StaticAssetStorage;

import org.springframework.web.multipart.MultipartFile;


public interface StaticAssetStorageDao {
    StaticAssetStorage create();

    StaticAssetStorage readStaticAssetStorageById(Long id);

    public StaticAssetStorage readStaticAssetStorageByStaticAssetId(Long id);

    StaticAssetStorage save(StaticAssetStorage assetStorage);

    void delete(StaticAssetStorage assetStorage);

    public Blob createBlob(MultipartFile uploadedFile) throws IOException;
}
