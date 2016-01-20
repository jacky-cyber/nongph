package cn.globalph.openadmin.server.security.dao;

import cn.globalph.openadmin.server.security.domain.AdminRole;

import java.util.List;

/**
 * 
 * @felix.wu
 *
 */
public interface AdminRoleDao {
    public List<AdminRole> readAllAdminRoles();
    public AdminRole readAdminRoleById(Long id);
    public AdminRole saveAdminRole(AdminRole role);
    public void deleteAdminRole(AdminRole role);
}
