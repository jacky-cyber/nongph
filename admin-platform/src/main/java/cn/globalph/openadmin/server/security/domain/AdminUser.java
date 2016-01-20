package cn.globalph.openadmin.server.security.domain;

import cn.globalph.common.sandbox.domain.SandBox;

import java.util.Map;
import java.util.Set;

/**
 * 
 * @felix.wu
 *
 */
public interface AdminUser extends AdminSecurityContext {
    public Long getId();
    public void setId(Long id);
    public String getName();
    public void setName(String name);
    public String getLogin();
    public void setLogin(String login);
    public String getPassword();
    public void setPassword(String password);
    public String getEmail();
    public void setEmail(String email);
    public Set<AdminRole> getAllRoles();
    public void setAllRoles(Set<AdminRole> allRoles);
    public String getUnencodedPassword();
    public void setUnencodedPassword(String unencodedPassword);

    /**
     * Stores the user's phone number.
     * @param phone
     */
    public void setPhoneNumber(String phone);

    /**
     * Returns the users phone number.
     * @return
     */
    public String getPhoneNumber();

    /**
     * Sets the users active status.   A user whose active status is set to false
     * will not be able to login.
     *
     * @param activeStatus
     */
    public void setActiveStatusFlag(Boolean activeStatus);

    /**
     * Returns the users active status.    A user whose active status is set to
     * false will not be able to login.
     *
     * @return
     */
    public Boolean getActiveStatusFlag();

    /**
     * The current sandbox associated with this user.
     * This is primarily intended to be used by the BLC-CMS workflow
     * processes.
     *
     * If null, the user is using their own SandBox.
     *
     * @return
     */
    public SandBox getOverrideSandBox();

    /**
     * Overrides the user's sandbox.    This could be used
     * to setup shared sandboxes.  Setting to null will
     * mean that the user is setup to use the sandbox associated
     * with their user.
     *
     * @param sandbox
     */
    public void setOverrideSandBox(SandBox sandbox);

    public Set<AdminPermission> getAllPermissions();
    public void setAllPermissions(Set<AdminPermission> allPermissions);
    //public AdminUser clone();

    public Map<String, String> getAdditionalFields();

    public void setAdditionalFields(Map<String, String> additionalFields);
    
    /**
     * @return the id of the last sandbox this admin user used
     */
    public Long getLastUsedSandBoxId();

    /**
     * Sets the last used sandbox for this admin user
     * 
     * @param sandBoxId
     */
    public void setLastUsedSandBoxId(Long sandBoxId);

}
