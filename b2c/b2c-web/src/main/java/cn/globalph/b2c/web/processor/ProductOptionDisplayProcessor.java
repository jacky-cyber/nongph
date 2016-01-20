package cn.globalph.b2c.web.processor;

import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.product.domain.ProductOptionValue;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.element.AbstractLocalVariableDefinitionElementProcessor;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author felix.wu
 */
public class ProductOptionDisplayProcessor extends AbstractLocalVariableDefinitionElementProcessor {

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public ProductOptionDisplayProcessor() {
        super("product_option_display");
    }

    @Override
    public int getPrecedence() {
        return 100;
    }

    protected void initServices(Arguments arguments) {

    }

    @Override
    protected Map<String, Object> getNewLocalVariables(Arguments arguments, Element element) {
        initServices(arguments);
        HashMap<String, String> productOptionDisplayValues = new HashMap<String, String>();
        Map<String, Object> newVars = new HashMap<String, Object>();
        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValue("orderItem"));
        Object item = expression.execute(arguments.getConfiguration(), arguments);
        if (item instanceof OrderItem) {
            OrderItem orderItem = (OrderItem) item;
            for (ProductOptionValue pov : orderItem.getSku().getProductOptionValues() ) {
                 productOptionDisplayValues.put( pov.getProductOption().getLabel(), pov.getAttributeValue() );
                }
          }
        newVars.put("productOptionDisplayValues", productOptionDisplayValues);

        return newVars;
    }

    @Override
    protected boolean removeHostElement(Arguments arguments, Element element) {
        return false;
    }
}
