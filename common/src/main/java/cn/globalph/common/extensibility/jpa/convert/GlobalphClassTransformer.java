package cn.globalph.common.extensibility.jpa.convert;

import javax.persistence.spi.ClassTransformer;
import java.util.Properties;

/**
 * 
 * @felix.wu
 *
 */
public interface GlobalphClassTransformer extends ClassTransformer {

    public void compileJPAProperties(Properties props, Object key) throws Exception;
        
}
