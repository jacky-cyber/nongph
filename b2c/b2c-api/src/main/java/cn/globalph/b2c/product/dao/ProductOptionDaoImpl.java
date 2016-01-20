package cn.globalph.b2c.product.dao;

import cn.globalph.b2c.product.domain.ProductOption;
import cn.globalph.b2c.product.domain.ProductOptionImpl;
import cn.globalph.b2c.product.domain.ProductOptionValue;
import cn.globalph.b2c.product.domain.ProductOptionValueImpl;
import cn.globalph.common.persistence.EntityConfiguration;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository("blProductOptionDao")
public class ProductOptionDaoImpl implements ProductOptionDao {
    
    @PersistenceContext(unitName="blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;
    
    @Override
    public List<ProductOption> readAllProductOptions() {
        TypedQuery<ProductOption> query = em.createNamedQuery("BC_READ_ALL_PRODUCT_OPTIONS", ProductOption.class);
        return query.getResultList();
    }
    
    public ProductOption saveProductOption(ProductOption option) {
        return em.merge(option);
    }

    @Override
    public ProductOption readProductOptionById(Long id) {
        return em.find(ProductOptionImpl.class, id);
    }

    @Override
    public ProductOptionValue readProductOptionValueById(Long id) {
        return em.find(ProductOptionValueImpl.class, id);
    }

}
