package cn.globalph.passport.domain;

import java.io.Serializable;

public interface Role extends Serializable{

    public Long getId();

    public void setId(Long id);

    public String getRoleName();

    public void setRoleName(String roleName);

}
