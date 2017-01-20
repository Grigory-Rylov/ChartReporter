package com.grishberg.graphreporter.data.enums;

/**
 * Created by grishberg on 17.01.17.
 * Структура хранения периода графика
 */
public enum ChartPeriod {
    DAY(1, 24),
    WEEK(7, 1),
    MONTH(30, 1),
    HALF_YEAR(180, 1),
    YEAR(365, 1);

    private final int partion;
    private final int period;

    ChartPeriod(final int partion, final int period) {
        this.partion = partion;
        this.period = period;
    }

    public int getPartion() {
        return partion;
    }

    public int getPeriod() {
        return period;
    }
}
