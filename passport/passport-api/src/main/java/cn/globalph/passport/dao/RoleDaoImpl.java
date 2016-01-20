package cn.globalph.passport.dao;

import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.AddressImpl;
import cn.globalph.passport.domain.CustomerRole;
import cn.globalph.passport.domain.Role;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@Repository("blRoleDao")
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public Address readAddressById(Long id) {
        return (Address) em.find(AddressImpl.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<CustomerRole> readCustomerRolesByCustomerId(Long customerId) {
        Query query = em.createNamedQuery("BC_READ_CUSTOMER_ROLES_BY_CUSTOMER_ID");
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
    
    public Role readRoleByName(String name) {
        Query query = em.createNamedQuery("BC_READ_ROLE_BY_NAME");
        query.setParameter("name", name);
        return (Role) query.getSingleResult();
    }

    public void addRoleToCustomer(CustomerRole customerRole) {
        em.persist(customerRole);
    }
}
