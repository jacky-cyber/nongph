package cn.globalph.b2c.web.money;

import cn.globalph.common.money.CurrencyConversionService;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;

import java.util.HashMap;

public interface CurrencyConversionPricingFilter extends Filter {
    
    @SuppressWarnings("rawtypes")
    public HashMap getCurrencyConversionContext(ServletRequest request);
    
    public CurrencyConversionService getCurrencyConversionService(ServletRequest request);
    
}
