package cn.globalph.passport.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.passport.domain.NetNode;
import cn.globalph.passport.domain.NetNodeImpl;

import org.springframework.stereotype.Repository;

@Repository("nphNetNodeDao")
public class NetNodeDaoImpl implements NetNodeDao {

	@PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NetNode> findNetNodeByCommunity(Long communityId) {
		Query query = em.createNamedQuery("NPH_NET_NODE_BY_COMMUNITY");
		query.setParameter("communityId", communityId);
        return query.getResultList();
	}

	@Override
	public NetNode readNetNodeById(Long netNodeId) {
		return (NetNode)em.find(NetNodeImpl.class, netNodeId);
	}

}
