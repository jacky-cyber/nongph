package cn.globalph.openadmin.web.rulebuilder.service;

import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.web.rulebuilder.dto.FieldDTO;
import cn.globalph.openadmin.web.rulebuilder.dto.FieldData;
import cn.globalph.openadmin.web.rulebuilder.dto.FieldWrapper;

import java.util.List;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public interface RuleBuilderFieldService extends Cloneable {

    public String getName();

    public FieldWrapper buildFields();

    public FieldDTO getField(String fieldName);

    public SupportedFieldType getSupportedFieldType(String fieldName);

    public SupportedFieldType getSecondaryFieldType(String fieldName);

    public List<FieldData> getFields();

    public void setFields(List<FieldData> fields);

    public RuleBuilderFieldService clone() throws CloneNotSupportedException;

    public void setRuleBuilderFieldServiceExtensionManager(RuleBuilderFieldServiceExtensionManager extensionManager);
}
