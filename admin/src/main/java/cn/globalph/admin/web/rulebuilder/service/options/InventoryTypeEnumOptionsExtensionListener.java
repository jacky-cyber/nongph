package cn.globalph.admin.web.rulebuilder.service.options;

import org.apache.commons.lang3.reflect.FieldUtils;

import cn.globalph.b2c.inventory.service.type.InventoryType;
import cn.globalph.common.EnumerationType;
import cn.globalph.common.time.HourOfDayType;
import cn.globalph.openadmin.web.rulebuilder.enums.AbstractRuleBuilderEnumOptionsExtensionListener;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Rule Builder enum options provider for {@link HourOfDayType}
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("blInventoryTypeOptionsExtensionListener")
public class InventoryTypeEnumOptionsExtensionListener extends AbstractRuleBuilderEnumOptionsExtensionListener {

    /**
     * Overridden to remove deprecated options
     */
    @Override
    protected Map<String, ? extends EnumerationType> getTypes(Class<? extends EnumerationType> clazz) {
        
        try {
            Map<String, ? extends EnumerationType> options =
                    (Map<String, ? extends EnumerationType>) FieldUtils.readStaticField(clazz, "TYPES", true);
            options.remove("NONE");
            options.remove("BASIC");
            return options;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected Map<String, Class<? extends EnumerationType>> getValuesToGenerate() {
        Map<String, Class<? extends EnumerationType>> map = 
                new HashMap<String, Class<? extends EnumerationType>>();
        
        map.put("blcOptions_InventoryType", InventoryType.class);
        
        return map;
    }

}
