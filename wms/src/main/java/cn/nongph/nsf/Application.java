package com.felix.nsf;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * 
 * @author Felix
 *
 */
public class Application {
	private ELResolver elResolver = null;
	
	private ExpressionFactory expressionFactory = null;
	@Inject
	private BeanManager beanManager;
	
	private Application(){};
	
	public ELResolver getELResolver() {
		if( elResolver==null ) {
			CompositeELResolver composite = new CompositeELResolver();
			composite.add( new MapELResolver() );
		    composite.add( new ListELResolver() );
		    composite.add( new ArrayELResolver() );
		    composite.add( new BeanELResolver() );
		    composite.add( beanManager().getELResolver() );
		    
		    elResolver = composite;
		}
		
		return elResolver;
	}
	
	public ExpressionFactory getExpressionFactory(){
		if( expressionFactory==null ) {
			expressionFactory = beanManager().wrapExpressionFactory( ExpressionFactory.newInstance() );
		}
		return expressionFactory;
	}
	
	private static Application instance = new Application();
	
	public static Application getInstance(){
		return instance;
	}
	

    private BeanManager beanManager() {
        if (beanManager == null) {
            synchronized (this) {
                if (beanManager == null) {
                    try {
                        beanManager = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
                    } catch (NamingException e) {
                        return null;
                    }
                }
            }
        }
        return beanManager;
    }
}
