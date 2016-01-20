package cn.globalph.common.extensibility.context.merge;

import org.springframework.core.PriorityOrdered;

/**
 * Use this merge bean post processor for merging tasks that should take place before the persistence layer is
 * initialized. This would include adding class transformers for load time weaving, and the like. See
 * {@link AbstractMergeBeanPostProcessor} for usage information.
 *
 * @see AbstractMergeBeanPostProcessor
 * @author Jeff Fischer
 */
public class EarlyStageMergeBeanPostProcessor extends AbstractMergeBeanPostProcessor implements PriorityOrdered {

    protected int order = Integer.MIN_VALUE;

    /**
     * This is the priority order for this post processor and will determine when this processor is run in relation
     * to other priority ordered processors (e.g. {@link org.springframework.context.annotation.CommonAnnotationBeanPostProcessor})
     * The default value if Integer.MIN_VALUE.
     */
    @Override
    public int getOrder() {
        return order;
    }

    /**
     * This is the priority order for this post processor and will determine when this processor is run in relation
     * to other priority ordered processors (e.g. {@link org.springframework.context.annotation.CommonAnnotationBeanPostProcessor})
     * The default value if Integer.MIN_VALUE.
     *
     * @param order the priority ordering
     */
    public void setOrder(int order) {
        this.order = order;
    }

}
