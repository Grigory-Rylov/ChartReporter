package com.grishberg.graphreporter.data.repository.values;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.beans.DailyValue;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by grishberg on 21.01.17.
 */
public class CacheActualityCheckerImpl implements CacheActualityChecker {
    private static final int MILLISECONDS = 1000;
    private static final String TIMEZONE_GMT = "GMT";
    private final long timeout;

    public CacheActualityCheckerImpl(final long timeout) {
        this.timeout = timeout;
    }

    @Override
    public boolean isCacheDataValid(final ListResultCloseable<DailyValue> cachedResult) {
        if (cachedResult == null || cachedResult.isEmpty() || cachedResult.isClosed()) {
            return false;
        }
        final long cachedDate = cachedResult.get(cachedResult.size() - 1).getDt();

        return isLastItemDateActual(cachedDate);
    }

    private boolean isLastItemDateActual(long cachedDate) {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_GMT));
        return calendar.getTime().getTime() - cachedDate * MILLISECONDS < timeout;
    }
}
