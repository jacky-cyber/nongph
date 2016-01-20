package cn.globalph.admin.web.rulebuilder.service.options;

import cn.globalph.common.EnumerationType;
import cn.globalph.common.template.TemplateType;
import cn.globalph.openadmin.web.rulebuilder.enums.AbstractRuleBuilderEnumOptionsExtensionListener;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Rule Builder enum options provider for {@link TemplateType}
 * 
 * Used in Content Tests
 * 
 * @author bpolster
 */
@Component("blTemplateTypeOptionsExtensionListener")
public class TemplateTypeEnumOptionsExtensionListener extends AbstractRuleBuilderEnumOptionsExtensionListener {

    @Override
    protected Map<String, Class<? extends EnumerationType>> getValuesToGenerate() {
        Map<String, Class<? extends EnumerationType>> map = 
                new HashMap<String, Class<? extends EnumerationType>>();
        
        map.put("blcOptions_TemplateType", TemplateType.class);
        
        return map;
    }

}
