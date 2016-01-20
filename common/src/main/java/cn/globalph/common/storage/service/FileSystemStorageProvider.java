package cn.globalph.common.storage.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import cn.globalph.common.storage.FileServiceException;
import cn.globalph.common.storage.domain.FileWorkArea;
import cn.globalph.common.storage.service.type.FileApplicationType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Default implementation of FileServiceProvider that uses the local file system to store files
 * 
 * This Provider can only be used in production systems that run on a single server or those that have a shared filesystem
 * mounted to the application servers.
 *
 */
@Service("commFileSystemStorageProvider")
public class FileSystemStorageProvider implements StorageProvider {
	
    @Value("${asset.storage.file.system.root.path}")
    private String fileSystemBaseDirectory;

    @Override
    public InputStream getResource(String url) {
        return getResource(url, FileApplicationType.ALL);
    }

    @Override
    public InputStream getResource(String url, FileApplicationType applicationType) {
        String filePath = FilenameUtils.normalize(getBaseDirectory() + File.separator + url);
        try{
        	return new FileInputStream(filePath);
        } catch( FileNotFoundException e) {
        	throw new RuntimeException("not find resource for " + url);
        }
    }

    @Override
    public void addOrUpdateResources(FileWorkArea area, List<File> files, boolean removeResourcesFromWorkArea) {
        for (File srcFile : files) {
            if (!srcFile.getAbsolutePath().startsWith(area.getFilePathLocation())) {
                throw new FileServiceException("Attempt to update file " + srcFile.getAbsolutePath() +
                        " that is not in the passed in WorkArea " + area.getFilePathLocation());
            }

            String fileName = srcFile.getAbsolutePath().substring(area.getFilePathLocation().length());
            
            // before building the resource name, convert the file path to a url-like path
            String url = FilenameUtils.separatorsToUnix(fileName);
            String destinationFilePath = FilenameUtils.normalize(getBaseDirectory() + File.separator + url);
            File destFile = new File(destinationFilePath);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            
            try {
                if (removeResourcesFromWorkArea) {
                    if (destFile.exists()) {
                        FileUtils.deleteQuietly(destFile);
                    }
                    FileUtils.moveFile(srcFile, destFile);
                } else {
                    FileUtils.copyFile(srcFile, destFile);
                }
            } catch (IOException ioe) {
                throw new FileServiceException("Error copying resource named " + fileName + " from workArea " +
                        area.getFilePathLocation() + " to " + url, ioe);
            }
        }
    }

    @Override
    public boolean removeResource(String name) {
        String filePathToRemove = FilenameUtils.normalize(getBaseDirectory() + File.separator + name);
        File fileToRemove = new File(filePathToRemove);
        return fileToRemove.delete();
    }

    /**
     * Returns a base directory (unique for each tenant in a multi-tenant installation.
     * Creates the directory if it does not already exist.
     */
    protected String getBaseDirectory() {
        if (fileSystemBaseDirectory == null || fileSystemBaseDirectory.isEmpty() ) {
           fileSystemBaseDirectory = System.getProperty("java.io.tmpdir");
        }
        return fileSystemBaseDirectory;
    }

	@Override
	public String getName() {
		return "FILESYSTEM";
	}
}
