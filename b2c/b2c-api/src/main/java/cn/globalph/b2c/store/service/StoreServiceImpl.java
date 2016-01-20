package cn.globalph.b2c.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.globalph.b2c.store.dao.StoreDao;
import cn.globalph.b2c.store.domain.Store;
import cn.globalph.passport.domain.Address;

import org.springframework.stereotype.Service;

@Service("blStoreService")
public class StoreServiceImpl implements StoreService {

    // private final static int MAXIMUM_DISTANCE = Integer.valueOf(25);
    @Resource(name = "blStoreDao")
    private StoreDao storeDao;

    public Store readStoreById(Long id) {
        return storeDao.readStoreById(id);
    }

    public Store readStoreByStoreName(String storeName) {
        return storeDao.readStoreByStoreName(storeName);
    }

    public Store readStoreByStoreCode(String storeCode) {
        return storeDao.readStoreByStoreCode(storeCode);
    }

    public List<Store> readAllStores() {
        return storeDao.readAllStores();
    }

    public List<Store> readAllStoresByState(String state) {
        return storeDao.readAllStoresByState(state);
    }

    public Map<Store, Double> findStoresByAddress(Address searchAddress, double distance) {
        Map<Store, Double> matchingStores = new HashMap<Store, Double>();
        for (Store store : readAllStores()) {
            Double storeDistance = findStoreDistance(store, Integer.parseInt(searchAddress.getPostalCode()));
            if (storeDistance != null && storeDistance <= distance) {
                matchingStores.put(store, storeDistance);
            }
        }

        return matchingStores;
    }
    
    private Double findStoreDistance(Store store, Integer zip) {
        return null;
        
        // A constant used to convert from degrees to radians.
        /*
        double degreesToRadians = 57.3;
        double storeDistance = 3959 * Math.acos( (Math.sin(zipCode.getZipLatitude() / degreesToRadians) * Math.sin(store.getLatitude() / degreesToRadians))
                + (Math.cos(zipCode.getZipLatitude() / degreesToRadians) * Math.cos(store.getLatitude() / degreesToRadians) * Math.cos((store.getLongitude() / degreesToRadians) - (zipCode.getZipLongitude() / degreesToRadians))));
        return storeDistance;
        */
    }
}
