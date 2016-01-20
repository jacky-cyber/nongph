package com.felix.nsf.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class ConverterProxy {
	private static Map<Class<? extends Converter>, Converter> converterInstance = new HashMap<Class<? extends Converter>, Converter>();
	
	private static Converter getConverter(Class<? extends Converter> cvtClass){
		Converter cvt = converterInstance.get( cvtClass );
		if( cvt==null ) {
			try {
				cvt = cvtClass.newInstance();
				converterInstance.put( cvtClass, cvt );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cvt;
	}
	
	public static Object string2Object( Field field, String str ){
		Annotation[] as = field.getAnnotations();
		for( Annotation an : as ) {
			ConverterBinding cb = an.annotationType().getAnnotation(ConverterBinding.class);
			if( cb!=null ) {
				Class<? extends Converter> cvtClass = cb.value();
				Converter cvt = getConverter( cvtClass );
				if( cvt!=null )
					return cvt.getAsObject( str );
				else
					return null;
			}
		}
		return str;
	}
}
