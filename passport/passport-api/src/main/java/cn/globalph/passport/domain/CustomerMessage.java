package cn.globalph.passport.domain;

import java.io.Serializable;
import java.util.Date;

import cn.globalph.passport.service.type.CustomerMessageStatusType;

public interface CustomerMessage extends Serializable {
	public Long getId();
	
	public void setId(Long id);
	
	public String getMessageText();
	
	public void setMessageText(String messageText);
	 
	public Date getCreateTime();
	
	public void setCreateTime(Date createTime);
	
	public Customer getCustomer();
	
	public void setCustomer(Customer customer);
	
	public CustomerMessageStatusType getStatus();
	
	public void setStatus(CustomerMessageStatusType status);
	
	public String getTitle();
	
	public void setTitle(String title);
	
	public String getUrl();
	
	public void setUrl(String url);
	
}
