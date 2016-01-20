package cn.globalph.b2c.product.dao;

import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.SkuFee;
import cn.globalph.b2c.product.domain.SkuImpl;
import cn.globalph.common.persistence.EntityConfiguration;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import java.util.List;

@Repository("blSkuDao")
public class SkuDaoImpl implements SkuDao {

    @PersistenceContext(unitName="blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public Sku save(Sku sku) {
        return em.merge(sku);
    }
    
    @Override
    public SkuFee saveSkuFee(SkuFee fee) {
        return em.merge(fee);
    }

    @Override
    public Sku readSkuById(Long skuId) {
        return (Sku) em.find(SkuImpl.class, skuId);
    }

    @Override
    public Sku readFirstSku() {
        TypedQuery<Sku> query = em.createNamedQuery("BC_READ_FIRST_SKU", Sku.class);
        return query.getSingleResult();
    }

    @Override
    public List<Sku> readAllSkus() {
        TypedQuery<Sku> query = em.createNamedQuery("BC_READ_ALL_SKUS", Sku.class);
        return query.getResultList();
    }

    @Override
    public List<Sku> readSkusById(List<Long> ids) {
        TypedQuery<Sku> query = em.createNamedQuery("BC_READ_SKUS_BY_ID", Sku.class);
        query.setParameter("skuIds", ids);
        return query.getResultList();
    }

    @Override
    public void delete(Sku sku){
        if (!em.contains(sku)) {
            sku = readSkuById(sku.getId());
        }
        em.remove(sku);     
    }

    @Override
    public Sku create() {
        return (Sku) entityConfiguration.createEntityInstance(Sku.class.getName());
    }

	@Override
	public Sku findSkuByProductURIAndId(String productURI, Long skuId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Sku> cq = cb.createQuery( Sku.class );
        Root<SkuImpl> fromRoot = cq.from( SkuImpl.class );
        Join<Sku, Product> prdJoin = fromRoot.join("product");
        cq.select( fromRoot );
        cq.where( cb.equal(fromRoot.get("id"), skuId), 
        		  cb.equal(prdJoin.get("url"),productURI) );
        
        Query q = em.createQuery( cq );
        q.setHint(QueryHints.HINT_CACHEABLE, true);
        q.setHint(QueryHints.HINT_CACHE_REGION, "query.sku");
        
        return (Sku)q.getSingleResult();
	}
}