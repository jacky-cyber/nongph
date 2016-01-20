package cn.globalph.b2c.inventory.domain.wrapper;

import cn.globalph.b2c.inventory.domain.wrap.InventoryWrap;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class InventoryWrapper implements APIWrapper<Sku, InventoryWrap>{

    public InventoryWrap wrapDetails(Sku sku) {
    	InventoryWrap wrap = new InventoryWrap();
    	wrap.setSkuId( sku.getId() );
        if (sku.getInventoryType() != null) {
        	wrap.setInventoryType(sku.getInventoryType().getType());
        }
        return wrap;
    }
    
    public InventoryWrap wrapSummary(Sku sku) {
        return wrapDetails(sku);
    }
    
    public InventoryWrap wrapSummary(Sku sku, Integer count) {
    	InventoryWrap wrap = wrapSummary(sku);
    	wrap.setQuantityAvailable( count );
        return wrap;
    }
}
