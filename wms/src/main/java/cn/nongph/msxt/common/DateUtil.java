package com.felix.msxt.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
	public static Date getTodayStart() {
		Date today = Calendar.getInstance().getTime();
		today.setHours( 0 );
		today.setMinutes( 0 );
		today.setSeconds( 0 );
		return today;
	}
	
	public static Date getTodayEnd() {
		Date today = Calendar.getInstance().getTime();
		today.setHours( 23 );
		today.setMinutes( 59 );
		today.setSeconds( 59 );
		return today;
	}
	
    public static String getStringFromDate(Date date) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        return sdf.format(date);
    }
    
    public static Date getDateFromString(String s){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {
            return sdf.parse(s);
        } catch (ParseException ex) {
           return null;
        }
    }
}
