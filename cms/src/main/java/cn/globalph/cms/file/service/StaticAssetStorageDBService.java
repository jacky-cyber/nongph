package cn.globalph.cms.file.service;

import cn.globalph.cms.file.domain.StaticAsset;
import cn.globalph.cms.file.domain.StaticAssetStorage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 静态资产数据库存储服务接口
 * @author felix.wu
 *
 */
public interface StaticAssetStorageDBService {

    /**
     * Returns a StaticAssetStorage object.   Assumes that the asset is stored in the Database.
     * 
     * Storing Assets in the DB is not the preferred mechanism for Broadleaf as of 3.0 so in most cases, this 
     * method would not be used by Broadleaf implementations.
     * 
     * @param id
     * @return
     */
    StaticAssetStorage findStaticAssetStorageById(Long id);

    /**
     * Returns a StaticAssetStorage object using the id of a related StaticAsset.   
     * Assumes that the asset is stored in the Database.
     * 
     * Storing Assets in the DB is not the preferred mechanism for Broadleaf as of 3.0 so in most cases, this 
     * method would not be used by Broadleaf implementations.
     * 
     * @param id
     * @return
     */
    StaticAssetStorage readStaticAssetStorageByStaticAssetId(Long id);

    /**
     * Persists a static asset to the database.   Not typically used since Broadleaf 3.0 as the 
     * preferred method for storing assets is on a shared-filesystem.
     * 
     * @param assetStorage
     * @return
     */
    StaticAssetStorage save(StaticAssetStorage assetStorage);

    /**
     * Removes a static asset from the database.   Not typically used since Broadleaf 3.0 as the 
     * preferred method for storing assets is on a shared-filesystem.
     * 
     * @param assetStorage
     */
    void delete(StaticAssetStorage assetStorage);

    /**
     * Persists the file to the DB or FileSystem according to the staticAsset's StorageType.    Typically, the 
     * MultipartFile is passed in from a Controller like the AdminAssetUploadController 
     *  
     * @param file
     * @param id
     * @throws IOException
     */
    void createStaticAssetStorageFromFile(MultipartFile file, StaticAsset staticAsset) throws IOException;

}
