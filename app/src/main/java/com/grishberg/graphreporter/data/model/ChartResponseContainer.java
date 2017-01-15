package com.grishberg.graphreporter.data.model;

import com.github.mikephil.charting.data.CandleEntry;

import java.util.List;

/**
 * Created by grishberg on 15.01.17.
 */
public class ChartResponseContainer {
    private final List<CandleEntry> entries;
    private final List<Long> dates;

    public ChartResponseContainer(final List<CandleEntry> entries, final List<Long> dates) {
        this.entries = entries;
        this.dates = dates;
    }

    public List<CandleEntry> getEntries() {
        return entries;
    }

    public List<Long> getDates() {
        return dates;
    }
}
