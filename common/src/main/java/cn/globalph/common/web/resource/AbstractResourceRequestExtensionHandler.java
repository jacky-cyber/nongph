package cn.globalph.common.web.resource;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;


/**
 * @author Andre Azzolini (apazzolini)
 */
public class AbstractResourceRequestExtensionHandler extends AbstractExtensionHandler 
        implements ResourceRequestExtensionHandler {

    @Override
    public ExtensionResultStatusType getOverrideResource(String path, ExtensionResultHolder erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}
