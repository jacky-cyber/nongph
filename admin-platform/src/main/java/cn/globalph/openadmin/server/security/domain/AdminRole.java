package cn.globalph.openadmin.server.security.domain;

import java.io.Serializable;
import java.util.Set;

public interface AdminRole extends Serializable {

    public void setId(Long id);
    public Long getId();
    public String getName();
    public void setName(String name);
    public String getDescription();
    public void setDescription(String description);
    public Set<AdminPermission> getAllPermissions();
    public AdminRole clone();
    
}
