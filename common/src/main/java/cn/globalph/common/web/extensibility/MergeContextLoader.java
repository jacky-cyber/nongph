package cn.globalph.common.web.extensibility;

import org.springframework.beans.BeansException;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * <p>Additionally, Processes a {@link #PATCH_LOCATION_PARAM "patchConfigLocation"}
 * context-param and passes its value to the context instance, parsing it into
 * potentially multiple file paths which can be separated by any number of
 * commas and spaces, e.g. "WEB-INF/patch1.xml,
 * WEB-INF/patch2.xml". Ant-style path patterns are supported as well,
 * e.g. "WEB-INF/*Patch.xml,WEB-INF/spring*.xml" or "WEB-INF/&#42;&#42;/*Patch.xml".
 * The patch configuration files are merged into the above config
 * {@link cn.globalph.common.extensibility.context.merge.MergeXmlConfigResource}.
 *
 * @author felix.wu
 */
public class MergeContextLoader extends ContextLoader {

    /**
     * Name of servlet context parameter (i.e., "<code>patchConfigLocation</code>")
     * that can specify the config location for the rootId context.
     */
    public static final String PATCH_LOCATION_PARAM = "patchConfigLocation";
    
    /**
     * Name of a bean to hook before Spring shutdown for this
     * context commences.
     */
    public static final String SHUTDOWN_HOOK_BEAN = "shutdownHookBean";
    
    /**
     * Name of method to call on the shutdown hook bean before
     * Spring shutdown for this context commences
     */
    public static final String SHUTDOWN_HOOK_METHOD = "shutdownHookMethod";

    /**
     * Instantiate the rootId WebApplicationContext for this loader, either the
     * default context class or a custom context class if specified.
     * <p>This implementation expects custom contexts to implement the
     * {@link ConfigurableWebApplicationContext} interface.
     * Can be overridden in subclasses.
     * <p>In addition, {@link #customizeContext} gets called prior to refreshing the
     * context, allowing subclasses to perform custom modifications to the context.
     * @param servletContext current servlet context
     * @return the rootId WebApplicationContext
     * @throws BeansException if the context couldn't be initialized
     * @see ConfigurableWebApplicationContext
     */
    @Override
    protected WebApplicationContext createWebApplicationContext(ServletContext servletContext) throws BeansException {
        MergeXmlWebApplicationContext wac = new MergeXmlWebApplicationContext();
        wac.setServletContext(servletContext);
        wac.setConfigLocation(servletContext.getInitParameter(ContextLoader.CONFIG_LOCATION_PARAM));
        wac.setPatchLocation(servletContext.getInitParameter(PATCH_LOCATION_PARAM));
        wac.setShutdownBean(servletContext.getInitParameter(SHUTDOWN_HOOK_BEAN));
        wac.setShutdownMethod(servletContext.getInitParameter(SHUTDOWN_HOOK_METHOD));
        customizeContext(servletContext, wac);
        //NOTE: in Spring 3.1, refresh gets called automatically. All that is required is to return the context back to Spring
        //wac.refresh();

        return wac;
    }
}
