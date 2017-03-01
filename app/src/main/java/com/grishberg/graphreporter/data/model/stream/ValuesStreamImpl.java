package com.grishberg.graphreporter.data.model.stream;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.model.values.DualDateValue;

import java.util.List;

/**
 * Created by grishberg on 27.02.17.
 */

public class ValuesStreamImpl implements ValuesStream<DualDateValue> {
    private final List<DailyValue> elements;
    private final int periodSizeInMilliseconds;
    private int previouseElementIndex;
    private long timePeriodUpperBound = 0;

    public ValuesStreamImpl(@NonNull final List<DailyValue> elements,
                            final int periodSizeInMilliseconds) {
        this.elements = elements;
        this.periodSizeInMilliseconds = periodSizeInMilliseconds;
        timePeriodUpperBound = !elements.isEmpty() ? elements.get(0).getDt() : 0;
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

        timePeriodUpperBound += periodSizeInMilliseconds;
        int iterationsCount = 0;
        while (hasMoreValuesInCollection()) {
            final DailyValue currentValue = elements.get(previouseElementIndex);
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
            return new DualDateValue(startDt - periodSizeInMilliseconds / 2, endDt + periodSizeInMilliseconds / 2,
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
}
