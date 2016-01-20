package cn.globalph.passport.service;

import java.util.List;

import cn.globalph.passport.domain.NetNode;

public interface NetNodeService {

	public List<NetNode> findNetNodeByCommunity(Long communityId);
	
	public NetNode readNetNodeById(Long netNodeId);
}
