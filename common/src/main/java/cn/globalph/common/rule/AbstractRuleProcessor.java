package cn.globalph.common.rule;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mvel2.CompileException;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public abstract class AbstractRuleProcessor<T> implements RuleProcessor<T> {
    
    protected final Log LOG = LogFactory.getLog(this.getClass());

    @SuppressWarnings("unchecked")
    protected Map<String, Serializable> expressionCache = Collections.synchronizedMap(new LRUMap(1000));
    protected ParserContext parserContext;
    protected Map<String, String> contextClassNames = new HashMap<String, String> ();

    /**
     * Having a parser context that imports the classes speeds MVEL by up to 60%.
     */
    protected ParserContext getParserContext() {
        if (parserContext == null) {
            parserContext = new ParserContext();
            parserContext.addImport("MVEL", MVEL.class);
            parserContext.addImport("MvelHelper", MvelHelper.class);
        }
        return parserContext;
    }

    /**
     * Helpful method for processing a boolean MVEL expression and associated arguments.
     *
     * Caches the expression in an LRUCache.
     * @param expression
     * @param vars
     * @return the result of the expression
     */
    protected Boolean executeExpression(String expression, Map<String, Object> vars) {
        Serializable exp = (Serializable) expressionCache.get(expression);
        vars.put("MVEL", MVEL.class);

        if (exp == null) {
            try {
                exp = MVEL.compileExpression(expression, getParserContext());
            } catch (CompileException ce) {
                LOG.warn("Compile exception processing phrase: " + expression,ce);
                return Boolean.FALSE;
            }
            expressionCache.put(expression, exp);
        }

        try {
            return (Boolean) MVEL.executeExpression(exp, vars);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return false;
    }

    /**
     * List of class names to add to the MVEL ParserContext.
     * @see {@link ParserContext}
     */
    public Map<String, String> getContextClassNames() {
        return contextClassNames;
    }

    /**
     * List of class names to add to the MVEL ParserContext.
     * @see {@link ParserContext}
     */
    public void setContextClassNames(Map<String, String> contextClassNames) {
        this.contextClassNames = contextClassNames;
    }

}
