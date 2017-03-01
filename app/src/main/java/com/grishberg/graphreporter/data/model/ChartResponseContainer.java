package com.grishberg.graphreporter.data.model;

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
    @Nullable private FormulaContainer formulaContainer;

    public ChartResponseContainer(final List<T> entries,
                                  final ChartPeriod period) {
        this.entries = entries;
        this.period = period;
    }

    public ChartResponseContainer(final List<T> entries,
                                  final ChartPeriod period,
                                  final FormulaContainer formulaContainer) {
        this.entries = entries;
        this.period = period;
        this.formulaContainer = formulaContainer;
    }


    public List<T> getEntries() {
        return entries;
    }

    public ChartPeriod getPeriod() {
        return period;
    }
}
