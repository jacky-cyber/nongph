package cn.globalph.common.i18n.dao;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

import javax.persistence.EntityManager;


/**
 * @author Andre Azzolini (apazzolini)
 */
public interface TranslationDaoExtensionHandler extends ExtensionHandler {
    
    /**
     * If there is a different id that should be used for a translation lookup instead of the given entityId,
     * the handler should place the result in the {@link ExtensionResultHolder} argument.
     * 
     * @param erh
     * @param em
     * @param clazz
     * @param entityId
     * @return the status of the call to the given extension handler
     */
    public ExtensionResultStatusType overrideRequestedId(ExtensionResultHolder erh, EntityManager em, 
            Class<?> clazz, Long entityId);

}
