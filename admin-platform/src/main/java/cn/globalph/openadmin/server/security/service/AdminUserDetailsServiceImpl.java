package cn.globalph.openadmin.server.security.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import cn.globalph.openadmin.server.security.domain.AdminPermission;
import cn.globalph.openadmin.server.security.domain.AdminRole;
import cn.globalph.openadmin.server.security.domain.AdminUser;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Jeff Fischer
 */
public class AdminUserDetailsServiceImpl implements UserDetailsService {

    @Resource(name="blAdminSecurityService")
    protected AdminSecurityService adminSecurityService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        AdminUser adminUser = adminSecurityService.readAdminUserByUserName(username);
        if (adminUser == null || adminUser.getActiveStatusFlag() == null || !adminUser.getActiveStatusFlag()) {
            throw new UsernameNotFoundException("The user was not found");
        }

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (AdminRole role : adminUser.getAllRoles()) {
            for (AdminPermission permission : role.getAllPermissions()) {
                if(permission.isFriendly()) {
                    for (AdminPermission childPermission : permission.getAllChildPermissions()) {
                        authorities.add(new SimpleGrantedAuthority(childPermission.getName()));
                    }
                } else {
                    authorities.add(new SimpleGrantedAuthority(permission.getName()));
                }
            }
        }
        for (AdminPermission permission : adminUser.getAllPermissions()) {
            if(permission.isFriendly()) {
                for (AdminPermission childPermission : permission.getAllChildPermissions()) {
                    authorities.add(new SimpleGrantedAuthority(childPermission.getName()));
                }
            } else {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        for (String perm : AdminSecurityService.DEFAULT_PERMISSIONS) {
            authorities.add(new GrantedAuthorityImpl(perm));
        }
        return new AdminUserDetails(adminUser.getId(), username, adminUser.getPassword(), true, true, true, true, authorities);
    }

}
