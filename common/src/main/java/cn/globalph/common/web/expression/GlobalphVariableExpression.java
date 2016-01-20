package cn.globalph.common.web.expression;

/**
 * Classes that implement this interface will be exposed to the Thymeleaf expression evaluation context.
 * If an implementing class defines its name as "theme" and has a method called attr(String name), that method
 * could then be invoked by ${#theme.attr('someName')}.
 * 
 * @author felix.wu
 */
public interface GlobalphVariableExpression {
    
    public String getName();
    
}
