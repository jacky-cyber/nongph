package cn.globalph.b2c.order.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.globalph.b2c.order.service.type.RefundStatus;

public interface Refund extends Serializable{
	public Long getRefundId();
	
	public void setRefundId(Long refundId);
	
	public String getRefundDesc();
	
	public void setRefundDesc(String refundDesc);
	
	public RefundStatus getRefundStatus();
	
	public void setRefundStatus(RefundStatus refundStatus);
	
	public Date getRefundTime();
	
	public void setRefundTime(Date refundTime);
	
	public String getRefundBy();
	
	public void setRefundBy(String refundBy);
	
	public OrderItem getOrderItem();
	
	public void setOrderItem(OrderItem orderItem);
	
	public Order getOrder();
	
	public void setOrder(Order order);
	
	public List<RefundMedia> getRefundMedia();
	
	public void setRefundMedia(List<RefundMedia> refundMedia);
	
	public Integer getRefundNum();
	
	public void setRefundNum(Integer refundNum);
}
