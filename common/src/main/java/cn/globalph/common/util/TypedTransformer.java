package cn.globalph.common.util;

import org.apache.commons.collections.Transformer;


/**
 * A class that provides for a typed transformer.
 * 
 * @author Andre Azzolini (apazzolini)
 * @see Transformer
 * @param <K> the type of the value that will be returned by the transformer
 */
public interface TypedTransformer<K> extends Transformer {
    
    public K transform(Object input);

}
