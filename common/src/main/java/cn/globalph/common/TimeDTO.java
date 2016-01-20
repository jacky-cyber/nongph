package cn.globalph.common;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.time.DayOfMonthType;
import cn.globalph.common.time.DayOfWeekType;
import cn.globalph.common.time.HourOfDayType;
import cn.globalph.common.time.MinuteType;
import cn.globalph.common.time.MonthType;
import cn.globalph.common.time.SystemTime;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by bpolster.
 */
public class TimeDTO {

    @AdminPresentation(excluded = true)
    private Calendar cal;

    @AdminPresentation(friendlyName = "TimeDTO_Hour_Of_Day", fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, broadleafEnumeration = "cn.globalph.common.time.HourOfDayType")
    private Integer hour;

    @AdminPresentation(friendlyName = "TimeDTO_Day_Of_Week", fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, broadleafEnumeration = "cn.globalph.common.time.DayOfWeekType")
    private Integer dayOfWeek;

    @AdminPresentation(friendlyName = "TimeDTO_Month", fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, broadleafEnumeration = "cn.globalph.common.time.MonthType")
    private Integer month;

    @AdminPresentation(friendlyName = "TimeDTO_Day_Of_Month", fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, broadleafEnumeration = "cn.globalph.common.time.DayOfMonthType")
    private Integer dayOfMonth;

    @AdminPresentation(friendlyName = "TimeDTO_Minute", fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, broadleafEnumeration = "cn.globalph.common.time.MinuteType")
    private Integer minute;

    @AdminPresentation(friendlyName = "TimeDTO_Date")
    private Date date;

    public TimeDTO() {
        cal = SystemTime.asCalendar();
    }

    public TimeDTO(Calendar cal) {
        this.cal = cal;
    }


    /**
     * @return  int representing the hour of day as 0 - 23
     */
    public HourOfDayType getHour() {
        if (hour == null) {
            hour = cal.get(Calendar.HOUR_OF_DAY);
        }
        return HourOfDayType.getInstance(hour.toString());
    }

    /**
     * @return int representing the day of week using Calendar.DAY_OF_WEEK values.
     * 1 = Sunday, 7 = Saturday
     */
    public DayOfWeekType getDayOfWeek() {
        if (dayOfWeek == null) {
            dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        }
        return DayOfWeekType.getInstance(dayOfWeek.toString());
    }

    /**
     * @return the current day of the month (1-31).
     */
    public DayOfMonthType getDayOfMonth() {
        if (dayOfMonth == null) {
            dayOfMonth =  cal.get(Calendar.DAY_OF_MONTH);
        }
        return DayOfMonthType.getInstance(dayOfMonth.toString());
    }

    /**
     * @return int representing the current month (1-12)
     */
    public MonthType getMonth() {
        if (month == null) {
            month = cal.get(Calendar.MONTH);
        }
        return MonthType.getInstance(month.toString());
    }

    public MinuteType getMinute() {
        if (minute == null) {
            minute = cal.get(Calendar.MINUTE);
        }
        return MinuteType.getInstance(minute.toString());
    }

    public Date getDate() {
        if (date == null) {
            date = cal.getTime();
        }
        return date;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
    }

    public void setHour(HourOfDayType hour) {
        this.hour = Integer.valueOf(hour.getType());
        ;
    }

    public void setDayOfWeek(DayOfWeekType dayOfWeek) {
        this.dayOfWeek = Integer.valueOf(dayOfWeek.getType());
    }

    public void setMonth(MonthType month) {
        this.month = Integer.valueOf(month.getType());
    }

    public void setDayOfMonth(DayOfMonthType dayOfMonth) {
        this.dayOfMonth = Integer.valueOf(dayOfMonth.getType());
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMinute(MinuteType minute) {
        this.minute = Integer.valueOf(minute.getType());
        ;
    }
}
