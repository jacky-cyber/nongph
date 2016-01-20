package cn.globalph.common.web.resolver;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * @author Andre Azzolini (apazzolini), bpolster
 */
@Service("blDatabaseResourceResolvertensionManager")
public class DatabaseResourceResolverExtensionManager extends ExtensionManager<DatabaseResourceResolverExtensionHandler> {

    public DatabaseResourceResolverExtensionManager() {
        super(DatabaseResourceResolverExtensionHandler.class);
    }

    /**
     * By default, this manager will allow other handlers to process the method when a handler returns
     * HANDLED.
     */
    @Override
    public boolean continueOnHandled() {
        return false;
    }
}
