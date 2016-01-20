package cn.globalph.openadmin.server.security.dao;

import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.openadmin.server.security.domain.AdminModule;
import cn.globalph.openadmin.server.security.domain.AdminSection;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author elbertbautista
 *
 */
@Repository("blAdminNavigationDao")
public class AdminNavigationDaoImpl implements AdminNavigationDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public List<AdminModule> readAllAdminModules() {
        Query query = em.createNamedQuery("BC_READ_ALL_ADMIN_MODULES");
        query.setHint(org.hibernate.ejb.QueryHints.HINT_CACHEABLE, true);
        List<AdminModule> modules = query.getResultList();
        return modules;
    }

    @Override
    public List<AdminSection> readAllAdminSections() {
        Query query = em.createNamedQuery("BC_READ_ALL_ADMIN_SECTIONS");
        query.setHint(org.hibernate.ejb.QueryHints.HINT_CACHEABLE, true);
        List<AdminSection> sections = query.getResultList();
        return sections;
    }
    
    @Override
    public AdminSection readAdminSectionByClass(Class<?> clazz) {
        String className = clazz.getName();
        
        // Try to find a section for the exact input received
        AdminSection section = readAdminSectionForClassName(className);
        if (section != null) return section;
        
        // If we didn't find a section, and this class ends in Impl, try again without the Impl.
        // Most of the sections should match to the interface
        if (className.endsWith("Impl")) {
            className = className.substring(0, className.length() - 4);
            section = readAdminSectionForClassName(className);
        }
        if (section != null) return section;
        
        // If we still didn't find a section, we should walk up the inheritance hierarchy tree looking for
        // implemented interfaces or extensions of those interfaces.
        // TODO: APA
        
        return null;
    }
    
    protected AdminSection readAdminSectionForClassName(String className) {
        TypedQuery<AdminSection> q = em.createQuery(
            "select s from " + AdminSection.class.getName() + " s where s.ceilingEntity = :className", AdminSection.class);
        q.setParameter("className", className);
        q.setHint(org.hibernate.ejb.QueryHints.HINT_CACHEABLE, true);
        List<AdminSection> result = q.getResultList();
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        return q.getResultList().get(0);
    }

    @Override
    public AdminSection readAdminSectionByURI(String uri) {
        Query query = em.createNamedQuery("BC_READ_ADMIN_SECTION_BY_URI");
        query.setParameter("uri", uri);
        query.setHint(org.hibernate.ejb.QueryHints.HINT_CACHEABLE, true);
        AdminSection adminSection = null;
        try {
             adminSection = (AdminSection) query.getSingleResult();
        } catch (NoResultException e) {
           //do nothing
        }
        return adminSection;
    }

    @Override
    public AdminSection readAdminSectionBySectionKey(String sectionKey) {
        Query query = em.createNamedQuery("BC_READ_ADMIN_SECTION_BY_SECTION_KEY");
        query.setHint(org.hibernate.ejb.QueryHints.HINT_CACHEABLE, true);
        query.setParameter("sectionKey", sectionKey);
        AdminSection adminSection = null;
        try {
            adminSection = (AdminSection) query.getSingleResult();
        } catch (NoResultException e) {
            //do nothing
        }
        return adminSection;
    }

}
