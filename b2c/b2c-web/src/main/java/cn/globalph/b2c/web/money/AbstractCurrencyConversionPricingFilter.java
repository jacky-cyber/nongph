package cn.globalph.b2c.web.money;

import cn.globalph.common.money.CurrencyConversionContext;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.io.IOException;

public abstract class AbstractCurrencyConversionPricingFilter implements CurrencyConversionPricingFilter {
    
    public void destroy() {
        //do nothing
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        CurrencyConversionContext.setCurrencyConversionContext(getCurrencyConversionContext(request));
        CurrencyConversionContext.setCurrencyConversionService(getCurrencyConversionService(request));
        try {
            filterChain.doFilter(request, response);
        } finally {
            CurrencyConversionContext.setCurrencyConversionContext(null);
            CurrencyConversionContext.setCurrencyConversionService(null);
        }
    }

    public void init(FilterConfig arg0) throws ServletException {
        //do nothing
    }
    
}
