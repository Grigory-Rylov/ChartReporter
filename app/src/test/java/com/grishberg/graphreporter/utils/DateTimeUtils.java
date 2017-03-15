package com.grishberg.graphreporter.utils;

import java.util.Calendar;

/**
 * Created by grishberg on 27.02.17.
 */

public class DateTimeUtils {
    public static long getDate(final int dd, final int mm, final int yyyy) {
        final Calendar c = Calendar.getInstance();
        c.set(yyyy, mm, dd, 0, 0);
        return c.getTimeInMillis() / 1000;
    }

    public static long getDate(final int dd,
                               final int mm,
                               final int yyyy,
                               final int hours, final int minutes) {
        final Calendar c = Calendar.getInstance();
        c.set(yyyy, mm, dd, hours, minutes);
        return c.getTimeInMillis() / 1000;
    }
}
