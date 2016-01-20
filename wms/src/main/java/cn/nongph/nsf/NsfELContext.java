package com.felix.nsf;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

public class NsfELContext extends ELContext{

	private FunctionMapper functionMapper = new NoopFunctionMapper();
    private VariableMapper variableMapper;
    private ELResolver resolver;

    /**
     * Constructs a new ELContext associated with the given ELResolver.
     * @param resolver the ELResolver to return from {@link #getELResolver()} 
     */
    public NsfELContext(ELResolver resolver) {
        this.resolver = resolver;
    }


    public FunctionMapper getFunctionMapper() {        
        return functionMapper;
    }

    public VariableMapper getVariableMapper() {
        if (variableMapper == null) {
            variableMapper = new VariableMapperImpl();
        }
        return variableMapper;
    }

    public ELResolver getELResolver() {
        return resolver;
    }

    public void setFunctionMapper(FunctionMapper functionMapper) {

        this.functionMapper = functionMapper;

    }


    // ----------------------------------------------------------- Inner Classes

    private static class VariableMapperImpl extends VariableMapper {

        private Map<String, ValueExpression> variables;

        public VariableMapperImpl() {
            variables = new HashMap<String,ValueExpression>();

        }

        public ValueExpression resolveVariable(String s) {
            return variables.get(s);
        }

        public ValueExpression setVariable(String s, ValueExpression valueExpression) {
            return (variables.put(s, valueExpression));
        }
    }


    private static class NoopFunctionMapper extends FunctionMapper {
        public Method resolveFunction(String s, String s1) {
            return null;
        }
    }
}
