package cn.globalph.passport.dao;

import java.util.List;

import cn.globalph.passport.domain.NetNode;

public interface NetNodeDao {
	
	public List<NetNode> findNetNodeByCommunity(Long communityId);
	
	public NetNode readNetNodeById(Long netNodeId);
}
