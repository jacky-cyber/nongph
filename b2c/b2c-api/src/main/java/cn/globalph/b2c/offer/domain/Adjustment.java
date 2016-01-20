package cn.globalph.b2c.offer.domain;

import cn.globalph.common.money.Money;

import java.io.Serializable;

/**
 * 价格调整
 * @author fwu
 */
public interface Adjustment extends Serializable {

    public Long getId();

    public void setId(Long id);

    public Offer getOffer();

    public String getReason();

    public void setReason(String reason);

    public Money getValue();
    
    public void setValue(Money value);

}
