package cn.globalph.common.storage.service;

import cn.globalph.common.storage.domain.FileWorkArea;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Many components can benefit from creating and manipulating temporary files as well
 * as storing and accessing files in a remote repository (such as AmazonS3).
 * 
 * This service provides a pluggable way to provide those services.
 */
public interface StorageService {

	InputStream getResource(String name);

    /**
     * Removes the resource from the configured FileProvider
     * 
     * @param name - fully qualified path to the resource
     * @param applicationType - The type of file being accessed
     */
    boolean removeResource(String name);

    /**
     * Takes in a temporary work area and a single File and copies that files to 
     * the configured FileProvider's permanent storage.
     * 
     * Passing in removeFilesFromWorkArea to true allows for more efficient file processing
     * when using a local file system as it performs a move operation instead of a copy.
     * 
     * @param workArea
     * @param fileName
     * @param removeFilesFromWorkArea
     */
    void addOrUpdateResource(String providerName, FileWorkArea workArea, File file, boolean removeFilesFromWorkArea);

    /**
     * Takes in a temporary work area and copies all of the files to the configured FileProvider's permanent storage.
     * 
     * Passing in removeFilesFromWorkArea to true allows for more efficient file processing
     * when using a local file system as it performs a move operation instead of a copy.
     * 
     * @param workArea
     * @param removeFilesFromWorkArea
     */
    void addOrUpdateResources(String providerName, FileWorkArea workArea, boolean removeFilesFromWorkArea);

    /**
     * Takes in a temporary work area and a list of Files and copies them to 
     * the configured FileProvider's permanent storage.
     * 
     * Passing in removeFilesFromWorkArea to true allows for more efficient file processing
     * when using a local file system as it performs a move operation instead of a copy.     
     * 
     * @param workArea
     * @param fileNames
     * @param removeFilesFromWorkArea
     */
    void addOrUpdateResources(String providerName, FileWorkArea workArea, List<File> files, boolean removeFilesFromWorkArea);
    
    /**
     * 创建临时存放目录
     * @return
     */
	FileWorkArea initializeWorkArea();
	
	/**
	 * 删除临时存放目录
	 * @param fwArea
	 */
	void closeWorkArea(FileWorkArea fwArea);
}
