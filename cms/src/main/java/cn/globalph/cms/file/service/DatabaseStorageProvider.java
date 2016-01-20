package cn.globalph.cms.file.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import cn.globalph.cms.file.domain.StaticAsset;
import cn.globalph.cms.file.domain.StaticAssetStorage;
import cn.globalph.common.storage.domain.FileWorkArea;
import cn.globalph.common.storage.service.StorageProvider;
import cn.globalph.common.storage.service.type.FileApplicationType;

@Service("cmsDatabaseStorageProvider")
public class DatabaseStorageProvider implements StorageProvider {
	
	private static final Log LOG = LogFactory.getLog(DatabaseStorageProvider.class);
	
    @Resource(name="cmsStaticAssetStorageDBService")
    private StaticAssetStorageDBService staticAssetStorageDBService;
    
    @Resource(name="cmsStaticAssetService")
    private StaticAssetService staticAssetService;
    
	@Override
	public InputStream getResource(String url) {
		return getResource(url, FileApplicationType.ALL);
	}

	@Override
	public InputStream getResource(String url, FileApplicationType fileApplicationType) {
		try{
			StaticAsset staticAsset = staticAssetService.findStaticAssetByFullUrl(url);
			StaticAssetStorage storage = staticAssetStorageDBService.readStaticAssetStorageByStaticAssetId(staticAsset.getId());
	        if (storage != null) {
	            InputStream is = storage.getFileData().getBinaryStream();
	            return is;
	        }
		} catch(Exception e) {
			LOG.error("can't get input stream for " + url, e);
		}
		return null;
	}

	@Override
	public void addOrUpdateResources(FileWorkArea workArea, List<File> files,
			boolean removeFilesFromWorkArea) {
	}

	@Override
	public boolean removeResource(String name) {
		return false;
	}

	@Override
	public String getName() {
		return "DATABASE";
	}
	
}
