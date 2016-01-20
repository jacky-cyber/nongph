package cn.globalph.openadmin.server.security.dao;

import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.openadmin.server.security.domain.ForgotPasswordSecurityToken;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 
 * @author bpolster
 *
 */
@Repository("blForgotPasswordSecurityTokenDao")
public class ForgotPasswordSecurityTokenDaoImpl implements ForgotPasswordSecurityTokenDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public ForgotPasswordSecurityToken readToken(String token) {
        return (ForgotPasswordSecurityToken) em.find(entityConfiguration.lookupEntityClass("cn.globalph.openadmin.server.security.domain.ForgotPasswordSecurityToken"), token);        
    }

    @Override
    public ForgotPasswordSecurityToken saveToken(ForgotPasswordSecurityToken token) {
        return em.merge(token);
    }
}
