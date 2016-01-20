package cn.globalph.common.web.expression;

import org.apache.commons.beanutils.PropertyUtils;

import cn.globalph.common.crossapp.service.CrossAppAuthService;
import cn.globalph.common.sandbox.domain.SandBox;
import cn.globalph.common.time.SystemTime;
import cn.globalph.common.web.WebRequestContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;


/**
 * This Thymeleaf variable expression class serves to expose elements from the BroadleafRequestContext
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class BRCVariableExpression implements GlobalphVariableExpression {
    
    @Autowired(required = false)
    @Qualifier("blCrossAppAuthService")
    protected CrossAppAuthService crossAppAuthService;

    @Override
    public String getName() {
        return "brc";
    }
    
    public SandBox getSandbox() {
        WebRequestContext brc = WebRequestContext.getWebRequestContext();
        if (brc != null) {
            return brc.getSandBox();
        }
        return null;
    }
    
    public Date getCurrentTime() {
        return SystemTime.asDate(true);
    }
    
    public Object get(String propertyName) {
        WebRequestContext brc = WebRequestContext.getWebRequestContext();
        if (brc != null) {
            try {
                return PropertyUtils.getProperty(brc, propertyName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public boolean isCsrMode() {
        return crossAppAuthService == null ? false : crossAppAuthService.hasCsrPermission();
    }
    
    public boolean isSandboxMode() {
        WebRequestContext brc = WebRequestContext.getWebRequestContext();
        return (brc == null) ? false : (brc.getSandBox() != null);
    }

}
