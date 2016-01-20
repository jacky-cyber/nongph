package cn.globalph.common.web;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

public class AbstractThymeleafViewResolverExtensionHandler extends AbstractExtensionHandler 
        implements ThymeleafViewResolverExtensionHandler {
    
    @Override
    public ExtensionResultStatusType overrideView(ExtensionResultHolder erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}
