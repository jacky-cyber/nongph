package cn.globalph.common.structure.dto;

import java.io.Serializable;

/**
 * StructuredContent data is converted into a DTO since it requires
 * pre-processing.   The data is fairly static so the desire is
 * to cache the value after it has been processed.
 *
 * This DTO represents a compact version of StructuredContentItemCriteria
 *
 * Created by bpolster.
 */
public class ItemCriteriaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Integer qty;
    protected String matchRule;

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getMatchRule() {
        return matchRule;
    }

    public void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }
}
