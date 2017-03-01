package com.grishberg.graphreporter.data.enums;

/**
 * Created by grishberg on 17.01.17.
 * Структура хранения периода графика
 */
public enum ChartPeriod {
    MINUTE_3(10, 60 * 3),
    MINUTE_15(10, 60 * 15),
    MINUTE_30(30, 60 * 30),
    HOUR(60, 60 * 60),
    DAY(1, 60 * 60 * 24),
    WEEK(7, 60 * 24 * 7),
    MONTH(30, 60 * 24 * 7 * 4),
    YEAR(1, 60 * 24 * 365);

    private final float partion;
    private final int period;

    ChartPeriod(final int partion, final int period) {
        this.partion = partion;
        this.period = period;
    }

    public float getPartion() {
        return partion;
    }

    public int getPeriod() {
        return period;
    }
}
