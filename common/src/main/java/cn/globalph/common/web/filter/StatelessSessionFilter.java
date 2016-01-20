package cn.globalph.common.web.filter;

import cn.globalph.common.util.BLCRequestUtils;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets a request attribute that informs all Broadleaf Filters that follow NOT to use the HTTP Session.
 * 
 * Intended for use by REST api requests.
 * 
 * @author bpolster
 */
@Component("blStatelessSessionFilter")
public class StatelessSessionFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        BLCRequestUtils.setOKtoUseSession(new ServletWebRequest((HttpServletRequest) request, (HttpServletResponse) response), Boolean.FALSE);
        filterChain.doFilter(request, response);
    }
}
