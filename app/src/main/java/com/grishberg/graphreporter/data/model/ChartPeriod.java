package com.grishberg.graphreporter.data.model;

/**
 * Created by grishberg on 17.01.17.
 */
public enum ChartPeriod {
    DAY(1),
    WEEK(7),
    MONTH(30),
    HALF_YEAR(180),
    YEAR(365);

    private final int partion;

    ChartPeriod(final int partion) {
        this.partion = partion;
    }

    public int getPartion() {
        return partion;
    }
}
