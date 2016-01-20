package cn.globalph.admin.web.rulebuilder.service.options;

import cn.globalph.b2c.order.service.type.FulfillmentType;
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
@Component("blFulfillmentTypeOptionsExtensionListener")
public class FulfillmentTypeEnumOptionsExtensionListener extends AbstractRuleBuilderEnumOptionsExtensionListener {

    @Override
    protected Map<String, Class<? extends EnumerationType>> getValuesToGenerate() {
        Map<String, Class<? extends EnumerationType>> map = 
                new HashMap<String, Class<? extends EnumerationType>>();
        
        map.put("blcOptions_FulfillmentType", FulfillmentType.class);
        
        return map;
    }

}
