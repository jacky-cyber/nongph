package cn.globalph.common.web.expression;

import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.spring3.expression.SpelVariableExpressionEvaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Provides a skeleton to register multiple {@link GlobalphVariableExpression} implementors.
 * @author felix.wu
 */
public class GlobalphVariableExpressionEvaluator extends SpelVariableExpressionEvaluator {
    
    @Resource(name = "blVariableExpressions")
    protected List<GlobalphVariableExpression> expressions = new ArrayList<GlobalphVariableExpression>();
    
    @Override
    protected Map<String,Object> computeAdditionalExpressionObjects(final IProcessingContext processingContext) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        for (GlobalphVariableExpression expression : expressions) {
            if (!(expression instanceof NullVariableExpression)) {
                map.put(expression.getName(), expression);
            }
        }
        
        return map;
    }

}
