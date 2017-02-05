package com.grishberg.graphreporter.data.model;

/**
 * Created by grishberg on 11.01.17.
 * Модель дневных данных с сервера
 */
public class DailyValue {

    private int id;
    private long dt;
    private double priceOpen;
    private double priceClose;
    private double priceHigh;
    private double priceLow;
    private double volume;

    public DailyValue() {
        // for fabric methods
    }

    public DailyValue(final int id,
                      final long dt,
                      final double priceStart,
                      final double priceHi,
                      final double priceLo,
                      final double priceEnd) {
        this.id = id;
        this.dt = dt;
        this.priceOpen = priceStart;
        this.priceClose = priceEnd;
        this.priceHigh = priceHi;
        this.priceLow = priceLo;
        this.volume = 0;
    }

    public static DailyValue makeFromOpen(final double priceStart) {
        final DailyValue value = new DailyValue();
        value.priceOpen = priceStart;
        return value;
    }

    public static DailyValue makeFromClosed(final double priceEnd) {
        final DailyValue value = new DailyValue();
        value.priceClose = priceEnd;
        return value;
    }

    public static DailyValue makeFromHi(final double priceHi) {
        final DailyValue value = new DailyValue();
        value.priceHigh = priceHi;
        return value;
    }

    public static DailyValue makeFromLo(final double priceLo) {
        final DailyValue value = new DailyValue();
        value.priceLow = priceLo;
        return value;
    }

    public static DailyValue makeFromCandle(final double priceOpen,
                                            final double priceHi,
                                            final double priceLo,
                                            final double priceClosed) {
        final DailyValue value = new DailyValue();
        value.priceOpen = priceOpen;
        value.priceHigh = priceHi;
        value.priceLow = priceLo;
        value.priceClose = priceClosed;
        return value;
    }

    public int getId() {
        return id;
    }

    public long getDt() {
        return dt;
    }

    public double getPriceOpen() {
        return priceOpen;
    }

    public double getPriceClose() {
        return priceClose;
    }

    public double getPriceHigh() {
        return priceHigh;
    }

    public double getPriceLow() {
        return priceLow;
    }

    public double getVolume() {
        return volume;
    }

}
