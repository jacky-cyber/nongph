package cn.globalph.cms.file.service;

import java.util.Map;

/**
 * 静态资产缓存服务
 * @author felix.wu
 *
 */
public interface StaticAssetCacheService {

    /**
     * 获得缓存的文件路径和类型
     * @param fullUrl 在特定存储中的路径
     * @param parameterMap 请求参数
     * @return
     * @throws Exception
     */
    String[] getCachedStaticAsset(String fullUrl, Map<String, String> parameterMap) throws Exception;
}
