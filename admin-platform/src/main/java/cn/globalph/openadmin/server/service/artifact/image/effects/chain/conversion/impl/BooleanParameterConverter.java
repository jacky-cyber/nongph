package cn.globalph.openadmin.server.service.artifact.image.effects.chain.conversion.impl;

import cn.globalph.openadmin.server.service.artifact.image.effects.chain.conversion.ConversionException;
import cn.globalph.openadmin.server.service.artifact.image.effects.chain.conversion.Parameter;
import cn.globalph.openadmin.server.service.artifact.image.effects.chain.conversion.ParameterConverter;

public class BooleanParameterConverter implements ParameterConverter {

    /* (non-Javadoc)
     * @see com.xpressdocs.email.asset.effects.chain.conversion.ParameterConverter#convert(java.lang.String, double)
     */
    public Parameter convert(String value, Double factor, boolean applyFactor) throws ConversionException {
        Parameter param = new Parameter();
        param.setParameterClass(boolean.class);
        param.setParameterInstance(Boolean.parseBoolean(value));
        
        return param;
    }

}
