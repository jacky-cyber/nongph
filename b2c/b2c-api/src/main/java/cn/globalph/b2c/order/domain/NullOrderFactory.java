package cn.globalph.b2c.order.domain;

/**
 * <p>Generates a shared, static instance of NullOrderImpl.</p>
 * 
 * @see cn.globalph.b2c.order.domain.NullOrderImpl
 * @author apazzolini
 */
public interface NullOrderFactory {
    
    public Order getNullOrder();
    
}
