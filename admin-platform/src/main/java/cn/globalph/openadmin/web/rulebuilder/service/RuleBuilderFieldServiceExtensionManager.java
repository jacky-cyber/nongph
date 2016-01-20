package cn.globalph.openadmin.web.rulebuilder.service;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * @author bpolster
 */
@Service("blRuleBuilderFieldServiceExtensionManager")
public class RuleBuilderFieldServiceExtensionManager extends ExtensionManager<RuleBuilderFieldServiceExtensionHandler> {

    public RuleBuilderFieldServiceExtensionManager() {
        super(RuleBuilderFieldServiceExtensionHandler.class);
    }

}
