package cn.globalph.passport.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyCollection;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.client.AddMethodType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "NPH_COMMUNITY")
@AdminPresentationClass(friendlyName = "Community")
public class CommunityImpl implements AdminMainEntity, Community {

	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(generator= "CommunityId")
    @GenericGenerator(
        name="CommunityId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="CommunityImpl"),
            @Parameter(name="entity_name", value="com.nonph.admin.netnode.domain.CommunityImpl")
        }
    )
    @Column(name = "COMMUNITY_ID")
	private Long id;
    
    @Column(name = "COMMUNITY_NAME")
    @AdminPresentation(friendlyName = "Community Name", prominent = true)
    private String communityName;
    
    @Column(name = "ISHEAD")
    @AdminPresentation(friendlyName = "Head",prominent = true)
    private Boolean isHead;
    
    @OneToMany(mappedBy = "community", targetEntity = NetNodeImpl.class, cascade = {CascadeType.ALL})
    @AdminPresentationCollection(addType = AddMethodType.LOOKUP, friendlyName = "Net Nodes")
    @ClonePolicyCollection
    private List<NetNode> netNodes = new ArrayList<NetNode>();
    
    @OneToMany(mappedBy = "community", targetEntity = CommunityImpl.class, cascade = {CascadeType.ALL})
    @AdminPresentationCollection(addType = AddMethodType.LOOKUP, friendlyName = "child communities")
    private List<Community> childComms = new ArrayList<Community>();
    
    @ManyToOne(targetEntity = CommunityImpl.class)
    @JoinColumn(name = "PARENT_COMMUNITY_ID")
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
	public String getCommunityName() {
		return this.communityName;
	}

	@Override
	public void setCommunityName(String communityName) {
		this.communityName = communityName;

	}

	@Override
	public Boolean getIsHead() {
		return this.isHead;
	}

	@Override
	public void setIsHead(Boolean isHead) {
		this.isHead = isHead;

	}

	@Override
	public String getMainEntityName() {
		return getCommunityName();
	}

	@Override
	public List<NetNode> getNetNodes() {
		return this.netNodes;
	}

	@Override
	public void setNetNodes(List<NetNode> netNodes) {
		this.netNodes = netNodes;
	}

	@Override
	public List<Community> getChildComms() {
		return this.childComms;
	}

	@Override
	public void setChildComms(List<Community> childComms) {
		this.childComms = childComms;
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

