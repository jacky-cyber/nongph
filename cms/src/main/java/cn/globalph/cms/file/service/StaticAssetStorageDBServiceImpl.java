package cn.globalph.cms.file.service;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import cn.globalph.cms.field.type.StorageType;
import cn.globalph.cms.file.dao.StaticAssetStorageDao;
import cn.globalph.cms.file.domain.StaticAsset;
import cn.globalph.cms.file.domain.StaticAssetStorage;
import cn.globalph.common.storage.domain.FileWorkArea;
import cn.globalph.common.storage.service.StorageService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;

import javax.annotation.Resource;

@Service("cmsStaticAssetStorageDBService")
public class StaticAssetStorageDBServiceImpl implements StaticAssetStorageDBService {
	
    @Value("${asset.server.max.uploadable.file.size}")
    private long maxUploadableFileSize;

    @Value("${asset.server.file.buffer.size}")
    private int fileBufferSize = 8096;

    @Resource(name = "commStorageService")
    private StorageService storageService;

    @Resource(name="blStaticAssetStorageDao")
    private StaticAssetStorageDao staticAssetStorageDao;
    
    @Override
    public StaticAssetStorage findStaticAssetStorageById(Long id) {
        return staticAssetStorageDao.readStaticAssetStorageById(id);
    }
    
    /**
     * 从数据库中读取文件
     */
    @Override
    public StaticAssetStorage readStaticAssetStorageByStaticAssetId(Long id) {
        return staticAssetStorageDao.readStaticAssetStorageByStaticAssetId(id);
    }

    @Override
    @Transactional("blTransactionManagerAssetStorageInfo")
    public StaticAssetStorage save(StaticAssetStorage assetStorage) {
        return staticAssetStorageDao.save(assetStorage);
    }

    @Override
    @Transactional("blTransactionManagerAssetStorageInfo")
    public void delete(StaticAssetStorage assetStorage) {
        staticAssetStorageDao.delete(assetStorage);
    }
        
    @Override
    @Transactional("blTransactionManagerAssetStorageInfo")
    public void createStaticAssetStorageFromFile(MultipartFile file, StaticAsset staticAsset) throws IOException {
        if (StorageType.DATABASE.equals(staticAsset.getStorageType())) {
            StaticAssetStorage storage = staticAssetStorageDao.create();
            storage.setStaticAssetId(staticAsset.getId());
            Blob uploadBlob = staticAssetStorageDao.createBlob(file);
            storage.setFileData(uploadBlob);
            staticAssetStorageDao.save(storage);
        } else if (StorageType.FILESYSTEM.equals(staticAsset.getStorageType())) {
            FileWorkArea tempWorkArea = storageService.initializeWorkArea();
            // Convert the given URL from the asset to a system-specific suitable file path
            String destFileName = FilenameUtils.normalize(tempWorkArea.getFilePathLocation() + File.separator + FilenameUtils.separatorsToSystem(staticAsset.getFullUrl()));

            InputStream input = file.getInputStream();
            byte[] buffer = new byte[fileBufferSize];

            File destFile = new File(destFileName);
            if (!destFile.getParentFile().exists()) {
                if (!destFile.getParentFile().mkdirs()) {
                    if (!destFile.getParentFile().exists()) {
                        throw new RuntimeException("Unable to create parent directories for file: " + destFileName);
                    }
                }
            }

            OutputStream output = new FileOutputStream(destFile);
            boolean deleteFile = false;
            try {
                int bytesRead;
                int totalBytesRead = 0;
                while ((bytesRead = input.read(buffer)) != -1) {
                    totalBytesRead += bytesRead;
                    if (totalBytesRead > maxUploadableFileSize) {
                        deleteFile = true;
                        throw new IOException("Maximum Upload File Size Exceeded");
                    }
                    output.write(buffer, 0, bytesRead);
                }
                
                // close the output file stream prior to moving files around
                
                output.close();
                storageService.addOrUpdateResource(StorageType.FILESYSTEM.getType(), tempWorkArea, destFile, deleteFile);
            } finally {
                IOUtils.closeQuietly(output);
                storageService.closeWorkArea(tempWorkArea);
            }
        }
    }
}