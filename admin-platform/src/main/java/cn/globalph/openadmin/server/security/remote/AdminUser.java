package cn.globalph.openadmin.server.security.remote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @felix.wu
 *
 */
public class AdminUser implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String userName;
    protected List<String> roles = new ArrayList<String>();
    protected List<String> permissions = new ArrayList<String>();
    protected String currentSandBoxId;
    protected Long id;
    protected String email;
    protected String phoneNumber;
    protected String name;
    
    public List<String> getRoles() {
        return roles;
    }
    
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    public List<String> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCurrentSandBoxId() {
        return currentSandBoxId;
    }

    public void setCurrentSandBoxId(String currentSandBoxId) {
        this.currentSandBoxId = currentSandBoxId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
