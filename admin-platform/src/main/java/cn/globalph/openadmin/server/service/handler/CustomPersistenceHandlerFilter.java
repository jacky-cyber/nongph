package cn.globalph.openadmin.server.service.handler;

/**
 * @author Jeff Fischer
 */
public interface CustomPersistenceHandlerFilter {
    
    public boolean shouldUseHandler(String handlerClassName);

}
