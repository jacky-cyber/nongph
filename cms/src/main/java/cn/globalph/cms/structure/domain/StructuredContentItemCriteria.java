package cn.globalph.cms.structure.domain;

import cn.globalph.common.rule.QuantityBasedRule;

import javax.annotation.Nonnull;

/**
 * Implementations of this interface contain item rule data that is used for targeting
 * <code>StructuredContent</code> items.
 * <br>
 * <br>
 * For example, a <code>StructuredContent</code> item could be setup to only show to user's
 * who have a particular product in their cart.
 *
 * @see cn.globalph.b2c.order.service.StructuredContentCartRuleProcessor
 * @author bpolster
 */
public interface StructuredContentItemCriteria extends QuantityBasedRule {

    /**
     * Returns the parent <code>StructuredContent</code> item to which this
     * field belongs.
     *
     * @return
     */
    @Nonnull
    public StructuredContent getStructuredContent();

    /**
     * Sets the parent <code>StructuredContent</code> item.
     * @param structuredContent
     */
    public void setStructuredContent(@Nonnull StructuredContent structuredContent);

    /**
     * Builds a copy of this item.   Used by the content management system when an
     * item is edited.
     *
     * @return a copy of this item
     */
    @Nonnull
    public StructuredContentItemCriteria cloneEntity();
    
}
