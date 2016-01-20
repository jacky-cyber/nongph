package cn.globalph.passport.web.core.expression;

import cn.globalph.common.web.expression.GlobalphVariableExpression;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;


/**
 * This Thymeleaf variable expression class serves to expose elements from the BroadleafRequestContext
 * 
 * @author felix.wu
 */
public class CustomerVariableExpression implements GlobalphVariableExpression {
    
    @Override
    public String getName() {
        return "customer";
    }
    
    public Customer getCurrent() {
        return CustomerState.getCustomer();
    }
    
}
