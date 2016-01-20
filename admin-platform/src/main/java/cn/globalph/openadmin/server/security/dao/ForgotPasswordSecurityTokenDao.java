package cn.globalph.openadmin.server.security.dao;

import cn.globalph.openadmin.server.security.domain.ForgotPasswordSecurityToken;

/**
 * 
 * @author bpolster
 *
 */
public interface ForgotPasswordSecurityTokenDao {
    public ForgotPasswordSecurityToken readToken(String token);
    public ForgotPasswordSecurityToken saveToken(ForgotPasswordSecurityToken token);
}
