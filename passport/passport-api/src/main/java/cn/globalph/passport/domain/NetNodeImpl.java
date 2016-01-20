package cn.globalph.passport.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "NPH_NET_NODE")
@AdminPresentationClass(friendlyName = "Net Node")
public class NetNodeImpl implements AdminMainEntity, NetNode {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "NetNodeId")
	@GenericGenerator(name = "NetNodeId", strategy = "cn.globalph.common.persistence.IdOverrideTableGenerator", parameters = {
			@Parameter(name = "segment_value", value = "NetNodeImpl"),
			@Parameter(name = "entity_name", value = "com.nonph.admin.netnode.domain.NetNodeImpl") })
	@Column(name = "NET_NODE_ID")
	private Long id;

	@Column(name = "NET_NODE_NAME")
	@AdminPresentation(friendlyName="Net Node Name",prominent= true)
	private String netNodeName;
	
	@Column(name = "ADDRESS")
	@AdminPresentation(friendlyName="Address",prominent= true)
	private String address;
	
	@Column(name = "CONTACT_WITH")
	@AdminPresentation(friendlyName="Contact With",prominent= true)
	private String contactWith;
	
	@Column(name = "PHONE_NUMBER")
	@AdminPresentation(friendlyName="Phone Number",prominent= true)
	private String phoneNumber;
	
	@ManyToOne(targetEntity = CommunityImpl.class)
    @JoinColumn(name = "COMMUNITY_ID")
	private Community community;
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;

	}

	@Override
	public String getNetNodeName() {
		return this.netNodeName;
	}

	@Override
	public void setNetNodeName(String netNodeName) {
		this.netNodeName = netNodeName;

	}


	@Override
	public String getAddress() {
		return this.address;
	}

	@Override
	public void setAddress(String address) {
		this.address = address;

	}

	@Override
	public String getContactWith() {
		return this.contactWith;
	}

	@Override
	public void setCotactWith(String contactWith) {
		this.contactWith = contactWith;

	}

	@Override
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	@Override
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;

	}

	@Override
	public String getMainEntityName() {
		return getNetNodeName();
	}

	@Override
	public Community getCommunity() {
		return this.community;
	}

	@Override
	public void setCommunity(Community community) {
		this.community = community;
	}

}

