package cn.globalph.b2c.offer.domain;

import cn.globalph.common.rule.SimpleRule;

/**
 * 
 * @felix.wu
 *
 */
public interface OfferRule extends SimpleRule {

    public Long getId();

    public void setId(Long id);

    public String getMatchRule();

    public void setMatchRule(String matchRule);

}
