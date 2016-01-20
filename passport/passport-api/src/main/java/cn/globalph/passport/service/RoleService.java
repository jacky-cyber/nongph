package cn.globalph.passport.service;

import cn.globalph.passport.domain.CustomerRole;

import java.util.List;

public interface RoleService {

    public List<CustomerRole> findCustomerRolesByCustomerId(Long customerId);
}
