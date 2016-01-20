package cn.globalph.openadmin.web.rulebuilder.service;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.openadmin.web.rulebuilder.dto.FieldData;

import java.util.List;


/**
 * @author bpolster
 */
public class AbstractRuleBuilderFieldServiceExtensionHandler extends AbstractExtensionHandler
        implements RuleBuilderFieldServiceExtensionHandler {

    @Override
    public ExtensionResultStatusType addFields(List<FieldData> fields, String name) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}
