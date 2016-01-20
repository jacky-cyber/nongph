package cn.globalph.cms.file.domain;

import cn.globalph.cms.field.type.StorageType;
import cn.globalph.cms.file.service.StaticAssetService;
import cn.globalph.openadmin.audit.AdminAuditable;

import java.io.Serializable;
import java.util.Map;

public interface StaticAsset extends Serializable {

    /**
     * Returns the id of the static asset.
     * @return
     */
    public Long getId();

    /**
     * Sets the id of the static asset.    
     * @param id
     */
    public void setId(Long id);


    /**
     * The name of the static asset.
     * @return
     */
    public String getName();

    /**
     * Sets the name of the static asset.   Used primarily for 
     * @param name
     */
    public void setName(String name);

    /**
     * Returns the altText of this asset.
     * 
     * @return
     */
    public String getAltText();

    /**
     * Set the altText of the static asset.
     * @param title
     */
    public void setAltText(String altText);

    /**
     * Returns the title of this asset.
     * 
     * @return
     */
    public String getTitle();

    /**
     * Set the title of the static asset.
     * @param title
     */
    public void setTitle(String title);

    /**
     * URL used to retrieve this asset.
     * @return
     */
    public String getFullUrl();

    /**
     * Sets the URL for the asset
     * @param fullUrl
     */
    public void setFullUrl(String fullUrl);

    /**
     * Filesize of the asset.
     * @return
     */
    public Long getFileSize();

    /**
     * Sets the filesize of the asset
     * @param fileSize
     */
    public void setFileSize(Long fileSize);

    /**
     * @deprecated - Use {@link #getTitle()} or {@link #getAltText()}getAltText() instead.
     * @return
     */
    public Map<String, StaticAssetDescription> getContentMessageValues();

    /**
     * @deprecated - Use {@link #setTitle(String)} or {@link #setAltText(String)} instead.
     * @param contentMessageValues
     */
    public void setContentMessageValues(Map<String, StaticAssetDescription> contentMessageValues);

    /**
     * Returns the mimeType of the asset.
     * @return
     */
    public String getMimeType();

    /**
     * Sets the mimeType of the asset.
     * @return
     */
    public void setMimeType(String mimeType);

    /**
     * Returns the file extension of the asset.
     * @return
     */
    public String getFileExtension();

    /**
     * Sets the fileExtension of the asset.
     * @param fileExtension
     */
    public void setFileExtension(String fileExtension);

    /**
     * Returns how the underlying asset is stored.  Typically on the FileSystem or the Database.
     * 
     * If null, this method returns <code>StorageType.DATABASE</code> for backwards compatibility.
     * 
     * @see {@link StaticAssetService}
     * @return
     */
    public StorageType getStorageType();

    /**
     * Returns how the asset was stored in the backend (e.g. DATABASE or FILESYSTEM)
     * @param storageType
     */
    public void setStorageType(StorageType storageType);

    public AdminAuditable getAuditable();

    public void setAuditable(AdminAuditable auditable);

}
