package cn.globalph.openadmin.server.security.dao;

import java.util.List;

import cn.globalph.openadmin.server.security.domain.AdminPermission;
import cn.globalph.openadmin.server.security.domain.AdminUser;
import cn.globalph.openadmin.server.security.service.type.PermissionType;

/**
 * 
 * @felix.wu
 *
 */
public interface AdminPermissionDao {

    public List<AdminPermission> readAllAdminPermissions();
    public AdminPermission readAdminPermissionById(Long id);
    public AdminPermission readAdminPermissionByName(String name);
    public AdminPermission saveAdminPermission(AdminPermission permission);
    public void deleteAdminPermission(AdminPermission permission);
    public boolean isUserQualifiedForOperationOnCeilingEntity(AdminUser adminUser, PermissionType permissionType, String ceilingEntityFullyQualifiedName);
    public boolean isUserQualifiedForOperationOnCeilingEntityViaDefaultPermissions(String ceilingEntityFullyQualifiedName);
    public boolean doesOperationExistForCeilingEntity(PermissionType permissionType, String ceilingEntityFullyQualifiedName);

}
