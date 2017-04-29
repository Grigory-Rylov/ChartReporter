package com.grishberg.graphreporter.mvp.presenter;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.beans.ChartResponseContainer;
import com.grishberg.graphreporter.data.beans.DailyValue;
import com.grishberg.graphreporter.data.beans.FormulaChartContainer;
import com.grishberg.graphreporter.data.beans.FormulaContainer;
import com.grishberg.graphreporter.data.beans.stream.ValuesStream;
import com.grishberg.graphreporter.data.beans.stream.ValuesStreamImpl;
import com.grishberg.graphreporter.data.beans.values.DualDateValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by grishberg on 19.01.17.
 * Формирование точек графика и формулы
 */
public class DailyValueToGraphEntryConverter {

    private static final int MILLISECOND = 1000;
    private static final int OPEN = 0;
    private static final int CLOSED = 1;
    private static final int HIGH = 2;
    private static final int LOW = 3;
    private static final int OPEN_AND_CLOSED = 4;
    private double previousY;

    private static double getNewFallValue(final double firstValue,
                                          @NonNull final FormulaContainer formulaContainer) {
        return formulaContainer.isFallPercent() ?
                firstValue -
                        (firstValue * formulaContainer.getFallValue() / 100D) :
                firstValue - formulaContainer.getFallValue();
    }

    private static double getNewGrowValue(final double firstValue,
                                          @NonNull final FormulaContainer formulaContainer) {
        return formulaContainer.isGrowPercent() ?
                firstValue *
                        (1D + formulaContainer.getGrowValue() / 100D) :
                firstValue + formulaContainer.getGrowValue();
    }

    private static DualDateValue makeDualDailyValue(final double value, @NonNull final FormulaContainer fc) {
        switch (fc.getVertexType()) {
            case OPEN:
                return DualDateValue.makeFromOpen(value);

            case CLOSED:
                return DualDateValue.makeFromClosed(value);

            case HIGH:
                return DualDateValue.makeFromHi(value);

            case LOW:
            default:
                return DualDateValue.makeFromLo(value);
        }
    }

    ChartResponseContainer<Entry> getLineData(final ChartPeriod period,
                                                     final List<DailyValue> dailyValues) {
        final List<Long> dates = new ArrayList<>();
        final List<Entry> entries = new ArrayList<>();
        final ValuesStream<DualDateValue> valuesStream = new ValuesStreamImpl(dailyValues, period.getPeriod());
        int position = -1;
        try {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                final DualDateValue nextValue = valuesStream.getNextElement();
                position++;
                if (nextValue == null) {
                    dates.add(0L);
                    dates.add(0L);
                    dates.add(0L);
                    position += 2;
                    continue;
                }
                dates.add(nextValue.getDtStart());
                entries.add(new Entry(position++, (float) nextValue.getPriceOpen(), nextValue.getDtStart()));
                dates.add(nextValue.getMidDt());
                dates.add(nextValue.getDtEnd());
                entries.add(new Entry(++position, (float) nextValue.getPriceClose()));
            }
        } catch (final ValuesStream.NoMoreItemException e) {
        }
        return new ChartResponseContainer<>(entries, period, dates);
    }

    private float getConvertedTime(final long dt) {
        return TimeUnit.MILLISECONDS.toHours(dt * MILLISECOND);
    }

    @NonNull
    ChartResponseContainer<CandleEntry> getCandleDataForPeriod(final ChartPeriod period,
                                                                      final List<DailyValue> dailyValues) {
        final List<Long> dates = new ArrayList<>();
        final List<CandleEntry> entries = new ArrayList<>();
        final ValuesStream<DualDateValue> valuesStream = new ValuesStreamImpl(dailyValues, period.getPeriod());
        int position = -1;
        try {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                final DualDateValue nextValue = valuesStream.getNextElement();
                position++;
                if (nextValue == null) {
                    dates.add(0L);
                    dates.add(0L);
                    dates.add(0L);
                    position += 2;
                    continue;
                }
                dates.add(nextValue.getDtStart());
                position++;
                dates.add(nextValue.getMidDt());
                entries.add(new CandleEntry(position,
                        (float) nextValue.getPriceHigh(),
                        (float) nextValue.getPriceLow(),
                        (float) nextValue.getPriceOpen(),
                        (float) nextValue.getPriceClose(),
                        nextValue.getMidDt())
                );
                position++;
                dates.add(nextValue.getDtEnd());
            }
        } catch (final ValuesStream.NoMoreItemException e) {
        }

        return new ChartResponseContainer<>(entries, period, dates);
    }

    /**
     * Формирование данных для формулы
     *
     * @param period      период, за который отображается график
     * @param dailyValues искодные данные
     * @return возвращается объект, содержащий данные для отображения 2х типов точек ТР и ТП
     */
    @NonNull
    FormulaChartContainer getFormulaDataForPeriod(final ChartPeriod period,
                                                         final List<DailyValue> dailyValues,
                                                         final FormulaContainer formulaContainer) {
        final List<Entry> entriesGrow = new ArrayList<>();
        final List<Entry> entriesFall = new ArrayList<>();

        FormulaPointsContainer valueToCompare = null;
        previousY = 0;
        int position = -1;
        final ValuesStream<DualDateValue> valuesStream = new ValuesStreamImpl(dailyValues, period.getPeriod());
        try {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                final DualDateValue nextValue = valuesStream.getNextElement();
                position++;
                if (nextValue == null) {
                    position += 2;
                    continue;
                }
                if (valueToCompare == null) {
                    valueToCompare = getValueToCompare(nextValue, formulaContainer);
                }
                position++;
                addIfConditionTrue(valueToCompare,
                        nextValue, //TODO: create class to compare
                        formulaContainer,
                        position,
                        entriesGrow,
                        entriesFall);
                position++;
            }
        } catch (final ValuesStream.NoMoreItemException e) {
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
    private FormulaPointsContainer getValueToCompare(@NonNull final DualDateValue dailyValue,
                                                     @NonNull final FormulaContainer formulaContainer) {
        switch (formulaContainer.getVertexType()) {
            case OPEN:
                return new FormulaPointsContainer(
                        DualDateValue.makeFromOpen(getNewGrowValue(dailyValue.getPriceOpen(), formulaContainer)),

                        DualDateValue.makeFromOpen(getNewFallValue(dailyValue.getPriceOpen(), formulaContainer))
                );
            case CLOSED:
                return new FormulaPointsContainer(
                        DualDateValue.makeFromClosed(getNewGrowValue(dailyValue.getPriceClose(), formulaContainer)),

                        DualDateValue.makeFromClosed(getNewFallValue(dailyValue.getPriceClose(), formulaContainer))
                );
            case HIGH:
                return new FormulaPointsContainer(
                        DualDateValue.makeFromHi(getNewGrowValue(dailyValue.getPriceHigh(), formulaContainer)),

                        DualDateValue.makeFromHi(getNewFallValue(dailyValue.getPriceHigh(), formulaContainer))
                );
            case LOW:
            default:
                return new FormulaPointsContainer(
                        DualDateValue.makeFromLo(getNewGrowValue(dailyValue.getPriceLow(), formulaContainer)),

                        DualDateValue.makeFromLo(getNewFallValue(dailyValue.getPriceLow(), formulaContainer))
                );
        }
    }

    /**
     * Добавить точку, если нужно, обновить значение старой точки ТР или ТП
     *
     * @param valueToCompare
     * @param value
     * @param fc
     * @param entriesGrow
     * @param entriesFall
     */
    private PrevValueState addIfConditionTrue(final FormulaPointsContainer valueToCompare,
                                              final DualDateValue value,
                                              final FormulaContainer fc,
                                              final int x,
                                              final List<Entry> entriesGrow,
                                              final List<Entry> entriesFall) {
        // ТР
        double currentValue;
        final double growPriceToCompare;
        final double fallPriceToCompare;
        int offset = 0;
        switch (fc.getVertexType()) {
            case OPEN:
                currentValue = value.getPriceOpen();
                growPriceToCompare = valueToCompare.valueGrow.getPriceOpen();
                offset = -1;
                break;
            case CLOSED:
                currentValue = value.getPriceClose();
                growPriceToCompare = valueToCompare.valueGrow.getPriceClose();
                offset = 1;
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
        if (currentValue > growPriceToCompare || currentValue == previousY) {
            if (previousY == currentValue && !entriesFall.isEmpty()) {
                entriesFall.remove(entriesFall.size() - 1);
            }
            entriesFall.add(new Entry(x + offset, (float) currentValue));
            previousY = currentValue;
            valueToCompare.valueGrow = value; // сдвиг точки
            valueToCompare.valueFall = makeDualDailyValue(getNewFallValue(currentValue, fc), fc);
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

        if (currentValue < fallPriceToCompare || currentValue == previousY) {
            if (previousY == currentValue && !entriesGrow.isEmpty()) {
                entriesGrow.remove(entriesGrow.size() - 1);
            }
            entriesGrow.add(new Entry(x + offset, (float) currentValue));
            previousY = currentValue;
            valueToCompare.valueFall = value;
            valueToCompare.valueGrow = makeDualDailyValue(getNewGrowValue(currentValue, fc), fc);
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
        DualDateValue valueGrow;
        DualDateValue valueFall;

        FormulaPointsContainer(final DualDateValue valueGrow, final DualDateValue valueFall) {
            this.valueGrow = valueGrow;
            this.valueFall = valueFall;
        }
    }
}
