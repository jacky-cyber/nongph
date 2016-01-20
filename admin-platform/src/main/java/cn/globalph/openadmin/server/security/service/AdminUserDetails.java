package cn.globalph.openadmin.server.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


/**
 * Extended DTO class to support salts based on the primary key of the admin user. This allows username changes for
 * admin users.
 * 
 * @see {@link AdminSecurityService}
 * @see {@link AdminUserDetailsServiceImpl}
 * @author Phillip Verheyden (phillipuniverse)
 */
public class AdminUserDetails extends User {
    
    private static final long serialVersionUID = 1L;
    
    protected Long id;
    
    public AdminUserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(id, username, password, true, true, true, true, authorities);
    }
    
    public AdminUserDetails(Long id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }
    
    public AdminUserDetails withId(Long id) {
        setId(id);
        return this;
    }
    
    /**
     * @return the primary key of the Admin User
     */
    public Long getId() {
        return id;
    }
    
    /**
     * @param id the primary key of the Admin User
     */
    public void setId(Long id) {
        this.id = id;
    }

}