package cn.globalph.b2c.product.domain.wrapper;

import cn.globalph.b2c.catalog.domain.Weight;
import cn.globalph.b2c.product.domain.wrap.WeightWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class WeightWrapper implements APIWrapper<Weight, WeightWrap>{

    @Override
    public WeightWrap wrapDetails(Weight model) {
    	WeightWrap wrap = new WeightWrap();
    	wrap.setWeight( model.getWeight() );
        if (model.getWeightUnitOfMeasure() != null) {
        	wrap.setUnitOfMeasure( model.getWeightUnitOfMeasure().getType() );
        }
        return wrap;
    }

    @Override
    public WeightWrap wrapSummary(Weight model) {
        return wrapDetails(model);
    }
}
