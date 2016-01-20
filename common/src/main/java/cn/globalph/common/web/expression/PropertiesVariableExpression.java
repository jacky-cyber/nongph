package cn.globalph.common.web.expression;

import cn.globalph.common.config.service.SystemPropertiesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * This Thymeleaf variable expression class provides access to runtime configuration properties that are configured
 * in development.properties, development-shared.properties, etc, for the current environment.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class PropertiesVariableExpression implements GlobalphVariableExpression {
    
    @Autowired
    protected SystemPropertiesService service;
    
    @Override
    public String getName() {
        return "props";
    }
    
    public String get(String propertyName) {
        return service.resolveSystemProperty(propertyName);
    }

    public int getAsInt(String propertyName) {
        return service.resolveIntSystemProperty(propertyName);
    }
    
    public boolean getForceShowIdColumns() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        boolean forceShow = service.resolveBooleanSystemProperty("listGrid.forceShowIdColumns");
        forceShow = forceShow || "true".equals(request.getParameter("showIds"));
        
        return forceShow;
    }
    
}
