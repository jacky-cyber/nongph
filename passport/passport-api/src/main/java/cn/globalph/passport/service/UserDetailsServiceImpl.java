package cn.globalph.passport.service;

import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerRole;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is being un-deprecated because we want the query for the customer to happen through Hibernate instead of
 * through raw JDBC, which is the case when <sec:jdbc-user-service /> is used. We need the query to go through Hibernate
 * so that we are able to attach the necessary filters in certain circumstances.
 * 
 * @author felix.wu
 */
@Service("blUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource(name = "blCustomerService")
    protected CustomerService customerService;

    @Resource(name = "blRoleService")
    protected RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        //login with loginName/mail/phone
    	Customer customer = customerService.readCustomerByUsername(username);
    	
        if (customer == null) {
            throw new UsernameNotFoundException("The customer was not found");
        }

        List<GrantedAuthority> grantedAuthorities = createGrantedAuthorities(roleService.findCustomerRolesByCustomerId(customer.getId()));
        return new CustomerUserDetails(customer.getId(), customer.getLoginName(), customer.getPassword(), !customer.isDeactivated(), true, !customer.isPasswordChangeRequired(), true, grantedAuthorities);
    }

    protected List<GrantedAuthority> createGrantedAuthorities(List<CustomerRole> customerRoles) {
        boolean roleUserFound = false;

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (CustomerRole role : customerRoles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            if (role.getRoleName().equals("ROLE_USER")) {
                roleUserFound = true;
            }
        }

        if (!roleUserFound) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return grantedAuthorities;
    }
    
}