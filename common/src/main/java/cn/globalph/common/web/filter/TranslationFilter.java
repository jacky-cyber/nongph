package cn.globalph.common.web.filter;

import cn.globalph.common.i18n.service.TranslationConsiderationContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Responsible for setting the necessary attributes on the {@link TranslationConsiderationContext}.
 * 
 */
@Component("blTranslationFilter")
public class TranslationFilter extends GenericFilterBean {
    
    @Resource(name = "blTranslationRequestProcessor")
    protected TranslationRequestProcessor translationRequestProcessor;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            translationRequestProcessor.process(new ServletWebRequest((HttpServletRequest) request, (HttpServletResponse) response));
            filterChain.doFilter(request, response);
        } finally {
            translationRequestProcessor.postProcess(new ServletWebRequest((HttpServletRequest) request, (HttpServletResponse) response));
        }
    }
}
