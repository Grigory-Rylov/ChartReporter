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
            double start = 0;
            double end = 0;
            int i;
            for (i = 0; i < periodPartionCount && pos < size; i++, pos++) {
                final DailyValue element = dailyValues.get(pos);
                if (i == 0) {
                    start = element.getPriceOpen();
                    startPeriod = element.getDt() * 1000L;
                }
                if ((i == periodPartionCount - 1) || (pos == size - 1)) {
                    end = element.getPriceClosed();
                    endPeriod = element.getDt() * 1000L;
                }
            }
            // start
            dates.add(startPeriod);
            entries.add(new Entry(periodCount++, (float) start));
            if (isDualChartMode) {
                //end
                dates.add(endPeriod);
                periodCount += 1;
            }
            dates.add(endPeriod);
            entries.add(new Entry(periodCount, (float) end));
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
            double hi = 0;
            double lo = Float.MAX_VALUE;
            double start = 0;
            double end = 0;
            int i;
            for (i = 0; i < periodPartionCount && pos < size; i++, pos++) {
                final DailyValue element = dailyValues.get(pos);
                hi = Math.max(element.getPriceHi(), hi);
                lo = Math.min(element.getPriceLo(), lo);
                if (i == 0) {
                    start = element.getPriceOpen();
                }
                if ((i == periodPartionCount - 1) || (pos == size - 1)) {
                    end = element.getPriceClosed();
                }
                currentDt = element.getDt() * 1000L;
            }
            dates.add(currentDt);
            if (isDualChartMode) {
                dates.add(currentDt);
                dates.add(currentDt);
            }
            periodCount += candlePeriodIncrement;
            entries.add(new CandleEntry(periodCount - candlePeriodOffset,
                    (float) hi,
                    (float) lo,
                    (float) start, (float) end));
        }

        return new ChartResponseContainer<>(entries, dates, period);
    }

    /**
     * Формирование данных для формулы
     *
     * @param period
     * @param dailyValues
     * @return
     */
    @NonNull
    public static ChartResponseContainer<Entry> getFormulaDataForPeriod(final ChartPeriod period,
                                                                        final List<DailyValue> dailyValues,
                                                                        final FormulaContainer formulaContainer) {
        final List<Long> dates = new ArrayList<>();
        final List<Entry> entries = new ArrayList<>();
        final int periodPartionCount = period.getPartion();
        final DailyValue startValue = getValueToCompare(dailyValues, formulaContainer);
        long endPeriod = 0;
        long startPeriod = 0;
        int pos = 0;
        final int size = dailyValues.size();
        int periodCount = 0;
        while (pos < size) {
            double open = 0;
            double close = 0;
            double hi = 0;
            double lo = Float.MAX_VALUE;
            int i;
            // пропуск какого то количества точек, которые входят в период
            for (i = 0; i < periodPartionCount && pos < size; i++, pos++) {
                final DailyValue element = dailyValues.get(pos);
                if (i == 0) {
                    open = element.getPriceOpen();
                    startPeriod = element.getDt() * 1000L;
                }
                if ((i == periodPartionCount - 1) || (pos == size - 1)) {
                    close = element.getPriceClosed();
                    endPeriod = element.getDt() * 1000L;
                }
                hi = Math.max(element.getPriceHi(), hi);
                lo = Math.min(element.getPriceLo(), lo);
            }
            if (addIfConditionTrue(startValue,
                    DailyValue.makeFromCandle(open, hi, lo, close),
                    formulaContainer,
                    periodCount,
                    entries)) {
                dates.add((startPeriod + endPeriod) / 2);
            }
            periodCount++;
        }

        return new ChartResponseContainer<>(entries, dates, period, formulaContainer);
    }

    /**
     * Сформировать начальную точку
     *
     * @param dailyValues
     * @param formulaContainer
     * @return
     */
    private static DailyValue getValueToCompare(@NonNull final List<DailyValue> dailyValues,
                                                @NonNull final FormulaContainer formulaContainer) {
        if (formulaContainer.isGreater()) {
            switch (formulaContainer.getVertexType()) {
                case OPEN:
                    return DailyValue.makeFromOpen(dailyValues.get(0).getPriceOpen() *
                            (1D + formulaContainer.getFormulaValue() / 100D));
                case CLOSED:
                    return DailyValue.makeFromClosed(dailyValues.get(0).getPriceClosed() *
                            (1D + formulaContainer.getFormulaValue() / 100D));
                case HIGH:
                    return DailyValue.makeFromLo(dailyValues.get(0).getPriceLo() *
                            (1D + formulaContainer.getFormulaValue() / 100D));
                default:
                    return DailyValue.makeFromHi(dailyValues.get(0).getPriceHi() *
                            (1D + formulaContainer.getFormulaValue() / 100D));
            }
        } else {
            switch (formulaContainer.getVertexType()) {
                case OPEN:
                    return DailyValue.makeFromOpen(dailyValues.get(0).getPriceOpen() -
                            (dailyValues.get(0).getPriceOpen() *
                                    formulaContainer.getFormulaValue() / 100D));
                case CLOSED:
                    return DailyValue.makeFromClosed(dailyValues.get(0).getPriceClosed() -
                            (dailyValues.get(0).getPriceClosed() *
                                    formulaContainer.getFormulaValue() / 100D));
                case HIGH:
                    return DailyValue.makeFromLo(dailyValues.get(0).getPriceClosed() -
                            (dailyValues.get(0).getPriceClosed() *
                                    formulaContainer.getFormulaValue() / 100D));
                default:
                    return DailyValue.makeFromHi(dailyValues.get(0).getPriceClosed() -
                            (dailyValues.get(0).getPriceClosed() *
                                    formulaContainer.getFormulaValue() / 100D));
            }
        }
    }

    /**
     * Добавить точку, если нужно
     *
     * @param valueToCompare
     * @param value
     * @param formulaContainer
     * @param x
     * @param entries
     */
    private static boolean addIfConditionTrue(final DailyValue valueToCompare,
                                              final DailyValue value,
                                              final FormulaContainer formulaContainer,
                                              final int x,
                                              final List<Entry> entries) {
        if (formulaContainer.isGreater()) {
            switch (formulaContainer.getVertexType()) {
                case OPEN:
                    if (value.getPriceOpen() > valueToCompare.getPriceOpen()) {
                        entries.add(new Entry(x, (float) value.getPriceOpen()));
                        return true;
                    }
                    break;
                case CLOSED:
                    if (value.getPriceClosed() > valueToCompare.getPriceClosed()) {
                        entries.add(new Entry(x, (float) value.getPriceClosed()));
                        return true;
                    }
                    break;
                case HIGH:
                    if (value.getPriceHi() > valueToCompare.getPriceHi()) {
                        entries.add(new Entry(x, (float) value.getPriceClosed()));
                        return true;
                    }
                    break;
                default:
                    if (value.getPriceLo() > valueToCompare.getPriceLo()) {
                        entries.add(new Entry(x, (float) value.getPriceClosed()));
                        return true;
                    }
            }
        } else {
            switch (formulaContainer.getVertexType()) {
                case OPEN:
                    if (value.getPriceOpen() < valueToCompare.getPriceOpen()) {
                        entries.add(new Entry(x, (float) value.getPriceOpen()));
                        return true;
                    }
                    break;
                case CLOSED:
                    if (value.getPriceClosed() < valueToCompare.getPriceClosed()) {
                        entries.add(new Entry(x, (float) value.getPriceClosed()));
                        return true;
                    }
                    break;
                case HIGH:
                    if (value.getPriceHi() < valueToCompare.getPriceHi()) {
                        entries.add(new Entry(x, (float) value.getPriceHi()));
                        return true;
                    }
                    break;
                default:
                    if (value.getPriceLo() < valueToCompare.getPriceLo()) {
                        entries.add(new Entry(x, (float) value.getPriceLo()));
                        return true;
                    }
            }
        }
        return false;
    }
}
