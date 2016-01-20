package cn.globalph.common.storage.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import cn.globalph.common.storage.domain.FileWorkArea;
import cn.globalph.common.storage.service.type.FileApplicationType;

/**
 * 类路径资源提供者
 * @author felix.wu
 */
@Service("commClassPathStorageProvider")
public class ClassPathStorageProvider implements StorageProvider {
	
	private static final Log LOGGER = LogFactory.getLog(StorageServiceImpl.class);
	
    @Value("${asset.storage.classpath.root.directory}")
	private String classpathRootDirectory;
	 
	@Override
	public InputStream getResource(String url) {
		return getResource( url, FileApplicationType.ALL );
	}

	@Override
	public InputStream getResource(String url, FileApplicationType fileApplicationType) {
		try {
            ClassPathResource resource = lookupResourceOnClassPath(url);
            if (resource != null && resource.exists()) {
                InputStream assetFile = resource.getInputStream();
                BufferedInputStream bufferedStream = new BufferedInputStream(assetFile);

                // Wrapping the buffered input stream with a globally shared stream allows us to 
                // vary the way the file names are generated on the file system.    
                // This benefits us (mainly in our demo site but their could be other uses) when we
                // have assets that are shared across sites that we also need to resize. 
                GloballySharedInputStream globallySharedStream = new GloballySharedInputStream(bufferedStream);
                globallySharedStream.mark(0);
                return globallySharedStream;
            } else {
                return null;
            }
        } catch (Exception e) {
        	LOGGER.error("Error getting resource from classpath", e);
        }
        return null;
	}

	@Override
	public void addOrUpdateResources(FileWorkArea workArea, List<File> files,
			boolean removeFilesFromWorkArea) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeResource(String name) {
		throw new UnsupportedOperationException();
	}
	
	private ClassPathResource lookupResourceOnClassPath(String name) {
        if ( classpathRootDirectory != null && !classpathRootDirectory.isEmpty() ) {
            try {
                String resourceName = FilenameUtils.separatorsToUnix(FilenameUtils.normalize(classpathRootDirectory + '/' + name));
                ClassPathResource resource = new ClassPathResource(resourceName);
                if (resource.exists()) {
                    return resource;
                }
            } catch (Exception e) {
            	LOGGER.error("Error getting resource from classpath", e);
            }
        }
        return null;
    }

	@Override
	public String getName() {
		return "CLASSPATH";
	}
}
