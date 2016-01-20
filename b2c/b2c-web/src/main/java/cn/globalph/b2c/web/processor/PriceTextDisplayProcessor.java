package cn.globalph.b2c.web.processor;

import cn.globalph.common.currency.util.BroadleafCurrencyUtils;
import cn.globalph.common.money.Money;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractTextChildModifierAttrProcessor;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

/**
 * A Thymeleaf processor that renders a Money object according to the currently set locale options.
 * For example, when rendering "6.99" in a US locale, the output text would be "$6.99".
 * When viewing in France for example, you might see "6,99 (US)$". Alternatively, if currency conversion
 * was enabled, you may see "5,59 (euro-symbol)"
 * 
 * @author apazzolini
 */
public class PriceTextDisplayProcessor extends AbstractTextChildModifierAttrProcessor {

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public PriceTextDisplayProcessor() {
        super("price");
    }
    
    @Override
    public int getPrecedence() {
        return 1500;
    }

    @Override
    protected String getText(Arguments arguments, Element element, String attributeName) {
        
        Money price = null;

        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValue(attributeName));
        Object result = expression.execute(arguments.getConfiguration(), arguments);
        if (result instanceof Money) {
            price = (Money) result;
        } else if (result instanceof Number) {
            price = new Money(((Number)result).doubleValue());
           }


        if (price == null) {
            return "Not Available";
           }

        return BroadleafCurrencyUtils.getNumberFormatFromCache().format(price.getAmount());
    }
}
