package cn.globalph.b2c.product.domain.wrapper;

import cn.globalph.b2c.product.domain.SkuBundleItem;
import cn.globalph.b2c.product.domain.wrap.SkuBundleItemWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class SkuBundleItemWrapper implements APIWrapper<SkuBundleItem, SkuBundleItemWrap> {
    
    @Override
    public SkuBundleItemWrap wrapDetails(SkuBundleItem model) {
    	SkuBundleItemWrap wrap = new SkuBundleItemWrap();
    	wrap.setId( model.getId() );
    	wrap.setQuantity( model.getQuantity() );
    	wrap.setSalePrice( model.getSalePrice() );
    	wrap.setRetailPrice( model.getRetailPrice() );
    	wrap.setBundleId( model.getBundle().getId() );
    	wrap.setName( model.getSku().getName() );
    	wrap.setDescription( model.getSku().getDescription() );
    	wrap.setLongDescription( model.getSku().getLongDescription() );
    	wrap.setActive( model.getSku().isActive() );
        // this.sku = (SkuWrapper)context.getBean(SkuWrapper.class.getName());
        // this.sku.wrap(model.getSku(), request);
    	wrap.setProductId( model.getSku().getProduct().getId() );
    	return wrap;
    }

    @Override
    public SkuBundleItemWrap wrapSummary(SkuBundleItem model) {
        return wrapDetails(model);
    }
}
