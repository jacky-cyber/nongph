package cn.globalph.openadmin.web.processor;

import cn.globalph.openadmin.web.rulebuilder.dto.FieldWrapper;
import cn.globalph.openadmin.web.rulebuilder.service.RuleBuilderFieldService;
import cn.globalph.openadmin.web.rulebuilder.service.RuleBuilderFieldServiceFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.element.AbstractLocalVariableDefinitionElementProcessor;
import org.thymeleaf.spring3.context.SpringWebContext;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Component("blAdminFieldBuilderProcessor")
public class AdminFieldBuilderProcessor extends AbstractLocalVariableDefinitionElementProcessor {

    private RuleBuilderFieldServiceFactory ruleBuilderFieldServiceFactory;

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public AdminFieldBuilderProcessor() {
        super("admin_field_builder");
    }

    @Override
    public int getPrecedence() {
        return 100;
    }

    protected void initServices(Arguments arguments) {
        final ApplicationContext applicationContext = ((SpringWebContext) arguments.getContext()).getApplicationContext();

        if (ruleBuilderFieldServiceFactory == null) {
            ruleBuilderFieldServiceFactory = (RuleBuilderFieldServiceFactory)
                    applicationContext.getBean("blRuleBuilderFieldServiceFactory");
        }

    }

    @Override
    protected Map<String, Object> getNewLocalVariables(Arguments arguments, Element element) {
        initServices(arguments);
        FieldWrapper fieldWrapper = new FieldWrapper();
        
        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValue("fieldBuilder"));
        String fieldBuilder = (String) expression.execute(arguments.getConfiguration(), arguments);

        if (fieldBuilder != null) {
            RuleBuilderFieldService ruleBuilderFieldService = ruleBuilderFieldServiceFactory.createInstance(fieldBuilder);
            if (ruleBuilderFieldService != null) {
                fieldWrapper = ruleBuilderFieldService.buildFields();
            }
        }
        
        Map<String, Object> newVars = new HashMap<String, Object>();
        newVars.put("fieldWrapper", fieldWrapper);
        return newVars;
    }

    @Override
    protected boolean removeHostElement(Arguments arguments, Element element) {
        return false;
    }
}
