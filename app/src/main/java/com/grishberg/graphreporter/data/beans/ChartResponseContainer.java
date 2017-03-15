package com.grishberg.graphreporter.data.beans;

import android.support.annotation.Nullable;

import com.github.mikephil.charting.data.Entry;
import com.grishberg.graphreporter.data.enums.ChartPeriod;

import java.util.List;

/**
 * Created by grishberg on 15.01.17.
 * Модель данных для отображения графика
 */
public class ChartResponseContainer<T extends Entry> {

    private final List<T> entries;
    private final ChartPeriod period;
    private final List<Long> dates;
    @Nullable
    private FormulaContainer formulaContainer;

    public ChartResponseContainer(final List<T> entries,
                                  final ChartPeriod period,
                                  final List<Long> dates) {
        this.entries = entries;
        this.period = period;
        this.dates = dates;
    }

    public ChartResponseContainer(final List<T> entries,
                                  final ChartPeriod period,
                                  final FormulaContainer formulaContainer,
                                  final List<Long> dates) {
        this.entries = entries;
        this.period = period;
        this.formulaContainer = formulaContainer;
        this.dates = dates;
    }

    public List<T> getEntries() {
        return entries;
    }

    public ChartPeriod getPeriod() {
        return period;
    }

    public List<Long> getDates() {
        return dates;
    }
}
