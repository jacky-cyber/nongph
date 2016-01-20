package com.felix.nsf;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.collection.internal.AbstractPersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.felix.msxt.common.DateUtil;

/**
 * @author Felix
 */
public class JSONPackager {
	private List<Object> currentPath; 
	
	private JSONPackager(){
		currentPath = new ArrayList<Object>(16);
	}
	
	public static JSONPackager getInstance(){
		return new JSONPackager();
	}
	
	public String package2JSONString( Object o ) {
		
		Object result = package2JSON( o );
		if( result!=null )
			return result.toString();
		else
			return null; 
	}
	
	private Object package2JSON( Object o ) {
		if( o == null ) 
			return null;
		
		if( checkDeadLoop( o ) )
			return "Skipped";
		else
			currentPath.add( o );
		
		Object result;
		if( o instanceof String ) {
			result = o;
		} else if( o instanceof Object[] ) {
			Object[] oa = (Object[])o;
			result = packageArray2JSON( oa );
		} else if ( o instanceof Collection<?> ) {
			Collection<?> cc = (Collection<?>) o;
			result = packageCollection2JSON( cc );
		} else if ( o instanceof Map<?,?> ){
			Map<?,?> map = (Map<?,?>)o;
			result = packageMap2JSON( map );
		} else {
			result = packageCommonObject2JSON( o );
		}
		currentPath.remove( currentPath.size()-1 );
		return result;
	}
	
	private Object packageArray2JSON( Object[] oa ) {
		JSONArray ja = new JSONArray();
		for( Object oo : oa )
			ja.put( processValue( oo ) );
		return ja;
	}
	
	private Object packageCollection2JSON( Collection<?> c ) {
		JSONArray ja = new JSONArray();
		for( Object oo : c )
			ja.put( processValue( oo ) );
		return ja;
	}
	
	private Object packageMap2JSON( Map<?,?> map ) {
		JSONObject jo = new JSONObject();
		for( Map.Entry<?, ?> entry : map.entrySet() ) {
			Object value = processValue( entry.getValue() );
			try {
				jo.put( entry.getKey().toString(), value );
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		return jo;
	}
	
	private Object packageCommonObject2JSON( Object o ) {
		JSONObject jo = new JSONObject();
		Class<?> c = o.getClass();
		Method[] methods = c.getMethods();
		for( Method m : methods ) {
			Need2JSON ann = m.getAnnotation( Need2JSON.class );
			if( ann == null ) 
				continue;
			
			String name = ann.value();
			Object value = invokeMethod(m, o);
			if( name.length()==0 )
				name = m.getName();
			
			try {
				jo.put( name, value );
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		
		while( c !=null ) {
			Field[] fileds = c.getDeclaredFields();
			for( Field f : fileds ) {
				Need2JSON ann = f.getAnnotation( Need2JSON.class );
				if( ann == null ) 
					continue;
				
				
				try {
					f.setAccessible( true );
					
					Object orgFV = f.get(o);
				    if( orgFV instanceof HibernateProxy){
			            LazyInitializer li = ((HibernateProxy)orgFV).getHibernateLazyInitializer();
			            if( li.isUninitialized()){
			            	continue;
			            }
			        }
				    
				    if( orgFV instanceof AbstractPersistentCollection ) {
				    	AbstractPersistentCollection apc = (AbstractPersistentCollection)orgFV;
				    	if( !apc.wasInitialized() ) {
				    		continue;
				    	}
			        }
				} catch ( Exception e1 ) {
					e1.printStackTrace();
					System.out.println( f.getName() );
				}
				    
				String fName = f.getName();
				String name = ann.value();
				Object value;
				if( name.length()==0 )
					name = fName;
				
				Method m = null;
				try {
					m = c.getMethod( "get" + fName.substring(0, 1).toUpperCase() + fName.substring(1) );
				} catch( NoSuchMethodException e ) {
					try {
						m = c.getMethod( "is" + fName.substring(0, 1).toUpperCase() + fName.substring(1) );
					} catch( NoSuchMethodException ee ) {}
				}	
				
				if( m == null) { 
					value = "Cann't get invoke method";
				} else {
					value = invokeMethod( m, o );
				}
				
				try {
					jo.put( name, value );
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
			c = c.getSuperclass();
		}
		return jo;
	}
	
	private Object invokeMethod( Method m, Object target) {
		Object value;
		try{
			Object valueO = m.invoke( target );
			value = processValue( valueO );
		} catch( Exception e ) { 
			value = "Invoke error";
		}
		
		return value;
	}
	
	private Object processValue( Object valueO ) {
		Object value;
		if( valueO==null )
			value = "";
		else if( valueO.getClass().isPrimitive()
			|| isBoxType( valueO )
			|| valueO instanceof String )
			value = valueO;
		else if( valueO instanceof Date )
			value = DateUtil.getStringFromDate( (Date)valueO );
		else if( valueO instanceof Enum )
			value = ( (Enum<?>)valueO ).name(); 
		else
			value = package2JSON( valueO );
		
		return value;
	}
	
	private boolean isBoxType( Object valueO ) {
	    if( valueO instanceof java.lang.Boolean
	    	|| valueO instanceof java.lang.Character
	    	|| valueO instanceof java.lang.Byte
	    	|| valueO instanceof java.lang.Short
	    	|| valueO instanceof java.lang.Integer
	    	|| valueO instanceof java.lang.Long
	    	|| valueO instanceof java.lang.Float
	    	|| valueO instanceof java.lang.Double
	    	|| valueO instanceof java.math.BigDecimal)
	    	return true;
	    else
	    	return false;
	}
	
	private boolean checkDeadLoop( Object o ){
		for( Object node : currentPath ) 
			if( o==node )
				return true;
		return false;
	}
}
