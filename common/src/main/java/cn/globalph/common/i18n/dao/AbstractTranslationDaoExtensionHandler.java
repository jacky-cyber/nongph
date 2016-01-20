package cn.globalph.common.i18n.dao;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

import javax.persistence.EntityManager;


/**
 * 
 * @author Andre Azzolini (apazzolini)
 */
public abstract class AbstractTranslationDaoExtensionHandler extends AbstractExtensionHandler implements
        TranslationDaoExtensionHandler {

    @Override
    public ExtensionResultStatusType overrideRequestedId(ExtensionResultHolder erh, EntityManager em,
            Class<?> Clazz, Long entityId) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
    
}
