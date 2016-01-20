package cn.globalph.b2c.product.domain.wrapper;

import cn.globalph.b2c.catalog.domain.Dimension;
import cn.globalph.b2c.product.domain.wrap.DimensionWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class DimensionWrapper implements APIWrapper<Dimension, DimensionWrap>{
    
    @Override
    public DimensionWrap wrapDetails(Dimension model) {
    	DimensionWrap wrap = new DimensionWrap();
    	wrap.setWidth( model.getWidth() );
    	wrap.setDepth( model.getDepth() );
    	wrap.setHeight( model.getHeight() );
    	wrap.setGirth( model.getGirth() );

        if (model.getDimensionUnitOfMeasure() != null) {
        	wrap.setDimensionUnitOfMeasure( model.getDimensionUnitOfMeasure().getType() );
        }

        if (model.getSize() != null) {
        	wrap.setSize( model.getSize().getType() );
        }

        if (model.getContainer() != null) {
        	wrap.setContainer( model.getContainer().getType() );
        }
        return wrap;
    }

    @Override
    public DimensionWrap wrapSummary(Dimension model) {
        return wrapDetails(model);
    }
}
