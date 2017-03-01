package com.grishberg.graphreporter.mvp.presenter;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.model.ChartResponseContainer;
import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.model.FormulaChartContainer;
import com.grishberg.graphreporter.data.model.FormulaContainer;
import com.grishberg.graphreporter.data.model.stream.ValuesStream;
import com.grishberg.graphreporter.data.model.stream.ValuesStreamImpl;
import com.grishberg.graphreporter.data.model.values.DualDateValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by grishberg on 19.01.17.
 * Формирование точек графика и формулы
 */
public class ChartsHelper {

    public static final int MILLISECOND = 1000;
    public static final float MILLISECOND_FRAKT = 1000f;
    private static final int CANDLE_PRIOD_OFFSET = -1;
    private static final int CANDLE_PERIOD_OFFSET = 1;
    private static final int CANDLE_PERIOD_INCREMENT = 2;
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

    public ChartResponseContainer<Entry> getLineData(final ChartPeriod period,
                                                     final List<DailyValue> dailyValues) {
        final List<Entry> entries = new ArrayList<>();
        final ValuesStream<DualDateValue> valuesStream = new ValuesStreamImpl(dailyValues, period.getPeriod());

        try {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                final DualDateValue nextValue = valuesStream.getNextElement();
                if (nextValue == null) {
                    continue;
                }
                entries.add(new Entry(getConvertedTime(nextValue.getDtStart()), (float) nextValue.getPriceOpen()));
                entries.add(new Entry(getConvertedTime(nextValue.getDtEnd()), (float) nextValue.getPriceClose()));
            }
        } catch (final ValuesStream.NoMoreItemException e) {
        }
        return new ChartResponseContainer<>(entries, period);
    }

    private float getConvertedTime(final long dt) {
        return TimeUnit.MILLISECONDS.toMinutes(dt * MILLISECOND) / MILLISECOND_FRAKT;
    }

    @NonNull
    public ChartResponseContainer<CandleEntry> getCandleDataForPeriod(final ChartPeriod period,
                                                                      final List<DailyValue> dailyValues) {
        final List<CandleEntry> entries = new ArrayList<>();
        final ValuesStream<DualDateValue> valuesStream = new ValuesStreamImpl(dailyValues, period.getPeriod());
        try {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                final DualDateValue nextValue = valuesStream.getNextElement();
                if (nextValue == null) {
                    continue;
                }
                entries.add(new CandleEntry(getConvertedTime(nextValue.getMidDt()),
                        (float) nextValue.getPriceHigh(),
                        (float) nextValue.getPriceLow(),
                        (float) nextValue.getPriceOpen(),
                        (float) nextValue.getPriceClose())
                );
            }
        } catch (final ValuesStream.NoMoreItemException e) {
        }

        return new ChartResponseContainer<>(entries, period);
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
                                                         final FormulaContainer formulaContainer) {
        final List<Entry> entriesGrow = new ArrayList<>();
        final List<Entry> entriesFall = new ArrayList<>();

        FormulaPointsContainer valueToCompare = null;
        previousY = 0;
        final ValuesStream<DualDateValue> valuesStream = new ValuesStreamImpl(dailyValues, period.getPeriod());
        try {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                final DualDateValue nextValue = valuesStream.getNextElement();
                if (nextValue == null) {
                    continue;
                }
                if (valueToCompare == null) {
                    valueToCompare = getValueToCompare(nextValue, formulaContainer);
                }
                addIfConditionTrue(valueToCompare,
                        nextValue, //TODO: create class to compare
                        formulaContainer,
                        entriesGrow,
                        entriesFall);
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
                                              final List<Entry> entriesGrow,
                                              final List<Entry> entriesFall) {
        // ТР
        double currentValue;
        final double growPriceToCompare;
        final double fallPriceToCompare;
        float x;
        switch (fc.getVertexType()) {
            case OPEN:
                currentValue = value.getPriceOpen();
                growPriceToCompare = valueToCompare.valueGrow.getPriceOpen();
                x = getConvertedTime(value.getDtStart());
                break;
            case CLOSED:
                currentValue = value.getPriceClose();
                growPriceToCompare = valueToCompare.valueGrow.getPriceClose();
                x = getConvertedTime(value.getDtEnd());
                break;
            case HIGH:
                currentValue = value.getPriceHigh();
                growPriceToCompare = valueToCompare.valueGrow.getPriceHigh();
                x = getConvertedTime(value.getMidDt());
                break;
            case LOW:
            default:
                currentValue = value.getPriceLow();
                growPriceToCompare = valueToCompare.valueGrow.getPriceLow();
                x = getConvertedTime(value.getMidDt());
        }
        if (currentValue > growPriceToCompare || currentValue == previousY) {
            if (previousY == currentValue && !entriesFall.isEmpty()) {
                entriesFall.remove(entriesFall.size() - 1);
            }
            entriesFall.add(new Entry(x, (float) currentValue));
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
                x = getConvertedTime(value.getDtStart());
                break;
            case CLOSED:
                currentValue = value.getPriceClose();
                fallPriceToCompare = valueToCompare.valueFall.getPriceClose();
                x = getConvertedTime(value.getDtEnd());
                break;
            case HIGH:
                currentValue = value.getPriceHigh();
                fallPriceToCompare = valueToCompare.valueFall.getPriceHigh();
                x = getConvertedTime(value.getMidDt());
                break;
            case LOW:
            default:
                currentValue = value.getPriceLow();
                fallPriceToCompare = valueToCompare.valueFall.getPriceLow();
                x = getConvertedTime(value.getMidDt());
        }

        if (currentValue < fallPriceToCompare || currentValue == previousY) {
            if (previousY == currentValue && !entriesGrow.isEmpty()) {
                entriesGrow.remove(entriesGrow.size() - 1);
            }
            entriesGrow.add(new Entry(x, (float) currentValue));
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
