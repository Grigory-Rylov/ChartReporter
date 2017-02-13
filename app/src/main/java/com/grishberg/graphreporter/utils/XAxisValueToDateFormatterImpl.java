package com.grishberg.graphreporter.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by grishberg on 11.02.17.
 */
public class XAxisValueToDateFormatterImpl implements XAxisValueToDateFormatter {
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US);

    private final List<Long> dates;

    public XAxisValueToDateFormatterImpl(final List<Long> dates) {
        this.dates = dates;
        dateFormat.setTimeZone(GMT);
    }

    @Override
    public String getDateAsString(final float x) {
        final int index = (int) x;
        if (dates == null || index >= dates.size() || index < 0) {
            return "";
        }
        final Date date = new Date(dates.get(index));
        return dateFormat.format(date);
    }

    @Override
    public String getDateAsString(final float x, final SimpleDateFormat dateFormat) {
        final int index = (int) x;
        if (dates == null || index >= dates.size() || index < 0) {
            return "";
        }
        final Date date = new Date(dates.get(index));
        return dateFormat.format(date);
    }
}
