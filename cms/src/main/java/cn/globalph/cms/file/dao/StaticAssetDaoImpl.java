package cn.globalph.cms.file.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cn.globalph.cms.file.domain.StaticAsset;
import cn.globalph.cms.file.domain.StaticAssetImpl;
import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.common.sandbox.domain.SandBox;
import cn.globalph.common.sandbox.domain.SandBoxImpl;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * felix.wu
 */
@Repository("blStaticAssetDao")
public class StaticAssetDaoImpl implements StaticAssetDao {

    private static SandBox DUMMY_SANDBOX = new SandBoxImpl();
    {
        DUMMY_SANDBOX.setId(-1l);
    }

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public StaticAsset readStaticAssetById(Long id) {
        return em.find(StaticAssetImpl.class, id);
    }
    
    public List<StaticAsset> readAllStaticAssets() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<StaticAsset> criteria = builder.createQuery(StaticAsset.class);
        Root<StaticAssetImpl> handler = criteria.from(StaticAssetImpl.class);
        criteria.select(handler);
        try {
            return em.createQuery(criteria).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<StaticAsset>();
        }
    }

    @Override
    public StaticAsset readStaticAssetByFullUrl(String fullUrl) {
        TypedQuery<StaticAsset> query = em.createNamedQuery("BC_READ_STATIC_ASSET_BY_FULL_URL", StaticAsset.class);
        query.setParameter("fullUrl", fullUrl);
        query.setHint(QueryHints.HINT_CACHEABLE, true);

        List<StaticAsset> results = query.getResultList();
        if (CollectionUtils.isEmpty(results)) {
            return null;
        } else {
            return results.iterator().next();
        }
    }

    @Override
    public StaticAsset addOrUpdateStaticAsset(StaticAsset asset, boolean clearLevel1Cache) {
        if (clearLevel1Cache) {
            em.detach(asset);
        }
        return em.merge(asset);
    }

    @Override
    public void delete(StaticAsset asset) {
        if (!em.contains(asset)) {
            asset = readStaticAssetById(asset.getId());
        }
        em.remove(asset);
    }

}
