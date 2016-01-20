package cn.globalph.b2c.order.domain;

import org.springframework.stereotype.Service;

/**
 * @see cn.globalph.b2c.order.domain.NullOrderFactory
 * @author apazzolini
 */
@Service("blNullOrderFactory")
public class NullOrderFactoryImpl implements NullOrderFactory {
    
    protected static final Order NULL_ORDER = new NullOrderImpl();

    @Override
    public Order getNullOrder() {
        return NULL_ORDER;
    }

}
