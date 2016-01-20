package cn.globalph.openadmin.web.rulebuilder.enums;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;


/**
 * Extension Manager used to aggregate option values for all registered {@link RuleBuilderEnumOptionsExtensionListener}
 * 
 */
@Component("blRuleBuilderEnumOptionsExtensionManager")
public class RuleBuilderEnumOptionsExtensionManager implements RuleBuilderEnumOptionsExtensionListener {
    
    @Resource(name = "blRuleBuilderEnumOptionsExtensionListeners")
    protected List<RuleBuilderEnumOptionsExtensionListener> listeners = new ArrayList<RuleBuilderEnumOptionsExtensionListener>();

    @Override
    public String getOptionValues() {
        StringBuilder sb = new StringBuilder();
        for (RuleBuilderEnumOptionsExtensionListener listener : listeners) {
            sb.append(listener.getOptionValues()).append("\r\n");
        }
        return sb.toString();
    }
    
    public List<RuleBuilderEnumOptionsExtensionListener> getListeners() {
        return listeners;
    }
    
    public void setListeners(List<RuleBuilderEnumOptionsExtensionListener> listeners) {
        this.listeners = listeners;
    }

}
