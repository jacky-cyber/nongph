package cn.globalph.b2c.catalog.dao;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.CategoryImpl;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.common.persistence.Status;
import cn.globalph.common.sandbox.SandBoxHelper;
import cn.globalph.common.time.SystemTime;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author felix.wu
 */
@Repository("blCategoryDao")
public class CategoryDaoImpl implements CategoryDao {

    protected Long currentDateResolution = 10000L;
    protected Date cachedDate = SystemTime.asDate();

    protected Date getCurrentDateAfterFactoringInDateResolution() {
        Date returnDate = SystemTime.getCurrentDateWithinTimeResolution(cachedDate, currentDateResolution);
        if (returnDate != cachedDate) {
            if (SystemTime.shouldCacheDate()) {
                cachedDate = returnDate;
            }
        }
        return returnDate;
    }

    @PersistenceContext(unitName="blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Resource(name = "blSandBoxHelper")
    protected SandBoxHelper sandBoxHelper;

    @Resource(name = "blCategoryDaoExtensionManager")
    protected CategoryDaoExtensionManager extensionManager;

    @Override
    public Category save(Category category) {
        return em.merge(category);
    }

    @Override
    public Category readCategoryById(Long categoryId) {
        return em.find(CategoryImpl.class, categoryId);
    }
    
    @Override
    public List<Category> readAllParentCategories() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Category> criteria = builder.createQuery(Category.class);
        Root<CategoryImpl> category = criteria.from(CategoryImpl.class);

        criteria.select(category);
        criteria.where(builder.isNull(category.get("defaultParentCategory")));
        TypedQuery<Category> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Category> readCategoriesByName(String categoryName) {
        TypedQuery<Category> query = em.createNamedQuery("BC_READ_CATEGORY_BY_NAME", Category.class);
        query.setParameter("categoryName", categoryName);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        return query.getResultList();
    }

    @Override
    public List<Category> readCategoriesByName(String categoryName, int limit, int offset) {
        TypedQuery<Category> query = em.createNamedQuery("BC_READ_CATEGORY_BY_NAME", Category.class);
        query.setParameter("categoryName", categoryName);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public List<Category> readAllCategories() {
        TypedQuery<Category> query = em.createNamedQuery("BC_READ_ALL_CATEGORIES", Category.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        return query.getResultList();
    }

    @Override
    public List<Category> readAllCategories(int limit, int offset) {
        TypedQuery<Category> query = em.createNamedQuery("BC_READ_ALL_CATEGORIES", Category.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.getResultList();
    }

    @Override
    public List<Product> readAllProducts() {
        TypedQuery<Product> query = em.createNamedQuery("BC_READ_ALL_PRODUCTS", Product.class);
        //don't cache - could take up too much memory
        return query.getResultList();
    }

    @Override
    public List<Product> readAllProducts(int limit, int offset) {
        TypedQuery<Product> query = em.createNamedQuery("BC_READ_ALL_PRODUCTS", Product.class);
        //don't cache - could take up too much memory
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public List<Category> readAllSubCategories(Category category) {
        TypedQuery<Category> query = em.createNamedQuery("BC_READ_ALL_SUBCATEGORIES", Category.class);
        query.setParameter("defaultParentCategoryId", sandBoxHelper.mergeCloneIds(em, CategoryImpl.class, category.getId()));
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.getResultList();
    }

    @Override
    public List<Category> readAllSubCategories(Category category, int limit, int offset) {
        TypedQuery<Category> query = em.createNamedQuery("BC_READ_ALL_SUBCATEGORIES", Category.class);
        query.setParameter("defaultParentCategoryId", sandBoxHelper.mergeCloneIds(em, CategoryImpl.class, category.getId()));
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public List<Category> readActiveSubCategoriesByCategory(Category category) {
        TypedQuery<Category> query = em.createNamedQuery("BC_READ_ACTIVE_SUBCATEGORIES_BY_CATEGORY", Category.class);
        query.setParameter("defaultParentCategoryId", sandBoxHelper.mergeCloneIds(em, CategoryImpl.class, category.getId()));
        query.setParameter("currentDate", getCurrentDateAfterFactoringInDateResolution());
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.getResultList();
    }

    @Override
    public List<Category> readActiveSubCategoriesByCategory(Category category, int limit, int offset) {
        TypedQuery<Category> query = em.createNamedQuery("BC_READ_ACTIVE_SUBCATEGORIES_BY_CATEGORY", Category.class);
        query.setParameter("parentCategoryId", sandBoxHelper.mergeCloneIds(em, CategoryImpl.class, category.getId()));
        query.setParameter("currentDate", getCurrentDateAfterFactoringInDateResolution());
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public Long getCurrentDateResolution() {
        return currentDateResolution;
    }

    @Override
    public void setCurrentDateResolution(Long currentDateResolution) {
        this.currentDateResolution = currentDateResolution;
    }

    @Override
    public void delete(Category category) {
        ((Status) category).setArchived('Y');
        em.merge(category);
    }

    @Override
    public Category create() {
        return (Category) entityConfiguration.createEntityInstance(Category.class.getName());
    }

    @Override
    public Category findCategoryByURI(String uri) {
        if (extensionManager != null) {
            ExtensionResultHolder<Category> holder = new ExtensionResultHolder<Category>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().findCategoryByURI(uri, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        Query query;
        query = em.createNamedQuery("BC_READ_CATEGORY_OUTGOING_URL");
        query.setParameter("currentDate", getCurrentDateAfterFactoringInDateResolution());
        query.setParameter("url", uri);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        @SuppressWarnings("unchecked")
        List<Category> results = query.getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);

        } else {
            return null;
        }
    }

}
