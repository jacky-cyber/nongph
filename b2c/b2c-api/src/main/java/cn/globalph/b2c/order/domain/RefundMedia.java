package cn.globalph.b2c.order.domain;

import java.io.Serializable;

public interface RefundMedia extends Serializable{
	public Long getId();
	
	public void setId(Long id);
	
	public Refund getRefund();
	
	public void setRefund(Refund refund);
	
	public String getMediaUrl();
	
	public void setMediaUrl(String mediaUrl);
}
