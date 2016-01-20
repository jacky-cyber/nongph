package cn.globalph.b2c.web.exception.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebRequestExceptionFilter implements Filter{
	protected static final Log LOG = LogFactory.getLog(WebRequestExceptionFilter.class);
	private FilterConfig filterConfig = null;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try{
			chain.doFilter(request, response);
		}catch(Exception e){
			LOG.error(e.getMessage());
			if(!response.isCommitted()){
				HttpServletResponse hres = (HttpServletResponse) response;
				String errorPage = (String)filterConfig.getInitParameter("internal-error-page");
				if(StringUtils.isNotEmpty(errorPage)){
					hres.sendRedirect(errorPage);
				}else{
					hres.sendRedirect("/errorPage/500.html");
				}
			}
		}
	}

	@Override
	public void destroy() {
		filterConfig = null;
	}
	
}
