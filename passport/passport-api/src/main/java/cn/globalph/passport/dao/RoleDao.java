package cn.globalph.passport.dao;

import cn.globalph.passport.domain.CustomerRole;
import cn.globalph.passport.domain.Role;

import java.util.List;

public interface RoleDao {

    public List<CustomerRole> readCustomerRolesByCustomerId(Long customerId);
    
    public Role readRoleByName(String name);
    
    public void addRoleToCustomer(CustomerRole customerRole);
    
}
