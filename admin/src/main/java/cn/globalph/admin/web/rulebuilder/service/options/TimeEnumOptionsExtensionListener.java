package cn.globalph.admin.web.rulebuilder.service.options;

import cn.globalph.common.EnumerationType;
import cn.globalph.common.time.DayOfMonthType;
import cn.globalph.common.time.DayOfWeekType;
import cn.globalph.common.time.HourOfDayType;
import cn.globalph.common.time.MinuteType;
import cn.globalph.common.time.MonthType;
import cn.globalph.openadmin.web.rulebuilder.enums.AbstractRuleBuilderEnumOptionsExtensionListener;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Rule Builder enum options provider for {@link HourOfDayType}
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("blTimeOptionsExtensionListener")
public class TimeEnumOptionsExtensionListener extends AbstractRuleBuilderEnumOptionsExtensionListener {

    @Override
    protected Map<String, Class<? extends EnumerationType>> getValuesToGenerate() {
        Map<String, Class<? extends EnumerationType>> map = 
                new HashMap<String, Class<? extends EnumerationType>>();
        
        map.put("blcOptions_HourOfDay", HourOfDayType.class);
        map.put("blcOptions_DayOfWeek", DayOfWeekType.class);
        map.put("blcOptions_Month", MonthType.class);
        map.put("blcOptions_DayOfMonth", DayOfMonthType.class);
        map.put("blcOptions_Minute", MinuteType.class);
        
        return map;
    }

}
