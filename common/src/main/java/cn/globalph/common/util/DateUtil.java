package cn.globalph.common.util;

import cn.globalph.common.time.SystemTime;

import java.util.Date;

public class DateUtil {

    public static final long ONE_HOUR_MILLIS = 60 * 60 * 1000;
    public static final long ONE_DAY_MILLIS = ONE_HOUR_MILLIS * 24;
    public static final long ONE_WEEK_MILLIS = ONE_DAY_MILLIS * 7;

    public static final long ONE_HOUR_SECONDS = 60 * 60;
    public static final long ONE_DAY_SECONDS = ONE_HOUR_SECONDS * 24;
    public static final long ONE_WEEK_SECONDS = ONE_DAY_SECONDS * 7;
    public static final long SIX_MONTHS_SECONDS = ONE_DAY_SECONDS * 6 * 30;

    public static boolean isActive(Date startDate, Date endDate, boolean includeTime) {
        Long date = SystemTime.asMillis(includeTime);
        return !(startDate == null || startDate.getTime() > date || (endDate != null && endDate.getTime() < date));
    }

}
