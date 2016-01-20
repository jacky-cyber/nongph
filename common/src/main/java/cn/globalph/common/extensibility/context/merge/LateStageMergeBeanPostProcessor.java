package cn.globalph.common.extensibility.context.merge;

import org.springframework.core.Ordered;

/**
 * Use this merge processor for merging duties that should take place later in the Spring startup lifecycle.
 * This would include items that should be merged after the initialization of the persistence layer, like beans
 * that rely on EntityManager injection in some way. This is the most commonly used merge processor. Less
 * commonly used is the {@link EarlyStageMergeBeanPostProcessor}. See {@link AbstractMergeBeanPostProcessor} for
 * usage information.
 *
 * @see AbstractMergeBeanPostProcessor
 */
public class LateStageMergeBeanPostProcessor extends AbstractMergeBeanPostProcessor implements Ordered {

    protected int order = Integer.MAX_VALUE;

    /**
     * The regular ordering for this post processor in relation to other post processors. The default
     * value is Integer.MAX_VALUE.
     */
    @Override
    public int getOrder() {
        return order;
    }

    /**
     * The regular ordering for this post processor in relation to other post processors. The default
     * value is Integer.MAX_VALUE.
     *
     * @param order the regular ordering
     */
    public void setOrder(int order) {
        this.order = order;
    }

}
