package com.grishberg.graphreporter.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by grishberg on 11.02.17.
 */
public class XAxisValueToDateFormatterImpl implements XAxisValueToDateFormatter {
    public static final float MILLISECOND = 1000f;
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US);

    public XAxisValueToDateFormatterImpl() {
        dateFormat.setTimeZone(GMT);
    }

    @Override
    public String getDateAsString(final float x) {
        final long millis = TimeUnit.MINUTES.toMillis((long) (x * MILLISECOND));
        return dateFormat.format(millis);
    }

    @Override
    public String getDateAsString(final float x, final SimpleDateFormat dateFormat) {
        final long millis = TimeUnit.MINUTES.toMillis((long) (x * MILLISECOND));
        return dateFormat.format(millis);
    }
}
