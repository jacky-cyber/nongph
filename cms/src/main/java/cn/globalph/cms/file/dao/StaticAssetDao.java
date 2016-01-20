package cn.globalph.cms.file.dao;

import java.util.List;

import cn.globalph.cms.file.domain.StaticAsset;

/**
 * Created by bpolster.
 */
public interface StaticAssetDao {

    public StaticAsset readStaticAssetById(Long id);
    
    public List<StaticAsset> readAllStaticAssets();

    public void delete(StaticAsset asset);

    public StaticAsset addOrUpdateStaticAsset(StaticAsset asset, boolean clearLevel1Cache);

    public StaticAsset readStaticAssetByFullUrl(String fullUrl);

}
