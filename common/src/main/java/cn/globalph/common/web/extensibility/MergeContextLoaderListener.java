package cn.globalph.common.web.extensibility;

import cn.globalph.common.classloader.release.ThreadLocalManager;

import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * Bootstrap listener to start up NPH's rootId {@link MergeWebApplicationContext}.
 * Simply delegates to {@link MergeContextLoader}.
 *
 * <p>This listener should be registered after
 * {@link org.springframework.web.util.Log4jConfigListener}
 * in <code>web.xml</code>, if the latter is used.
 *
 */
public class MergeContextLoaderListener extends ContextLoaderListener {

    /**
     * Create the ContextLoader to use. Can be overridden in subclasses.
     * @return the new ContextLoader
     */
    protected MergeContextLoader createContextLoader() {
        return new MergeContextLoader();
    }


    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        ThreadLocalManager.remove();
    }
}
