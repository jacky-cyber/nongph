package cn.globalph.passport.dao;

import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.passport.domain.CustomerForgotPasswordSecurityToken;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository("blCustomerForgotPasswordSecurityTokenDao")
public class CustomerForgotPasswordSecurityTokenDaoImpl implements CustomerForgotPasswordSecurityTokenDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public CustomerForgotPasswordSecurityToken readToken(String token) {
        return (CustomerForgotPasswordSecurityToken) em.find(entityConfiguration.lookupEntityClass("cn.globalph.passport.domain.CustomerForgotPasswordSecurityToken"), token);        
    }

    @Override
    public CustomerForgotPasswordSecurityToken saveToken(CustomerForgotPasswordSecurityToken token) {
        return em.merge(token);
    }
}
