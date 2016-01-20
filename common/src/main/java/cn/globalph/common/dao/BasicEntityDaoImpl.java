package cn.globalph.common.dao;

import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.common.util.dao.DynamicDaoHelperImpl;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository("blGenericEntityDao")
public abstract class BasicEntityDaoImpl<T> implements BasicEntityDao<T> {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;
       
    protected DynamicDaoHelperImpl daoHelper = new DynamicDaoHelperImpl();
    
    @Override
    public T get(Long id) {
        return em.find(getImplClass(), id);
    }

	@Override
	public T persist(T o) {
		em.persist( o );
		return o;
	}

	@Override
	public void delete(T o) {
		em.remove( o );
	}

	@Override
	public void detach(T o) {
		em.detach( o);
	}
    
    /*
    @Override
    public Class<?> getImplClass(String className) {
        Class<?> clazz = entityConfiguration.lookupEntityClass(className);
        if (clazz == null) {
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return clazz;
    }
	*/
}
