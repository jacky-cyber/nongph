package cn.globalph.common.web.resolver;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

import org.thymeleaf.TemplateProcessingParameters;

import java.io.InputStream;


/**
 * Extension handler for resolving templates from the database.
 * 
 * @author Andre Azzolini (apazzolini), bpolster
 */
public interface DatabaseResourceResolverExtensionHandler extends ExtensionHandler {
    
    public static final String IS_KEY = "IS_KEY";
    
    /**
     * If this method returns any of the handled conditions in {@link ExtensionResultStatusType},
     * the value keyed by {@link DatabaseResourceResolverExtensionHandler.IS_KEY} in the 
     * {@link ExtensionResultHolder}'s context map will be an {@link InputStream} of the resolved resource's
     * contents.
     * 
     * @param erh
     * @param params
     * @param resourceName
     * @return whether or not a resource was resolved
     */
    public ExtensionResultStatusType resolveResource(ExtensionResultHolder erh, 
            TemplateProcessingParameters params, String resourceName);

}
