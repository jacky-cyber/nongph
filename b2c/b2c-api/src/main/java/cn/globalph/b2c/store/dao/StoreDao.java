package cn.globalph.b2c.store.dao;

import java.util.List;

import cn.globalph.b2c.store.domain.Store;

public interface StoreDao {

    public Store readStoreById(Long id);

    public Store readStoreByStoreName(final String storeName);

    /**
     * @deprecated use {@link #readStoreByStoreName(String)} instead
     *
     * @param storeCode
     * @return
     */
    @Deprecated
    public Store readStoreByStoreCode(final String storeCode);

    public List<Store> readAllStores();

    public List<Store> readAllStoresByState(final String state);

}
