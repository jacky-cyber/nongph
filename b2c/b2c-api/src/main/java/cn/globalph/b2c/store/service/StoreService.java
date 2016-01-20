package cn.globalph.b2c.store.service;

import java.util.List;
import java.util.Map;

import cn.globalph.b2c.store.domain.Store;
import cn.globalph.passport.domain.Address;

public interface StoreService {

    public Store readStoreById(Long id);

    public Store readStoreByStoreName(String storeName);

    /**
     * @deprecated use {@link #readStoreByStoreName(String)} instead.
     *
     * @param storeCode
     * @return
     */
    @Deprecated
    public Store readStoreByStoreCode(String storeCode);

    public Map<Store,Double> findStoresByAddress(Address searchAddress, double distance);

    public List<Store> readAllStores();

    public List<Store> readAllStoresByState(String state);

}
