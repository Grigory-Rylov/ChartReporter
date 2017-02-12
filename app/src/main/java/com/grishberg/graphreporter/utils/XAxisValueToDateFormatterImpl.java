package com.grishberg.graphreporter.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by grishberg on 11.02.17.
 */
public class XAxisValueToDateFormatterImpl implements XAxisValueToDateFormatter {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy hh:mm", Locale.US);

    private final List<Long> dates;

    public XAxisValueToDateFormatterImpl(final List<Long> dates) {
        this.dates = dates;
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
    public String getDateAsString(float x, final SimpleDateFormat dateFormat) {
        final int index = (int) x;
        if (dates == null || index >= dates.size() || index < 0) {
            return "";
        }
        final Date date = new Date(dates.get(index));
        return dateFormat.format(date);
    }
}
