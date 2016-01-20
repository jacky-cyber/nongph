package cn.globalph.common.web.expression;

/**
 * A null implementation of {@link GlobalphVariableExpression} 
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class NullVariableExpression implements GlobalphVariableExpression {

    @Override
    public String getName() {
        return null;
    }
    
}
