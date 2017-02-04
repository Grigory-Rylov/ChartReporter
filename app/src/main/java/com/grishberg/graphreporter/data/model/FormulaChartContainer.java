package com.grishberg.graphreporter.data.model;

import com.github.mikephil.charting.data.BubbleEntry;

import java.util.List;

/**
 * Created by grishberg on 30.01.17.
 */
public class FormulaChartContainer {
    private final List<BubbleEntry> growPoints;
    private final List<BubbleEntry> fallPoints;
    private final FormulaContainer formulaContainer;

    public FormulaChartContainer(final List<BubbleEntry> growPoints,
                                 final List<BubbleEntry> fallPoints,
                                 final FormulaContainer formulaContainer) {
        this.growPoints = growPoints;
        this.fallPoints = fallPoints;
        this.formulaContainer = formulaContainer;
    }

    public List<BubbleEntry> getGrowPoints() {
        return growPoints;
    }

    public List<BubbleEntry> getFallPoints() {
        return fallPoints;
    }

    public FormulaContainer getFormulaContainer() {
        return formulaContainer;
    }
}
