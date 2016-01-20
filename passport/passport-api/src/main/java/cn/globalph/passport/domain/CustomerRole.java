package cn.globalph.passport.domain;

import java.io.Serializable;

public interface CustomerRole extends Serializable {

    public Long getId();

    public void setId(Long id);

    public Customer getCustomer();

    public void setCustomer(Customer customer);

    public Role getRole();

    public void setRole(Role role);

    public String getRoleName();

}
