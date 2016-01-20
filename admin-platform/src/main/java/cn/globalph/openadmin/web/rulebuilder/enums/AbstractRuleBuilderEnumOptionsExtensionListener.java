package cn.globalph.openadmin.web.rulebuilder.enums;

import org.apache.commons.lang3.reflect.FieldUtils;

import cn.globalph.common.EnumerationType;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Abstract extension listener for rule builder enum options that handles the boilerplate code required for setting up
 * the response to the client. This class provides two abstract methods that must be implemented, {@link #getVariableName()}
 * and {@link #getEnumClass()}. Generates a String with the following pattern:
 * 
 * var variableName = [
 *     { label : "enumFriendlyType", name : "enumType" },
 *     { label : "enumFriendlyType2", name : "enumType2" },
 *     ...
 *     { label : "enumFriendlyTypeN", name : "enumTypeN" }
 * ];
 * 
 * @author Andre Azzolini (apazzolini)
 */
public abstract class AbstractRuleBuilderEnumOptionsExtensionListener implements RuleBuilderEnumOptionsExtensionListener {
    
    public String getOptionValues() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Class<? extends EnumerationType>> entry : getValuesToGenerate().entrySet()) {
            try {
                sb.append("var ").append(entry.getKey()).append(" = [");
                
                int i = 0;
                Map<String, ? extends EnumerationType> types = getTypes(entry.getValue());
                for (Entry<String, ? extends EnumerationType> entry2 : types.entrySet()) {
                    sb.append("{ label : \"" + entry2.getValue().getFriendlyType() + "\"");
                    sb.append(", ");
                    sb.append(" name : \"" + entry2.getValue().getType() + "\" }");
                    if (++i < types.size()) {
                        sb.append(", ");
                    }
                }
                sb.append("]; \r\n");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sb.toString();
    }
    
    @SuppressWarnings("unchecked")
    protected Map<String, ? extends EnumerationType> getTypes(Class<? extends EnumerationType> clazz) {
        try {
            return (Map<String, ? extends EnumerationType>) FieldUtils.readStaticField(clazz, "TYPES", true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @return a map representing the various values that this extension listener should generate
     */
    protected abstract Map<String, Class<? extends EnumerationType>> getValuesToGenerate();

}
