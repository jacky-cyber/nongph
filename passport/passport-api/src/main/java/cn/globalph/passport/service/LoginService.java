package cn.globalph.passport.service;

import cn.globalph.passport.domain.Customer;

import org.springframework.security.core.Authentication;

public interface LoginService {
    public Authentication loginCustomer(Customer customer);

    public Authentication loginCustomer(String username, String clearTextPassword);

    public void switchCustomer(String username);

    public void logoutCustomer();
}
