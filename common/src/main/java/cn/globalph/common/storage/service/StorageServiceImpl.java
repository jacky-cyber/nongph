package cn.globalph.common.storage.service;

import cn.globalph.common.sitemap.service.SiteMapGenerator;
import cn.globalph.common.storage.FileServiceException;
import cn.globalph.common.storage.domain.FileWorkArea;
import cn.globalph.common.storage.service.type.FileApplicationType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/**
 * Many components can benefit from creating and manipulating temporary files as well
 * as storing and accessing files in a remote repository (such as AmazonS3).
 * 
 * This service provides a pluggable way to provide those services via {@link FileStorageProvider} implementations.
 * 
 * This service can be used by any component that needs to write files to an area shared by multiple application servers.
 * 
 * For example usage, see {@link SiteMapGenerator}.  The CMS module also uses this component to load, store, and 
 * manipulate images for the file-system.   
 * 
 * Generally, the process to create a new asset in the shared file system is ...
 * 1.  Call initializeWorkArea() to get a temporary directory
 * 2.  Create files, directories, etc. using the {@link FileWorkArea#filePathLocation} as the root directory.
 * 3.  Once your file processing is complete, call {@link #addOrUpdateResources(FileWorkArea, FileApplicationType)} to
 * 4.  Call {@link #closeWorkArea()} to clear out the temporary files
 */
@Service("commStorageService")
public class StorageServiceImpl implements StorageService {
	
	private static final Log LOGGER = LogFactory.getLog(StorageServiceImpl.class);
	
    @Resource(name = "commStorageProviders")
    private List<StorageProvider> storageProviders = new ArrayList<StorageProvider>();
    
    @Value("${asset.work.area.max.generated.directories}")
    private int maxGeneratedDirectoryDepth = 2;
    
    @Value("${asset.work.area.directory}")
    private String workAreaDirectory;
    
    /**
     * 从文件存储中获得文件
     */
    @Override
    public InputStream getResource(String url) {
    	String[] urlPart = url.split(":");
    	StorageProvider sp = selectStorageProvider( urlPart[0] );
    	if( sp!=null )
    		return sp.getResource( urlPart[1] );
    	else 
    		return null;
    }

    /**
     * Removes the resource matching the passed in file name from the FileProvider
     */
    @Override
    public boolean removeResource(String url) {
    	String[] urlPart = url.split(":");
    	StorageProvider sp = selectStorageProvider( urlPart[0] );
    	if( sp!=null )
    		return sp.removeResource( urlPart[1] );
    	else
    		return false;
    }

    /**
     * Takes in a work area and a fileName. Loads the file onto the provider.
     * 
     * Passing in removeFilesFromWorkArea to true allows for more efficient file processing
     * when using a local file system as it performs a move operation instead of a copy.
     * 
     * @param workArea
     * @param applicationType
     * @param fileNames
     * @param removeFilesFromWorkArea
     */
    @Override
    public void addOrUpdateResource(String providerName, FileWorkArea workArea, File file, boolean removeFilesFromWorkArea) {
        List<File> files = new ArrayList<File>();
        files.add(file);
        addOrUpdateResources(providerName, workArea, files, removeFilesFromWorkArea);
    }

    /**
     * Takes in a work area and application type and moves all of the files to the configured FileProvider.
     * 
     * @param workArea
     * @param applicationType
     */
    @Override
    public void addOrUpdateResources(String providerName, FileWorkArea workArea, boolean removeFilesFromWorkArea) {
        File folder = new File(workArea.getFilePathLocation());
        List<File> fileList = new ArrayList<File>();
        buildFileList(folder, fileList);
        addOrUpdateResources(providerName, workArea, fileList, removeFilesFromWorkArea);
    }
    
    @Override
    public void addOrUpdateResources(String providerName, FileWorkArea workArea, List<File> files, boolean removeFilesFromWorkArea) {
        checkFiles(workArea, files);
        selectStorageProvider(providerName).addOrUpdateResources(workArea, files, removeFilesFromWorkArea);
    }
    
    @Override
    public FileWorkArea initializeWorkArea() {
        String subDirectory = getSubDirectory();
        FileWorkArea fw = new FileWorkArea();
        fw.setFilePathLocation(subDirectory);
        
        File tmpDir = new File(subDirectory);
        if ( !tmpDir.exists() ) {
           	LOGGER.trace("Creating temp directory named " + subDirectory);
            if (!tmpDir.mkdirs()) {
                throw new FileServiceException("Unable to create temporary working directory for " + subDirectory);
            }
        }
        return fw;
    }
    
    @Override
    public void closeWorkArea(FileWorkArea fwArea) {
        File tempDirectory = new File(fwArea.getFilePathLocation());
        try {
            if (tempDirectory.exists()) {
                FileUtils.deleteDirectory(tempDirectory);
            }

            for (int i = 1; i < maxGeneratedDirectoryDepth; i++) {
                tempDirectory = tempDirectory.getParentFile();
                if (tempDirectory.list().length == 0 && tempDirectory.exists()) {
                    FileUtils.deleteDirectory(tempDirectory);
                }
            }
        } catch (IOException ioe) {
            throw new FileServiceException("Unable to delete temporary working directory for " + tempDirectory, ioe);
        }
    }
    private void checkFiles(FileWorkArea workArea, List<File> fileList) {
        for (File file : fileList) {
            String fileName = file.getAbsolutePath();
            if (!fileName.startsWith(workArea.getFilePathLocation())) {
                throw new FileServiceException("File operation attempted on file that is not in provided work area. "
                        + fileName + ".  Work area = " + workArea.getFilePathLocation());
            }
            if (!file.exists()) {
                throw new FileServiceException("Add or Update Resource called with filename that does not exist.  " + fileName);
            }
        }
    }

    /**
     * Adds the file to the passed in Collection.
     * If the file is a directory, adds its children recursively.   Otherwise, just adds the file to the list.    
     * @param file
     * @param fileList
     */
    private void buildFileList(File file, Collection<File> fileList) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (child.isDirectory()) {
                        buildFileList(child, fileList);
                    } else {
                        fileList.add(child);
                    }
                }
            }
        } else {
            fileList.add(file);
        }
    }
    
    private StorageProvider selectStorageProvider(String name){
    	for( StorageProvider sp : storageProviders ) {
    		if( sp.getName().equals( name ) ) 
    			return sp;
    	}
    	return null;
    }
    
    /**
     * Returns a directory that is unique for this work area.   
     */
    private String getSubDirectory() {
        String subDir = workAreaDirectory;
    	Random random = new Random();
        // This code is used to ensure that we don't have thousands of sub-directories in a single parent directory.
        if( maxGeneratedDirectoryDepth>3 )
        	LOGGER.warn("Property asset.server.max.generated.file.system.directories set to high, currently set to " + maxGeneratedDirectoryDepth);
        else {
	        for (int i = 0; i < maxGeneratedDirectoryDepth; i++) {
	            // check next int value
	            int num = random.nextInt(256);
	            subDir = FilenameUtils.concat(subDir, Integer.toHexString(num));
	        }
        }
        return subDir;
    }

}
