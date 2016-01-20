package cn.globalph.passport.domain;

import java.io.Serializable;
import java.util.List;

public interface Community extends Serializable {

	public Long getId();
	public void setId(Long id);

	public String getCommunityName();
	public void setCommunityName(String communityName);

	public Boolean getIsHead();
	public void setIsHead(Boolean isHead);

	public List<NetNode> getNetNodes();
	public void setNetNodes(List<NetNode> netNodes);

	public List<Community> getChildComms();
	public void setChildComms(List<Community> childComms);

	public Community getCommunity();
	public void setCommunity(Community community);

}
