package com.grishberg.graphreporter.mvp.presenter;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.model.ChartResponseContainer;
import com.grishberg.graphreporter.data.model.DailyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grishberg on 19.01.17.
 */
public class ChartsHelper {
    private ChartsHelper() {
    }

    public static ChartResponseContainer<Entry> getLineData(final ChartPeriod period,
                                                            final List<DailyValue> dailyValues) {
        final List<Long> dates = new ArrayList<>();
        final List<Entry> entries = new ArrayList<>();
        final int periodPartionCount = period.getPartion();
        long endPeriod = 0;
        long startPeriod = 0;
        int pos = 0;
        final int size = dailyValues.size();
        int periodCount = 0;
        while (pos < size) {
            float start = 0;
            float end = 0;
            int i;
            for (i = 0; i < periodPartionCount && pos < size; i++, pos++) {
                final DailyValue element = dailyValues.get(pos);
                if (i == 0) {
                    start = element.getPriceStart();
                    startPeriod = element.getDt() * 1000L;
                }
                if ((i == periodPartionCount - 1) || (pos == size - 1)) {
                    end = element.getPriceEnd();
                    endPeriod = element.getDt() * 1000L;
                }
            }
            // start
            dates.add(startPeriod);
            entries.add(new Entry(periodCount++, start));

            //end
            dates.add(endPeriod);
            dates.add(endPeriod);
            periodCount += 1;
            entries.add(new Entry(periodCount++, end));
        }

        return new ChartResponseContainer<>(entries, dates, period);
    }

    @NonNull
    public static ChartResponseContainer<CandleEntry> getCandleDataForPeriod(final ChartPeriod period,
                                                                             final List<DailyValue> dailyValues) {
        final List<Long> dates = new ArrayList<>();
        final List<CandleEntry> entries = new ArrayList<>();
        final int periodPartionCount = period.getPartion();
        long currentDt = 0;
        int pos = 0;
        final int size = dailyValues.size();
        int periodCount = 0;
        while (pos < size) {
            float hi = 0;
            float lo = Float.MAX_VALUE;
            float start = 0;
            float end = 0;
            int i;
            for (i = 0; i < periodPartionCount && pos < size; i++, pos++) {
                final DailyValue element = dailyValues.get(pos);
                hi = Math.max(element.getPriceHi(), hi);
                lo = Math.min(element.getPriceLo(), lo);
                if (i == 0) {
                    start = element.getPriceStart();
                }
                if ((i == periodPartionCount - 1) || (pos == size - 1)) {
                    end = element.getPriceEnd();
                }
                currentDt = element.getDt() * 1000L;
            }
            dates.add(currentDt);
            dates.add(currentDt);
            dates.add(currentDt);
            periodCount += 3;
            entries.add(new CandleEntry(periodCount - 2, hi, lo, start, end));
        }

        return new ChartResponseContainer<>(entries, dates, period);
    }
}
