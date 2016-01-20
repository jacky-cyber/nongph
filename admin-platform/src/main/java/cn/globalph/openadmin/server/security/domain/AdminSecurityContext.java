package cn.globalph.openadmin.server.security.domain;

import cn.globalph.openadmin.server.service.type.ContextType;

import java.io.Serializable;
import java.util.Set;

public interface AdminSecurityContext extends Serializable {

    public ContextType getContextType();

    public void setContextType(ContextType contextType);

    public String getContextKey();

    public void setContextKey(String contextKey);

    public Set<AdminRole> getAllRoles();

    public void setAllRoles(Set<AdminRole> allRoles);

    public Set<AdminPermission> getAllPermissions();

    public void setAllPermissions(Set<AdminPermission> allPermissions);

}
