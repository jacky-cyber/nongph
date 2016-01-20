package cn.globalph.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;

/**
 * @author Jeff Fischer
 */
public class FormatUtil {

    public static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
    public static final String DATE_FORMAT_WITH_TIMEZONE = "yyyy.MM.dd HH:mm:ss Z";

    public static SimpleDateFormat getDateFormat() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter;
    }
    
    /**
     * Used with dates in rules since they are not stored as a Timestamp type (and thus not converted to a specific database
     * timezone on a save). In order to provide accurate information, the timezone must also be preserved in the MVEL rule
     * expression
     * 
     * @return
     */
    public static SimpleDateFormat getTimeZoneFormat() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_WITH_TIMEZONE);
        return formatter;
    }

    /**
     * Use to produce date Strings in the W3C date format
     * 
     * @param date
     * @return
     * @throws DatatypeConfigurationException 
     */
    public static String formatDateUsingW3C(Date date) {
        String w3cDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
        return w3cDate = w3cDate.substring(0, 22) + ":" + w3cDate.substring(22, 24);
    }

}
