package cn.globalph.common.util;

import java.util.Locale;

import cn.globalph.common.web.WebRequestContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * Convenience class to faciliate getting internationalized messages. 
 * 
 * Note that this class is scanned as a bean to pick up the applicationContext, but the methods
 * this class provides should be invoked statically.
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Service("blBLCMessageUtils")
public class BLCMessageUtils implements ApplicationContextAware {

    protected static ApplicationContext applicationContext;
    
    /**
     * Returns the message requested by the code with no arguments and the currently set Java Locale on 
     * the {@link WebRequestContext} as returned by {@link WebRequestContext#getJavaLocale()}
     * 
     * @param code
     * @return the message
     */
    public static String getMessage(String code) {
        return getMessage(code, (Object) null);
    }
    
    /**
     * Returns the message requested by the code with the specified arguments and the currently set Java Locale on 
     * the {@link WebRequestContext} as returned by {@link WebRequestContext#getJavaLocale()}
     * 
     * @param code
     * @return the message
     */
    public static String getMessage(String code, Object... args) {
        return getMessageSource().getMessage(code, args, Locale.CHINA);
    }
    
    /**
     * @return the "messageSource" bean from the application context
     */
    protected static MessageSource getMessageSource() {
        return (MessageSource) applicationContext.getBean("messageSource");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BLCMessageUtils.applicationContext = applicationContext;
    }

}
