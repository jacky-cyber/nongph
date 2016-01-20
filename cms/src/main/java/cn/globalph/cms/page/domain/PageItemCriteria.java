package cn.globalph.cms.page.domain;

import cn.globalph.common.rule.QuantityBasedRule;

import javax.annotation.Nonnull;

/**
 * Implementations of this interface contain item rule data that is used for targeting
 * <code>Page</code>s.
 * <br>
 * <br>
 * For example, a <code>Page</code>  could be setup to only show to user's
 * who have a particular product in their cart.
 *
 * @see cn.globalph.b2c.order.service.PageCartRuleProcessor
 * @author bpolster
 */
public interface PageItemCriteria extends QuantityBasedRule {

    /**
     * Returns the parent <code>Page</code> to which this
     * field belongs.
     *
     * @return
     */
    @Nonnull
    public Page getPage();

    /**
     * Sets the parent <code>Page</code>.
     * @param page
     */
    public void setPage(@Nonnull Page page);

    /**
     * Builds a copy of this item.   Used by the content management system when an
     * item is edited.
     *
     * @return a copy of this item
     */
    @Nonnull
    public PageItemCriteria cloneEntity();
    
}
