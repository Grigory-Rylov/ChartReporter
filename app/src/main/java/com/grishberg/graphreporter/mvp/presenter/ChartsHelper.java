package com.grishberg.graphreporter.mvp.presenter;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.model.ChartResponseContainer;
import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.model.FormulaChartContainer;
import com.grishberg.graphreporter.data.model.FormulaContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grishberg on 19.01.17.
 * Формирование точек графика и формулы
 */
public class ChartsHelper {

    public static final int CANDLE_PRIOD_OFFSET = -1;
    private static final int CANDLE_PERIOD_OFFSET = 1;
    private static final int CANDLE_PERIOD_INCREMENT = 2;
    private static final int OPEN = 0;
    private static final int CLOSED = 1;
    private static final int HIGH = 2;
    private static final int LOW = 3;
    private int previousGrowX;
    private int previousFallX;

    /**
     * Новая точка роста
     *
     * @param firstValue
     * @param formulaContainer
     * @return
     */
    private static double getNewFallValue(@NonNull final double firstValue,
                                          @NonNull final FormulaContainer formulaContainer) {
        return formulaContainer.isFallPercent() ?
                firstValue -
                        (firstValue * formulaContainer.getFallValue() / 100D) :
                firstValue - formulaContainer.getFallValue();
    }

    /**
     * Новая точка падения
     *
     * @param firstValue
     * @param formulaContainer
     * @return
     */
    private static double getNewGrowValue(@NonNull final double firstValue,
                                          @NonNull final FormulaContainer formulaContainer) {
        return formulaContainer.isGrowPercent() ?
                firstValue *
                        (1D + formulaContainer.getGrowValue() / 100D) :
                firstValue + formulaContainer.getGrowValue();
    }

    private static DailyValue makeDailyValue(final double value, @NonNull final FormulaContainer fc) {
        switch (fc.getVertexType()) {
            case OPEN:
                return DailyValue.makeFromOpen(value);

            case CLOSED:
                return DailyValue.makeFromClosed(value);

            case HIGH:
                return DailyValue.makeFromHi(value);

            case LOW:
            default:
                return DailyValue.makeFromLo(value);
        }
    }

    public ChartResponseContainer<Entry> getLineData(final ChartPeriod period,
                                                     final List<DailyValue> dailyValues,
                                                     final boolean isDualChartMode) {
        final List<Long> dates = new ArrayList<>();
        final List<Entry> entries = new ArrayList<>();
        final int periodPartionCount = period.getPartion();
        long endPeriod = 0;
        long startPeriod = 0;
        int pos = 0;
        final int size = dailyValues.size();
        int periodCount = isDualChartMode ? 0 : -1;
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
                    end = element.getPriceClose();
                    endPeriod = element.getDt() * 1000L;
                }
            }
            // start
            dates.add(startPeriod);
            entries.add(new Entry(periodCount, (float) start));
            if (isDualChartMode) {
                //end
                dates.add((long) ((startPeriod + endPeriod) / 2.d));
                periodCount += 1;
            }
            entries.add(new Entry(++periodCount, (float) end));
        }

        return new ChartResponseContainer<>(entries, dates, period);
    }

    @NonNull
    public ChartResponseContainer<CandleEntry> getCandleDataForPeriod(final ChartPeriod period,
                                                                      final List<DailyValue> dailyValues,
                                                                      final boolean isDualChartMode) {
        final List<Long> dates = new ArrayList<>();
        final List<CandleEntry> entries = new ArrayList<>();
        final int periodPartionCount = period.getPartion();
        final int candlePeriodOffset = isDualChartMode ? CANDLE_PERIOD_OFFSET : 0;
        final int candlePeriodIncrement = isDualChartMode ? CANDLE_PERIOD_INCREMENT : 1;
        int periodCount = isDualChartMode ? 0 : CANDLE_PRIOD_OFFSET;
        long currentDt = 0;
        int pos = 0;
        final int size = dailyValues.size();
        while (pos < size) {
            double hi = 0;
            double lo = Float.MAX_VALUE;
            double start = 0;
            double end = 0;
            int i;
            for (i = 0; i < periodPartionCount && pos < size; i++, pos++) {
                final DailyValue element = dailyValues.get(pos);
                hi = Math.max(element.getPriceHigh(), hi);
                lo = Math.min(element.getPriceLow(), lo);
                if (i == 0) {
                    start = element.getPriceOpen();
                }
                if ((i == periodPartionCount - 1) || (pos == size - 1)) {
                    end = element.getPriceClose();
                }
                currentDt = element.getDt() * 1000L;
            }
            dates.add(currentDt);
            if (isDualChartMode) {
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
     * @param period      период, за который отображается график
     * @param dailyValues искодные данные
     * @return возвращается объект, содержащий данные для отображения 2х типов точек ТР и ТП
     */
    @NonNull
    public FormulaChartContainer getFormulaDataForPeriod(final ChartPeriod period,
                                                         final List<DailyValue> dailyValues,
                                                         final FormulaContainer formulaContainer,
                                                         final boolean isDualChartMode) {
        final List<Entry> entriesGrow = new ArrayList<>();
        final List<Entry> entriesFall = new ArrayList<>();
        final int periodPartitionCount = period.getPartion();

        final FormulaPointsContainer valueToCompare = getValueToCompare(dailyValues.get(0), formulaContainer);
        previousGrowX = 0;
        previousFallX = 0;
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
            for (i = 0; i < periodPartitionCount && pos < size; i++, pos++) {
                final DailyValue element = dailyValues.get(pos);
                if (i == 0) {
                    open = element.getPriceOpen();
                }
                if ((i == periodPartitionCount - 1) || (pos == size - 1)) {
                    close = element.getPriceClose();
                }
                hi = Math.max(element.getPriceHigh(), hi);
                lo = Math.min(element.getPriceLow(), lo);
            }
            if (isDualChartMode) {
                periodCount++;
            }

            addIfConditionTrue(valueToCompare,
                    DailyValue.makeFromCandle(open, hi, lo, close),
                    formulaContainer,
                    periodCount,
                    entriesGrow,
                    entriesFall);
            periodCount++;
        }
        return new FormulaChartContainer(entriesGrow, entriesFall, formulaContainer);
    }

    /**
     * Сформировать начальную точку
     *
     * @param dailyValue
     * @param formulaContainer
     * @return
     */
    private FormulaPointsContainer getValueToCompare(@NonNull final DailyValue dailyValue,
                                                     @NonNull final FormulaContainer formulaContainer) {
        switch (formulaContainer.getVertexType()) {
            case OPEN:
                return new FormulaPointsContainer(
                        DailyValue.makeFromOpen(getNewGrowValue(dailyValue.getPriceOpen(), formulaContainer)),

                        DailyValue.makeFromOpen(getNewFallValue(dailyValue.getPriceOpen(), formulaContainer))
                );
            case CLOSED:
                return new FormulaPointsContainer(
                        DailyValue.makeFromClosed(getNewGrowValue(dailyValue.getPriceClose(), formulaContainer)),

                        DailyValue.makeFromClosed(getNewFallValue(dailyValue.getPriceClose(), formulaContainer))
                );
            case HIGH:
                return new FormulaPointsContainer(
                        DailyValue.makeFromHi(getNewGrowValue(dailyValue.getPriceHigh(), formulaContainer)),

                        DailyValue.makeFromHi(getNewFallValue(dailyValue.getPriceHigh(), formulaContainer))
                );
            case LOW:
            default:
                return new FormulaPointsContainer(
                        DailyValue.makeFromLo(getNewGrowValue(dailyValue.getPriceLow(), formulaContainer)),

                        DailyValue.makeFromLo(getNewFallValue(dailyValue.getPriceLow(), formulaContainer))
                );
        }
    }

    /**
     * Добавить точку, если нужно, обновить значение старой точки ТР или ТП
     *
     * @param valueToCompare
     * @param value
     * @param fc
     * @param x
     * @param entriesGrow
     * @param entriesFall
     */
    private PrevValueState addIfConditionTrue(final FormulaPointsContainer valueToCompare,
                                              final DailyValue value,
                                              final FormulaContainer fc,
                                              final int x,
                                              final List<Entry> entriesGrow,
                                              final List<Entry> entriesFall) {
        // ТР
        double currentValue;
        final double growPriceToCompare;
        final double fallPriceToCompare;
        switch (fc.getVertexType()) {
            case OPEN:
                currentValue = value.getPriceOpen();
                growPriceToCompare = valueToCompare.valueGrow.getPriceOpen();
                break;
            case CLOSED:
                currentValue = value.getPriceClose();
                growPriceToCompare = valueToCompare.valueGrow.getPriceClose();
                break;
            case HIGH:
                currentValue = value.getPriceHigh();
                growPriceToCompare = valueToCompare.valueGrow.getPriceHigh();
                break;
            case LOW:
            default:
                currentValue = value.getPriceLow();
                growPriceToCompare = valueToCompare.valueGrow.getPriceLow();
        }
        if (currentValue > growPriceToCompare) {
            entriesFall.add(new Entry(x, (float) currentValue));
            valueToCompare.valueGrow = value; // сдвиг точки
            valueToCompare.valueFall = makeDailyValue(getNewFallValue(currentValue, fc), fc);
            this.previousGrowX = x;
            return PrevValueState.GROW;
        }

        //ТП
        switch (fc.getVertexType()) {
            case OPEN:
                currentValue = value.getPriceOpen();
                fallPriceToCompare = valueToCompare.valueFall.getPriceOpen();
                break;
            case CLOSED:
                currentValue = value.getPriceClose();
                fallPriceToCompare = valueToCompare.valueFall.getPriceClose();
                break;
            case HIGH:
                currentValue = value.getPriceHigh();
                fallPriceToCompare = valueToCompare.valueFall.getPriceHigh();
                break;
            case LOW:
            default:
                currentValue = value.getPriceLow();
                fallPriceToCompare = valueToCompare.valueFall.getPriceLow();
        }

        if (currentValue < fallPriceToCompare) {
            entriesGrow.add(new Entry(x, (float) currentValue));
            valueToCompare.valueFall = value;
            valueToCompare.valueGrow = makeDailyValue(getNewGrowValue(currentValue, fc), fc);
            this.previousFallX = x;
            return PrevValueState.FALL;
        }

        return PrevValueState.NEUTRAL;
    }

    private enum PrevValueState {
        GROW,
        FALL,
        NEUTRAL
    }

    private static class FormulaPointsContainer {
        DailyValue valueGrow;
        DailyValue valueFall;

        FormulaPointsContainer(final DailyValue valueGrow, final DailyValue valueFall) {
            this.valueGrow = valueGrow;
            this.valueFall = valueFall;
        }
    }
}
