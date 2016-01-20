package cn.globalph.common.web;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;


/**
 * @author Andre Azzolini (apazzolini)
 */
public interface ThymeleafViewResolverExtensionHandler extends ExtensionHandler {
    
    public ExtensionResultStatusType overrideView(ExtensionResultHolder erh);

}
