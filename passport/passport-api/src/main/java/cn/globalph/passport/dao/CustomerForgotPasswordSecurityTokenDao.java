package cn.globalph.passport.dao;

import cn.globalph.passport.domain.CustomerForgotPasswordSecurityToken;

public interface CustomerForgotPasswordSecurityTokenDao {
    public CustomerForgotPasswordSecurityToken readToken(String token);
    public CustomerForgotPasswordSecurityToken saveToken(CustomerForgotPasswordSecurityToken token);
}
