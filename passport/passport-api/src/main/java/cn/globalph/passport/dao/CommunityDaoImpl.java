package cn.globalph.passport.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.passport.domain.Community;
import cn.globalph.passport.domain.CommunityImpl;

import org.springframework.stereotype.Repository;

@Repository("nphCommunityDao")
public class CommunityDaoImpl implements CommunityDao {

	@PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Community> findFirstLevelCommunities() {
	    Query query = em.createNamedQuery("NPH_FIND_FIRST_LEVEL_COMMUNITIES");
        return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Community> findChildCommunities(Long communityId) {
		Query query = em.createNamedQuery("NPH_FIND_CHILD_COMMUNITIES");
		query.setParameter("communityId", communityId);
        return query.getResultList();
	}

	@Override
	public Community readCommunityById(Long communityId) {
		 return (Community) em.find(CommunityImpl.class, communityId);
	}

}
