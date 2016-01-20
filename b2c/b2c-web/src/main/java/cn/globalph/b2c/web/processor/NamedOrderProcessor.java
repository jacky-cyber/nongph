package cn.globalph.b2c.web.processor;

import cn.globalph.b2c.order.domain.NullOrderImpl;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.common.web.dialect.AbstractModelVariableModifierProcessor;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

import javax.annotation.Resource;

/**
 * A Thymeleaf processor that will add the desired named order to the model
 *
 * @author elbertbautista
 */
public class NamedOrderProcessor extends AbstractModelVariableModifierProcessor {
    
    @Resource(name = "blOrderService")
    protected OrderService orderService;

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     *
     * NOTE: thymeleaf normalizes the attribute names by converting all to lower-case
     * we will use the underscore instead of camel case to avoid confusion
     *
     */
    public NamedOrderProcessor() {
        super("named_order");
    }

    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {
        Customer customer = CustomerState.getCustomer();

        String orderVar = element.getAttributeValue("orderVar");
        String orderName = element.getAttributeValue("orderName");

        Order order = orderService.findNamedOrderForCustomer(orderName, customer);
        if (order != null) {
            addToModel(arguments, orderVar, order);
        } else {
            addToModel(arguments, orderVar, new NullOrderImpl());
        }
    }
}
