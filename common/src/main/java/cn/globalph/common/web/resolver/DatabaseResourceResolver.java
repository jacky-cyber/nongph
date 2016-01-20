package cn.globalph.common.web.resolver;

import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;

import java.io.InputStream;

import javax.annotation.Resource;


/**
 * An implementation of {@link IResourceResolver} that provides an extension point for retrieving
 * templates from the database.
 * 
 * @author felix.wu
 */
@Service("blDatabaseResourceResolver")
public class DatabaseResourceResolver implements IResourceResolver {
    
    @Override
    public String getName() {
        return "BL_DATABASE";
    }
    
    @Resource(name = "blDatabaseResourceResolvertensionManager")
    protected DatabaseResourceResolverExtensionManager extensionManager;

    @Override
    public InputStream getResourceAsStream(TemplateProcessingParameters params, String resourceName) {
        ExtensionResultHolder erh = new ExtensionResultHolder();
        ExtensionResultStatusType result = extensionManager.getHandlerProxy().resolveResource(erh, params, resourceName);
        if (result ==  ExtensionResultStatusType.HANDLED) {
            return (InputStream) erh.getContextMap().get(DatabaseResourceResolverExtensionHandler.IS_KEY);
        }
        return null;
    }

}
