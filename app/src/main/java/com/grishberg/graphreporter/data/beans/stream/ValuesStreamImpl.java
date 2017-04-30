package com.grishberg.graphreporter.data.beans.stream;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.grishberg.graphreporter.data.beans.DailyValue;
import com.grishberg.graphreporter.data.beans.values.DualDateValue;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by grishberg on 27.02.17.
 */

public class ValuesStreamImpl implements ValuesStream<DualDateValue> {
    public static final long MILLISECONDS = 1000L;
    private final List<DailyValue> elements;
    private final int periodSizeInSeconds;
    private int previouseElementIndex;
    private long timePeriodUpperBound = 0;

    public ValuesStreamImpl(@NonNull final List<DailyValue> elements,
                            final int periodSizeInMilliseconds) {
        this.elements = elements;
        this.periodSizeInSeconds = periodSizeInMilliseconds;
        timePeriodUpperBound = getInitialDate(elements);
    }

    private long getInitialDate(@NonNull final List<DailyValue> elements) {
        if (!elements.isEmpty()) {
            final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calendar.setTimeInMillis(elements.get(0).getDt());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTimeInMillis() / 1000;
        }
        return 0;
    }

    @Override
    public int getSize() {
        return elements.size();
    }

    @Nullable
    @Override
    public DualDateValue getNextElement() throws NoMoreItemException {
        if (!hasMoreValuesInCollection()) {
            return null;
        }
        final double open = elements.get(previouseElementIndex).getPriceOpen();
        double hi = 0;
        double lo = Float.MAX_VALUE;
        double close = 0;
        final long startDt = elements.get(previouseElementIndex).getDt();
        long endDt = startDt;
        long currentDt;
        double volume = 0;

        timePeriodUpperBound += periodSizeInSeconds;
        int iterationsCount = 0;
        DailyValue currentValue;
        while (hasMoreValuesInCollection()) {
            currentValue = elements.get(previouseElementIndex);
            currentDt = currentValue.getDt();
            if (currentDt >= timePeriodUpperBound) {
                break;
            }
            iterationsCount++;
            previouseElementIndex++;
            endDt = currentDt;
            volume += currentValue.getVolume();

            hi = Math.max(currentValue.getPriceHigh(), hi);
            lo = Math.min(currentValue.getPriceLow(), lo);
            close = currentValue.getPriceClose();
        }
        if (iterationsCount > 0) {
            return new DualDateValue(startDt - periodSizeInSeconds / 2, endDt + periodSizeInSeconds / 2,
                    open, close, hi, lo, volume);
        }
        return null;
    }

    private boolean hasMoreValuesInCollection() throws NoMoreItemException {
        if (previouseElementIndex >= elements.size()) {
            throw new NoMoreItemException();
        }
        return true;
    }

    @Override
    public DualDateValue getPrevElement() throws NoMoreItemException {
        if (!hasPrevValuesInCollection()) {
            return null;
        }
        final double open = elements.get(previouseElementIndex).getPriceOpen();
        double hi = 0;
        double lo = Float.MAX_VALUE;
        double close = 0;
        final long startDt = elements.get(previouseElementIndex).getDt();
        long endDt = startDt;
        long currentDt;
        double volume = 0;

        timePeriodUpperBound -= periodSizeInSeconds;
        int iterationsCount = 0;
        DailyValue currentValue;
        while (hasPrevValuesInCollection()) {
            currentValue = elements.get(previouseElementIndex);
            currentDt = currentValue.getDt();
            if (currentDt <= timePeriodUpperBound) {
                break;
            }
            iterationsCount++;
            previouseElementIndex--;
            endDt = currentDt;
            volume += currentValue.getVolume();

            hi = Math.max(currentValue.getPriceHigh(), hi);
            lo = Math.min(currentValue.getPriceLow(), lo);
            close = currentValue.getPriceClose();
        }
        if (iterationsCount > 0) {
            return new DualDateValue(startDt - periodSizeInSeconds / 2, endDt + periodSizeInSeconds / 2,
                    open, close, hi, lo, volume);
        }
        return null;
    }

    private boolean hasPrevValuesInCollection() throws NoMoreItemException {
        if (previouseElementIndex > 0) {
            throw new NoMoreItemException();
        }
        return true;
    }
}
