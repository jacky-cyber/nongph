package cn.globalph.b2c.product.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.money.Money;

public interface GroupOn extends Serializable{
	public Long getId();
	
	public void setId(Long id);
	
	public Sku getSku();
	
	public void setSku(Sku sku);
	
	public Integer getSkuQuantity();
	
	public void setSkuQuantity(Integer skuQuantity);
	
	public Integer getParticipateInCount();
	
	public void setParticipateInCount(Integer participateInCount);
	
	public String getRuleDesc();
	
	public void setRuleDesc(String ruleDesc);
	
	public Date getStartDate();
	
	public void setStartDate(Date startDate);
	
	public Date getEndDate();
	
	public void setEndDate(Date endDate);
	
	public List<GroupOnAttr> getGroupOnAttrs();
	
	public void setGroupOnAttrs(List<GroupOnAttr> groupOnAttrs);
	
	public List<Order> getOrders();
	
	public void setOrders(List<Order> orders);
	
	public boolean isValid();

	/*add by jenny 09/09*/
	void setGroupPrice(Money groupPrice);

	Money getGroupPrice();

	void setGroupUnit(String groupUnit);

	String getGroupUnit();

	void setMinCount(Integer minCount);

	Integer getMinCount();
}
