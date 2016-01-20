package cn.globalph.passport.service;

import cn.globalph.passport.dao.RoleDao;
import cn.globalph.passport.domain.CustomerRole;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

@Service("blRoleService")
public class RoleServiceImpl implements RoleService {

    @Resource(name="blRoleDao")
    protected RoleDao roleDao;

    public List<CustomerRole> findCustomerRolesByCustomerId(Long customerId) {
        return roleDao.readCustomerRolesByCustomerId(customerId);
    }
}
