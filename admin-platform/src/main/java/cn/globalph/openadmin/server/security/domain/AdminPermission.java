package cn.globalph.openadmin.server.security.domain;

import cn.globalph.openadmin.server.security.service.type.PermissionType;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface AdminPermission extends Serializable {

    public void setId(Long id);
    public Long getId();
    public String getName();
    public void setName(String name);
    public String getDescription();
    public void setDescription(String description);
    public PermissionType getType();

    public void setType(PermissionType type);

    public List<AdminPermissionQualifiedEntity> getQualifiedEntities();

    public void setQualifiedEntities(List<AdminPermissionQualifiedEntity> qualifiedEntities);

    public Set<AdminUser> getAllUsers();

    public void setAllUsers(Set<AdminUser> allUsers);

    public AdminPermission clone();
    
    public Set<AdminRole> getAllRoles();
    public void setAllRoles(Set<AdminRole> allRoles);
    public List<AdminPermission> getAllChildPermissions();

    public List<AdminPermission> getAllParentPermissions();
    public Boolean isFriendly();
}
