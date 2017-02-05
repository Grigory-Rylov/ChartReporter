package com.grishberg.graphreporter.data.model;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by grishberg on 30.01.17.
 * Контейнер для графика формулы
 */
public class FormulaChartContainer {
    private final List<Entry> growPoints;
    private final List<Entry> fallPoints;
    private final FormulaContainer formulaContainer;

    public FormulaChartContainer(final List<Entry> growPoints,
                                 final List<Entry> fallPoints,
                                 final FormulaContainer formulaContainer) {
        this.growPoints = growPoints;
        this.fallPoints = fallPoints;
        this.formulaContainer = formulaContainer;
    }

    public List<Entry> getGrowPoints() {
        return growPoints;
    }

    public List<Entry> getFallPoints() {
        return fallPoints;
    }

    public FormulaContainer getFormulaContainer() {
        return formulaContainer;
    }
}
