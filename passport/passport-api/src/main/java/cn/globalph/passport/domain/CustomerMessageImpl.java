package cn.globalph.passport.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationToOneLookup;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.passport.service.type.CustomerMessageStatusType;

@Entity
@Table(name = "PH_CUSTOMER_MESSAGE")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "用户消息")
public class CustomerMessageImpl implements CustomerMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CUSTOMER_MESSAGE_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
    @Column(name = "MESSAGE_TEXT",length = Integer.MAX_VALUE - 1)
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    @AdminPresentation(friendlyName = "消息内容",fieldType = SupportedFieldType.HTML_BASIC)
    private String messageText;
    
    @Column(name = "MESSAGE_TITLE")
    @AdminPresentation(friendlyName="消息标题", prominent = true)
    private String title;
    
    @Column(name = "URL")
    @AdminPresentation(friendlyName="消息URL")
    private String url;
    
    @Column(name = "STATUS")
    @AdminPresentation(friendlyName="消息状态", prominent=true, fieldType=SupportedFieldType.BROADLEAF_ENUMERATION, broadleafEnumeration="cn.globalph.passport.service.type.CustomerMessageStatusType")
    private String status;
    
    @Column(name = "CREATE_TIME")
    @AdminPresentation(friendlyName = "创建时间",prominent=true)
    private Date createTime;
    
    @ManyToOne(targetEntity=CustomerImpl.class)
    @JoinColumn(name = "CUSTOMER_ID")
    @AdminPresentation(friendlyName = "用户",prominent=true)
    @AdminPresentationToOneLookup
    private Customer customer;
    
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getMessageText() {
		return this.messageText;
	}

	@Override
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	@Override
	public Date getCreateTime() {
		return this.createTime;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public Customer getCustomer() {
		return this.customer;
	}

	@Override
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public CustomerMessageStatusType getStatus() {
		return CustomerMessageStatusType.getInstance(this.status);
	}

	@Override
	public void setStatus(CustomerMessageStatusType status) {
		this.status =  status.getType();
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getUrl() {
		return this.url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}
}
