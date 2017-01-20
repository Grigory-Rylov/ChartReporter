package com.grishberg.graphreporter.data.model;

import com.github.mikephil.charting.data.Entry;
import com.grishberg.graphreporter.data.enums.ChartPeriod;

import java.util.List;

/**
 * Created by grishberg on 15.01.17.
 * Модель данных для отображения графика
 */
public class ChartResponseContainer<T extends Entry> {

    private final List<T> entries;
    private final List<Long> dates;
    private final ChartPeriod period;

    public ChartResponseContainer(final List<T> entries,
                                  final List<Long> dates,
                                  final ChartPeriod period) {
        this.entries = entries;
        this.dates = dates;
        this.period = period;
    }

    public List<T> getEntries() {
        return entries;
    }

    public List<Long> getDates() {
        return dates;
    }

    public ChartPeriod getPeriod() {
        return period;
    }
}
