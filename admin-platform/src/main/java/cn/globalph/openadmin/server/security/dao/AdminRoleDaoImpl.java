package cn.globalph.openadmin.server.security.dao;

import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.openadmin.server.security.domain.AdminRole;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

/**
 * 
 * @felix.wu
 *
 */
@Repository("blAdminRoleDao")
public class AdminRoleDaoImpl implements AdminRoleDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public void deleteAdminRole(AdminRole role) {
        if (!em.contains(role)) {
            role = readAdminRoleById(role.getId());
        }
        em.remove(role);
    }

    public AdminRole readAdminRoleById(Long id) {
        return (AdminRole) em.find(entityConfiguration.lookupEntityClass("cn.globalph.openadmin.server.security.domain.AdminRole"), id);
    }

    public AdminRole saveAdminRole(AdminRole role) {
        return em.merge(role);
    }

    @SuppressWarnings("unchecked")
    public List<AdminRole> readAllAdminRoles() {
        Query query = em.createNamedQuery("BC_READ_ALL_ADMIN_ROLES");
        List<AdminRole> roles = query.getResultList();
        return roles;
    }

}
