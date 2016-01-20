package cn.globalph.openadmin.web.rulebuilder.enums;

import cn.globalph.common.resource.GeneratedResource;
import cn.globalph.common.web.resource.AbstractGeneratedResourceHandler;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Generated resource handler for blc-rulebuilder-options.js.
 * 
 * Delegates to all registered {@link RuleBuilderEnumOptionsExtensionListener} to create the resource
 */
@Component("blRuleBuilderEnumOptionsResourceHandler")
public class RuleBuilderEnumOptionsResourceHandler extends AbstractGeneratedResourceHandler {
    
    @javax.annotation.Resource(name = "blRuleBuilderEnumOptionsExtensionManager")
    protected RuleBuilderEnumOptionsExtensionManager ruleBuilderEnumOptions;
    
    @Override
    public boolean canHandle(String path) {
        return "generated/js/ruleBuilder-options.js".equals(path);
    }

    @Override
    public Resource getFileContents(String path, List<Resource> locations) {
        return new GeneratedResource(ruleBuilderEnumOptions.getOptionValues().getBytes(), path);
    }

    @Override
    public boolean isCachedResourceExpired(GeneratedResource cachedResource, String path, List<Resource> locations) {
        return false;
    }

}
