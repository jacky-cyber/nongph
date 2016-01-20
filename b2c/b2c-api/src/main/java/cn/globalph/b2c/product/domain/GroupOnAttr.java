package cn.globalph.b2c.product.domain;

import java.io.Serializable;

import cn.globalph.common.money.Money;

public interface GroupOnAttr extends Serializable {
	public Long getId();
	
	public void setId(Long id);
	
	public GroupOn getGroupOn();
	
	public void setGroupOn(GroupOn groupOn);
	
	public Integer getCountRangeFrom();
	
	public void setCountRangeFrom(Integer countRangeFrom);
	
	public Integer getCountRangeTo();
	
	public void setCountRangeTo(Integer countRangeTo);
	
	public Money getPrice();
	
	public void setPrice(Money price);
}
