package cn.globalph.cms.file.service;

import cn.globalph.cms.file.domain.StaticAsset;
import cn.globalph.common.storage.service.StaticAssetPathService;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface StaticAssetService {

    public StaticAsset findStaticAssetById(Long id);
    
    public List<StaticAsset> readAllStaticAssets();

    public StaticAsset findStaticAssetByFullUrl(String fullUrl);

    /**
     * Used when uploading a file to Broadleaf.    This method will create the corresponding 
     * asset.   
     * 
     * Depending on the the implementation, the actual asset may be saved to the DB or to 
     * the file system.    The default implementation {@link StaticAssetServiceImpl} has a 
     * environment properties that determine this behavior <code>asset.use.filesystem.storage</code>
     * 
     * The properties Map allows for implementors to update other Asset properties at the
     * same time they are uploading a file.  The default implementation uses this for an optional URL to 
     * be specified.
     * 
     * @see StaticAssetServiceImpl
     * 
     * @param file - the file being uploaded
     * @param properties - additional meta-data properties
     * @return
     * @throws IOException
     */
    public StaticAsset createStaticAssetFromFile(MultipartFile file, Map<String, String> properties);

    /**
     * @see StaticAssetPathService#getStaticAssetUrlPrefix()
     * @deprecated since 3.1.0. 
     */
    public String getStaticAssetUrlPrefix();

    /**
     * @see StaticAssetPathService#getStaticAssetEnvironmentUrlPrefix()
     * @deprecated since 3.1.0. 
     */
    public String getStaticAssetEnvironmentUrlPrefix();

    /**
     * @see StaticAssetPathService#getStaticAssetEnvironmentSecureUrlPrefix()
     * @deprecated since 3.1.0. 
     */
    public String getStaticAssetEnvironmentSecureUrlPrefix();

    /**
     * @see StaticAssetPathService#convertAssetPath(String, String, boolean)
     * @deprecated since 3.1.0. 
     */
    public String convertAssetPath(String assetPath, String contextPath, boolean secureRequest);

    /**
     * Add an asset outside of Broadleaf Admin.  
     * 
     * @param staticAsset
     * @return
     */
    public StaticAsset addStaticAsset(StaticAsset staticAsset);

    /**
     * Update an asset outside of Broadleaf Admin.  
     * 
     * @param staticAsset
     * @return
     */
    public StaticAsset updateStaticAsset(StaticAsset staticAsset);

    /**
     * Delete an asset outside of Broadleaf Admin.  
     * 
     * @param staticAsset
     * @return
     */
    public void deleteStaticAsset(StaticAsset staticAsset);

}
