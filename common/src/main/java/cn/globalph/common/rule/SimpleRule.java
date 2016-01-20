package cn.globalph.common.rule;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Represents a class containing an MVEL rule
 *
 * @author Jeff Fischer
 */
public interface SimpleRule extends Serializable {

    /**
     * The rule in the form of an MVEL expression
     *
     * @return the rule as an MVEL string
     */
    @Nonnull
    public String getMatchRule();

    /**
     * Sets the match rule used to test this item.
     *
     * @param matchRule the rule as an MVEL string
     */
    public void setMatchRule(@Nonnull String matchRule);

}
