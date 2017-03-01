package com.grishberg.graphreporter.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by grishberg on 11.02.17.
 */
public class XAxisValueToDateFormatterImpl implements XAxisValueToDateFormatter {
    public static final long MILLISECOND = 1000;
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US);
    private List<Long> datesList = new ArrayList<>();

    public XAxisValueToDateFormatterImpl(final List<Long> datesList) {
        dateFormat.setTimeZone(GMT);
        this.datesList = datesList;
    }

    @Override
    public String getDateAsString(long date) {
        return dateFormat.format(date * MILLISECOND);
    }

    @Override
    public String getDateAsString(final float x) {
        final long millis = datesList.get((int) x) * MILLISECOND;
        return dateFormat.format(millis);
    }

    @Override
    public String getDateAsString(final float x, final SimpleDateFormat dateFormat) {
        if (x < 0 || datesList.size() <= (int) x || datesList.get((int) x) == 0) {
            return "";
        }
        final long millis = datesList.get((int) x) * MILLISECOND;
        return dateFormat.format(millis);
    }
}
