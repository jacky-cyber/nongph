package cn.globalph.openadmin.server.security.dao;

import cn.globalph.openadmin.server.security.domain.AdminUser;

import java.util.List;
import java.util.Set;

/**
 * 
 * @felix.wu
 *
 */
public interface AdminUserDao {
    List<AdminUser> readAllAdminUsers();
    AdminUser readAdminUserById(Long id);
    AdminUser readAdminUserByUserName(String userName);
    AdminUser saveAdminUser(AdminUser user);
    void deleteAdminUser(AdminUser user);
    List<AdminUser> readAdminUserByEmail(String emailAddress);
    List<AdminUser> readAdminUsersByIds(Set<Long> ids);
}
