package cn.globalph.common.web.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Component("blEstablishSessionFilter")
public class EstablishSessionFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (HttpServletRequest.class.isAssignableFrom(request.getClass())) {
            ((HttpServletRequest) request).getSession();
        }
        filterChain.doFilter(request, response);
    }
}
