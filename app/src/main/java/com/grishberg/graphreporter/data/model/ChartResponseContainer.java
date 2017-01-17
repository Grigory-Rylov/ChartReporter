package com.grishberg.graphreporter.data.model;

import com.github.mikephil.charting.data.CandleEntry;

import java.util.List;

/**
 * Created by grishberg on 15.01.17.
 */
public class ChartResponseContainer {

    private final List<CandleEntry> entries;
    private final List<Long> dates;
    private final ChartPeriod period;

    public ChartResponseContainer(final List<CandleEntry> entries,
                                  final List<Long> dates,
                                  final ChartPeriod period) {
        this.entries = entries;
        this.dates = dates;
        this.period = period;
    }

    public List<CandleEntry> getEntries() {
        return entries;
    }

    public List<Long> getDates() {
        return dates;
    }

    public ChartPeriod getPeriod() {
        return period;
    }
}
