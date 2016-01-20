package cn.globalph.common.web.util;

import cn.globalph.common.web.resource.WebResourceHttpRequestHandler;

import org.springframework.context.ApplicationContext;
import org.thymeleaf.Arguments;
import org.thymeleaf.spring3.context.SpringWebContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Note that the utility methods to return a js or css request handler cannot be replaced with @Resource
 * annotations due to these beans only existing in a web application context, whereas the callers of these
 * resources may exist in both web and non-web application contexts.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class ProcessorUtils {
    
    protected static Map<String, Object> cachedBeans = new HashMap<String, Object>();
    
    /**
     * Note: See the class level comment for {@link ProcessorUtils}
     * 
     * @param arguments
     * @return the "blJsResources" bean
     */
    public static WebResourceHttpRequestHandler getJsRequestHandler(Arguments arguments) {
        String key = "blJsResources";
        WebResourceHttpRequestHandler reqHandler = (WebResourceHttpRequestHandler) cachedBeans.get(key);
        if (reqHandler == null) {
            final ApplicationContext appCtx = ((SpringWebContext) arguments.getContext()).getApplicationContext();
            reqHandler = (WebResourceHttpRequestHandler) appCtx.getBean(key);
            cachedBeans.put(key, reqHandler);
        }
        return reqHandler;
    }
    
    /**
     * Note: See the class level comment for {@link ProcessorUtils}
     * 
     * @param arguments
     * @return the "blCssResources" bean
     */
    public static WebResourceHttpRequestHandler getCssRequestHandler(Arguments arguments) {
        String key = "blCssResources";
        WebResourceHttpRequestHandler reqHandler = (WebResourceHttpRequestHandler) cachedBeans.get(key);
        if (reqHandler == null) {
            final ApplicationContext appCtx = ((SpringWebContext) arguments.getContext()).getApplicationContext();
            reqHandler = (WebResourceHttpRequestHandler) appCtx.getBean(key);
            cachedBeans.put(key, reqHandler);
        }
        return reqHandler;
    }

}
