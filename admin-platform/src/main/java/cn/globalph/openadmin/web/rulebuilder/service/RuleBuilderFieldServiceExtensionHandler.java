package cn.globalph.openadmin.web.rulebuilder.service;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.openadmin.web.rulebuilder.dto.FieldData;

import java.util.List;


/**
 * Allows Broadleaf Add-On modules to manipulate the list of rule fields.
 * 
 * @author bpolster
 */
public interface RuleBuilderFieldServiceExtensionHandler extends ExtensionHandler {
    
    public ExtensionResultStatusType addFields(List<FieldData> fields, String name);

}
