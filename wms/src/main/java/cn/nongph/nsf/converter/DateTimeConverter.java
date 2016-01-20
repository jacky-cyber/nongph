package com.felix.nsf.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.felix.msxt.common.DateUtil;

public class DateTimeConverter implements Converter {
	private SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_TIME_FORMAT);

	@Override
	public Object getAsObject(String value) {
		if( StringUtils.isEmpty(value) )
			return null;
		else {
			try{
				return sdf.parse( value );
			} catch (Exception e){
				throw new ConverterException( "can't converter " + value +" to Date", e );
			}
		}
	}

	@Override
	public String getAsString(Object value) {
		return sdf.format( (Date)value ); 
	}
}
