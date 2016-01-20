package cn.globalph.common.web.processor;

import cn.globalph.common.util.BLCSystemProperty;
import cn.globalph.common.web.dialect.AbstractModelVariableModifierProcessor;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;


/**
 * A Thymeleaf processor that will lookup the value of a configuration variable.
 * Takes in a configuration variable attribute name and returns the value. 
 * 
 * @author bpolster
 */
public class ConfigVariableProcessor extends AbstractModelVariableModifierProcessor {

    public ConfigVariableProcessor() {
        super("config");
    }
    
    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {
        String resultVar = element.getAttributeValue("resultVar");
        if (resultVar == null) {
            resultVar = "value";
        }
        
        String attributeName = element.getAttributeValue("name");
        String attributeValue = BLCSystemProperty.resolveSystemProperty(attributeName);
        
        addToModel(arguments, resultVar, attributeValue);
    }
}
