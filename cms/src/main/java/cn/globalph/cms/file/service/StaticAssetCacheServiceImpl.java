package cn.globalph.cms.file.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import cn.globalph.cms.common.AssetNotFoundException;
import cn.globalph.cms.file.domain.StaticAsset;
import cn.globalph.cms.file.service.operation.NamedOperationManager;
import cn.globalph.common.storage.service.StorageService;
import cn.globalph.openadmin.server.service.artifact.ArtifactService;
import cn.globalph.openadmin.server.service.artifact.image.Operation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

@Service("cmsStaticAssetCacheService")
public class StaticAssetCacheServiceImpl implements StaticAssetCacheService {
	    
    @Resource(name = "commStorageService")
    private StorageService storageService;

    @Resource(name="cmsStaticAssetService")
    private StaticAssetService staticAssetService;
    
    @Resource(name="blArtifactService")
    private ArtifactService artifactService;

    @Resource(name="blNamedOperationManager")
    private NamedOperationManager namedOperationManager;
    
    /**
     * cache文件存放根目录,此目录结构需要storage支持
     */
    @Value("${asset.cache.root.directory}")
    private String cacheRootDirectory;   
    
    @Override
    public String[] getCachedStaticAsset(String fullUrl, Map<String, String> parameterMap) throws Exception {
        //转换请求参数
        String mimeType = getMimeType( fullUrl );
        Map<String, String> convertedParameters = namedOperationManager.applyNamedParameters(parameterMap);
        File cachedFile = getCachedFile( fullUrl, mimeType, convertedParameters);
        if ( "image/gif".equals(mimeType) ) {
            mimeType = "image/png";
        }
        return new String[]{ cachedFile.getAbsolutePath(), mimeType };
    }
    
    private File getCachedFile(String fullUrl, String mimeType, Map<String, String> paramMap) throws Exception {
    	String cachedFilePath = getCacheRootDirectory() + "/" + constructCachedFileName(fullUrl, paramMap);
        //从cache中得到文件名对应的文件
        File cachedFile = new File( cachedFilePath );
        if( !cachedFile.exists() ) {//创建cache
        	if( isBaseUrl(paramMap) ) 
        		createCachedBaseFile(fullUrl, cachedFile);
        	else
        		createCachedFile(fullUrl, mimeType, paramMap, cachedFile);
        }
        return cachedFile;
    }
    
    private void createCachedFile(String fullUrl, String mimeType, Map<String, String> parameterMap, File cachedFile) throws Exception {
    	//取源文件
        File cachedBaseFile = getCachedBaseFile( fullUrl, parameterMap );
        BufferedInputStream original = new BufferedInputStream( new FileInputStream(cachedBaseFile) );
        original.mark(0);                                    
        
        Operation[] operations = artifactService.buildOperations(parameterMap, original, mimeType);
        InputStream converted = artifactService.convert(original, operations, mimeType);
        
        createLocalFileFromInputStream(converted, cachedFile);
    }
    
    private File getCachedBaseFile( String fullUrl, Map<String, String> parameterMap ) throws Exception {
        Map<String, String> utParam = new HashMap<String, String>();
        if( parameterMap.containsKey("ut") )
        	utParam.put( "ut", parameterMap.get("ut") );
        
    	String cachedBaseFilePath = getCacheRootDirectory() + "/" + constructCachedFileName(fullUrl, utParam);
    	File cachedBaseFile = new File( cachedBaseFilePath );
    	if( !cachedBaseFile.exists() ) {
    		createCachedBaseFile(fullUrl, cachedBaseFile);
    	}
    	return cachedBaseFile;
    }
    
    private void createCachedBaseFile(String fullUrl, File cachedFile) throws Exception {
    	StaticAsset staticAsset = staticAssetService.findStaticAssetByFullUrl(fullUrl);
        if ( staticAsset == null ) {
            throw new AssetNotFoundException("Unable to find an asset for the url (" + fullUrl + ")");
        }
		InputStream is = storageService.getResource( staticAsset.getStorageType().getType() + ":" + staticAsset.getFullUrl() );
        createLocalFileFromInputStream(is, cachedFile);
    }
    
    /**
     * 从输入流中复制文件到文件存放目录
     * @param is
     * @param cachedBaseFile
     * @throws IOException
     */
    private void createLocalFileFromInputStream(InputStream is, File file) throws IOException {
        FileOutputStream tos = null;
        try {
            if ( !file.getParentFile().exists() ) {
            	file.getParentFile().mkdirs();
            }
            tos = new FileOutputStream(file);
            IOUtils.copy(is, tos);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(tos);
        }
    }
    
    /**
     * Builds a file system path for the passed in static asset and paramaterMap.
     * 
     * @param staticAsset
     * @param parameterMap
     * @param useSharedFile
     * @return
     * @throws Exception 
     */
    private String constructCachedFileName(String fullUrl, Map<String, String> parameterMap) throws Exception {

        StringBuilder sb2 = new StringBuilder(200);
        if ( parameterMap != null ) {
            for ( Map.Entry<String, String> entry : parameterMap.entrySet() ) {
                sb2.append( '-' ).append( entry.getKey() ).append( '-' ).append( entry.getValue() );
            }
        }
        
        /*
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest( sb2.toString().getBytes() );
        BigInteger number = new BigInteger(1, messageDigest);
        String digest = number.toString(16);
        */
        String digest = DigestUtils.md5Hex( sb2.toString() );
        StringBuilder sb = new StringBuilder();
        sb.append( fullUrl.substring(0, fullUrl.lastIndexOf('.')) );
        sb.append( "---" ).append( pad(digest, 32, '0') );
        sb.append( fullUrl.substring(fullUrl.lastIndexOf('.') ) );

        return sb.toString();
    }

    private String pad(String s, int length, char pad) {
        StringBuilder buffer = new StringBuilder(s);
        while (buffer.length() < length) {
            buffer.insert(0, pad);
        }
        return buffer.toString();
    }
    
    private String getCacheRootDirectory() {
        if( cacheRootDirectory==null || cacheRootDirectory.isEmpty() ) {
        	cacheRootDirectory = System.getProperty("java.io.tmpdir");
        }
    	return cacheRootDirectory;
    }
    
    private boolean isBaseUrl(Map<String, String> params){
    	return params.isEmpty() || (params.size()==1 && params.keySet().iterator().next().equals("ut"));
    }
    
    private String getMimeType(String fullUrl){
    	if( fullUrl.endsWith(".jpg") || fullUrl.endsWith(".JPG") )
    		return "image/jpg";
    	else if( fullUrl.endsWith(".gif") || fullUrl.endsWith(".GIF")  )
    		return "image/gif";
    	else if( fullUrl.endsWith(".png") || fullUrl.endsWith(".PNG") )
    		return "image/png";
    	else
    		throw new RuntimeException("Unknown image type");
    }
}