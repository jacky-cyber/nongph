package cn.globalph.b2c.product.domain.wrapper;

import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.wrap.SkuWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class SkuWrapper implements APIWrapper<Sku, SkuWrap> {
	private WeightWrapper weightWrapper;
	private DimensionWrapper dimensionWrapper;
	private ProductWrapper productWrapper;
    
	@Override
    public SkuWrap wrapDetails(Sku model) {
    	SkuWrap wrap = new SkuWrap();
    	wrap.setId( model.getId() );
    	wrap.setActiveStartDate( model.getActiveStartDate() );
    	wrap.setActiveEndDate( model.getActiveEndDate() );
    	wrap.setName( model.getName() );
    	wrap.setDescription( model.getDescription() );
    	wrap.setLongDescription( model.getLongDescription() );
    	wrap.setRetailPrice( model.getRetailPrice() );
    	wrap.setSalePrice( model.getSalePrice() );
    	wrap.setActive( model.isActive() );
    	wrap.setPrimaryMediaUrl( model.getSkuMedia().get("primary").getUrl() );
    	wrap.setOnSale( model.isOnSale() );
        if (model.getInventoryType() != null) {
        	wrap.setInventoryType( model.getInventoryType().getType() );
        }
        
        if (model.getWeight() != null){
        	wrap.setWeight( weightWrapper.wrapDetails(model.getWeight()) );
        }

        if (model.getDimension() != null){
            wrap.setDimension( dimensionWrapper.wrapDetails( model.getDimension() ) );
        }
        if(model.getProduct()!=null){
        	wrap.setProduct( productWrapper.wrapDetails(model.getProduct()));
        }
        return wrap;
    }

    @Override
    public SkuWrap wrapSummary(Sku model) {
        return wrapDetails(model);
    }
}
