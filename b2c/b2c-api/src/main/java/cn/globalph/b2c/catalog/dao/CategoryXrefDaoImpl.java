package cn.globalph.b2c.catalog.dao;

import cn.globalph.b2c.catalog.domain.CategoryProductXref;
import cn.globalph.b2c.catalog.domain.CategoryXref;
import cn.globalph.b2c.catalog.domain.CategoryXrefImpl;
import cn.globalph.common.persistence.EntityConfiguration;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Jeff Fischer
 */
@Repository("blCategoryXrefDao")
public class CategoryXrefDaoImpl implements CategoryXrefDao {

    @PersistenceContext(unitName="blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public List<CategoryXref> readXrefsByCategoryId(Long categoryId) {
        TypedQuery<CategoryXref> query = em.createNamedQuery("BC_READ_CATEGORY_XREF_BY_CATEGORYID", CategoryXref.class);
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }

    @Override
    public List<CategoryXref> readXrefsBySubCategoryId(Long subCategoryId) {
        TypedQuery<CategoryXref> query = em.createNamedQuery("BC_READ_CATEGORY_XREF_BY_SUBCATEGORYID", CategoryXref.class);
        query.setParameter("subCategoryId", subCategoryId);
        return query.getResultList();
    }

    @Override
    public CategoryXref readXrefByIds(Long categoryId, Long subCategoryId) {
        Query query = em.createNamedQuery("BC_READ_CATEGORY_XREF_BY_IDS");
        query.setParameter("categoryId", categoryId);
        query.setParameter("subCategoryId", subCategoryId);
        return (CategoryXref) query.getSingleResult();
    }

    @Override
    public CategoryXref save(CategoryXrefImpl categoryXref){
        return em.merge(categoryXref);
    }

    @Override
    public void delete(CategoryXref categoryXref) {
        if (!em.contains(categoryXref)) {
            categoryXref = readXrefByIds(categoryXref.getCategory().getId(), categoryXref.getSubCategory().getId());
        }
        em.remove(categoryXref);        
    }

    @Override
    public CategoryProductXref save(CategoryProductXref categoryProductXref) {
        return em.merge(categoryProductXref);
    }
    
}
