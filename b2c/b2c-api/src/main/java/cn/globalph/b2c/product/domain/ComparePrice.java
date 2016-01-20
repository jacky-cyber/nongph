package cn.globalph.b2c.product.domain;

import java.io.Serializable;
import java.util.Date;

import cn.globalph.common.money.Money;

public interface ComparePrice extends Serializable{
	public Long getId();
	
	public void setId(Long id);
	
	public Sku getSku();
	
	public void setSku(Sku sku);
	
	public String getName();
	
	public void setName(String name);
	
	public Money getPrice();
	
	public void setPrice(Money price);
	
	public String getSource();
	
	public void setSource(String source);
	
	public String getSourceUrl();
	
	public void setSourceUrl(String sourceUrl);
	
	public Date getCreateTime();
	
	public void setCreateTime(Date createTime);
}
