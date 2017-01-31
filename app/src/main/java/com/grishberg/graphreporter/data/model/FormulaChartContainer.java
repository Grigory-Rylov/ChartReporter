package com.grishberg.graphreporter.data.model;

import com.github.mikephil.charting.data.Entry;
import com.grishberg.graphreporter.data.enums.ChartPeriod;

/**
 * Created by grishberg on 30.01.17.
 */
public class FormulaChartContainer {
    private final ChartPeriod period;
    private final ChartResponseContainer<Entry> entryResponse;
    private final FormulaContainer formulaContainer;

    public FormulaChartContainer(final ChartPeriod period,
                                 final ChartResponseContainer<Entry> entryResponse,
                                 final FormulaContainer formulaContainer) {
        this.period = period;
        this.entryResponse = entryResponse;
        this.formulaContainer = formulaContainer;
    }

    public ChartPeriod getPeriod() {
        return period;
    }

    public ChartResponseContainer<Entry> getEntryResponse() {
        return entryResponse;
    }

    public FormulaContainer getFormulaContainer() {
        return formulaContainer;
    }
}
