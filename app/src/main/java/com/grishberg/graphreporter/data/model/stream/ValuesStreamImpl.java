package com.grishberg.graphreporter.data.model.stream;

import android.support.annotation.NonNull;

import com.grishberg.graphreporter.data.model.DailyValue;

import java.util.List;

/**
 * Created by grishberg on 27.02.17.
 */

public class ValuesStreamImpl implements ValuesStream<DailyValue> {
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

    @Override
    public DailyValue getNextElement() {
        final double open = elements.get(previouseElementIndex).getPriceOpen();
        double hi = 0;
        double lo = Float.MAX_VALUE;
        double close = 0;
        final long startDt = elements.get(previouseElementIndex).getDt();
        long endDt = startDt;
        long currentDt = 0;
        double volume = 0;

        final long oldPeriodBound = timePeriodUpperBound;
        timePeriodUpperBound += periodSizeInMilliseconds;
        while (true) {
            final DailyValue currentValue = elements.get(previouseElementIndex);
            currentDt = currentValue.getDt();
            if (currentDt >= timePeriodUpperBound) {
                break;
            }
            previouseElementIndex++;
            endDt = currentDt;
            volume += currentValue.getVolume();

            hi = Math.max(currentValue.getPriceHigh(), hi);
            lo = Math.min(currentValue.getPriceLow(), lo);
            close = currentValue.getPriceClose();
        }
        return new DailyValue(0, 0, (oldPeriodBound + endDt) / 2, open, close, hi, lo, volume);
    }
}
