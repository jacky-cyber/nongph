package cn.globalph.passport.service;

import cn.globalph.common.web.WebRequestContext;
import cn.globalph.passport.domain.Customer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;

public class LoginServiceImpl implements LoginService {

    @Resource(name="blAuthenticationManager")
    private AuthenticationManager authenticationManager;
    
    @Resource(name="blUserDetailsService")
    private UserDetailsService userDetailsService;

    @Override
    public Authentication loginCustomer(Customer customer) {
        return loginCustomer(customer.getLoginName(), customer.getUnencodedPassword());
    }

    @Override
    public Authentication loginCustomer(String username, String clearTextPassword) {
        UserDetails principal = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, clearTextPassword, principal.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    public void switchCustomer(String username) {
        UserDetails principal = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Override
    public void logoutCustomer() {
        SecurityContextHolder.getContext().setAuthentication(null);
        //customerStateRequestProcessor.process(getWebRequest());
        //cartStateRequestProcessor.process(getWebRequest());
    }

    protected WebRequest getWebRequest() {
        return WebRequestContext.getWebRequestContext().getWebRequest();
    }

}
