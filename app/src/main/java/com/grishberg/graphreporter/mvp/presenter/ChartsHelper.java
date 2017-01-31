package com.grishberg.graphreporter.mvp.presenter;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.model.ChartResponseContainer;
import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.model.FormulaContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grishberg on 19.01.17.
 */
public class ChartsHelper {

    private static final int CANDLE_PERIOD_OFFSET = 1;
    private static final int CANDLE_PERIOD_INCREMENT = 2;

    private ChartsHelper() {
    }

    public static ChartResponseContainer<Entry> getLineData(final ChartPeriod period,
                                                            final List<DailyValue> dailyValues,
                                                            final boolean isDualChartMode) {
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
            if (isDualChartMode) {
                //end
                dates.add(endPeriod);
                periodCount += 1;
            }
            dates.add(endPeriod);
            entries.add(new Entry(periodCount, end));
        }

        return new ChartResponseContainer<>(entries, dates, period);
    }

    @NonNull
    public static ChartResponseContainer<CandleEntry> getCandleDataForPeriod(final ChartPeriod period,
                                                                             final List<DailyValue> dailyValues,
                                                                             final boolean isDualChartMode) {
        final List<Long> dates = new ArrayList<>();
        final List<CandleEntry> entries = new ArrayList<>();
        final int periodPartionCount = period.getPartion();
        final int candlePeriodOffset = isDualChartMode ? CANDLE_PERIOD_OFFSET : 0;
        final int candlePeriodIncrement = isDualChartMode ? CANDLE_PERIOD_INCREMENT : 1;
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
            if (isDualChartMode) {
                dates.add(currentDt);
                dates.add(currentDt);
            }
            periodCount += candlePeriodIncrement;
            entries.add(new CandleEntry(periodCount - candlePeriodOffset, hi, lo, start, end));
        }

        return new ChartResponseContainer<>(entries, dates, period);
    }

    /**
     * Формирование данных для формулы
     *
     * @param isDualChartMode
     * @param period
     * @param dailyValues
     * @param isDualChartMode
     * @return
     */
    @NonNull
    public static ChartResponseContainer<Entry> getFormulaDataForPeriod(final ChartPeriod period,
                                                                        final List<DailyValue> dailyValues,
                                                                        final FormulaContainer formulaContainer,
                                                                        final boolean isDualChartMode) {
        final List<Long> dates = new ArrayList<>();
        final List<Entry> entries = new ArrayList<>();
        final int periodPartionCount = period.getPartion();
        //TODO: точки выбирать исходя из условия
        final double maxValue = dailyValues.get(0).getPriceEnd() *
                (1D + formulaContainer.getFormulaValue() / 100D);
        final double minValue = dailyValues.get(0).getPriceEnd() -
                (dailyValues.get(0).getPriceEnd() * formulaContainer.getFormulaValue() / 100D);
        long endPeriod = 0;
        long startPeriod = 0;
        int pos = 0;
        final int size = dailyValues.size();
        int periodCount = 0;
        while (pos < size) {
            float start = 0;
            float end = 0;
            int i;
            // пропуск какого то количества точек, которые входят в период
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
            if (formulaContainer.isGreater()) {
                if (end > maxValue) {
                    // ТР
                    entries.add(new Entry(periodCount, end));
                }
            } else {
                if (end < minValue) {
                    // ТП
                    entries.add(new Entry(periodCount, end));
                }
            }
            // start
            dates.add(startPeriod);
            //entries.add(new Entry(periodCount++, start));
            if (isDualChartMode) {
                //end
                dates.add(endPeriod);
                periodCount += 1;
            }
            dates.add(endPeriod);
            //entries.add(new Entry(periodCount, end));
        }

        return new ChartResponseContainer<>(entries, dates, period);
    }
}
