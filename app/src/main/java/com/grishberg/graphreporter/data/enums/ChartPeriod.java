package com.grishberg.graphreporter.data.enums;

/**
 * Created by grishberg on 17.01.17.
 * Структура хранения периода графика
 */
public enum ChartPeriod {
    MINUTE(1, 1),
    MINUTE_15(1, 15),
    HOUR(1, 60),
    HOUR_2(1, 60 * 2),
    DAY(1, 60 * 60 * 24),
    WEEK(7, 60 * 24 * 7),
    MONTH(30, 60 * 24 * 7 * 4),
    YEAR(365, 60 * 24 * 365),
    MAX(1, 999);

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
