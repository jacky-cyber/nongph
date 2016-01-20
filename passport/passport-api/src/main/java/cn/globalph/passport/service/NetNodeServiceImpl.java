package cn.globalph.passport.service;

import java.util.List;

import javax.annotation.Resource;

import cn.globalph.passport.dao.NetNodeDao;
import cn.globalph.passport.domain.NetNode;

import org.springframework.stereotype.Service;

@Service("nphNetNodeService")
public class NetNodeServiceImpl implements NetNodeService {

	@Resource
	protected NetNodeDao netNodeDao;
	
	@Override
	public List<NetNode> findNetNodeByCommunity(Long communityId) {
		return netNodeDao.findNetNodeByCommunity(communityId);
	}

	@Override
	public NetNode readNetNodeById(Long netNodeId) {
		return netNodeDao.readNetNodeById(netNodeId);
	}

}
