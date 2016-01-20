package cn.globalph.cms.structure.domain;

import cn.globalph.common.rule.SimpleRule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Implementations hold the values for a rule used to determine if a <code>StructuredContent</code>
 * item should be displayed.
 * <br>
 * The rule is represented as a valid MVEL string.    The Content Management System by default
 * is able to process rules based on the current customer, product,
 * {@link cn.globalph.common.TimeDTO time}, or {@link cn.globalph.common.RequestDTO request}
 *
 * @see cn.globalph.cms.web.structure.DisplayContentTag
 * @see cn.globalph.cms.structure.service.StructuredContentServiceImpl#evaluateAndPriortizeContent(java.util.List, int, java.util.Map)
 *
 */
public interface StructuredContentRule extends SimpleRule {

    /**
     * Gets the primary key.
     *
     * @return the primary key
     */
    @Nullable
    public Long getId();

    /**
     * Sets the primary key.
     *
     * @param id the new primary key
     */
    public void setId(@Nullable Long id);

    /**
     * Builds a copy of this content rule.   Used by the content management system when an
     * item is edited.
     *
     * @return a copy of this rule
     */
    @Nonnull
    public StructuredContentRule cloneEntity();

}
