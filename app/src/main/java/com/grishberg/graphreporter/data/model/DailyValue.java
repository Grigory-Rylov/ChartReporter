package com.grishberg.graphreporter.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by grishberg on 11.01.17.
 * Модель дневных данных с сервера
 */
public class DailyValue {

    private int id;
    private long dt;
    @SerializedName("price1")
    private double priceOpen;
    @SerializedName("price2")
    private double priceClosed;
    @SerializedName("price3")
    private double priceHi;
    @SerializedName("price4")
    private double priceLo;
    private double volume;

    public DailyValue() {
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
        this.priceClosed = priceEnd;
        this.priceHi = priceHi;
        this.priceLo = priceLo;
        this.volume = 0;
    }

    public static DailyValue makeFromOpen(final double priceStart) {
        final DailyValue value = new DailyValue();
        value.priceOpen = priceStart;
        return value;
    }

    public static DailyValue makeFromClosed(final double priceEnd) {
        final DailyValue value = new DailyValue();
        value.priceClosed = priceEnd;
        return value;
    }

    public static DailyValue makeFromHi(final double priceHi) {
        final DailyValue value = new DailyValue();
        value.priceHi = priceHi;
        return value;
    }

    public static DailyValue makeFromLo(final double priceLo) {
        final DailyValue value = new DailyValue();
        value.priceLo = priceLo;
        return value;
    }

    public static DailyValue makeFromCandle(final double priceOpen,
                                            final double priceHi,
                                            final double priceLo,
                                            final double priceClosed) {
        final DailyValue value = new DailyValue();
        value.priceOpen = priceOpen;
        value.priceHi = priceHi;
        value.priceLo = priceLo;
        value.priceClosed = priceClosed;
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

    public double getPriceClosed() {
        return priceClosed;
    }

    public double getPriceHi() {
        return priceHi;
    }

    public double getPriceLo() {
        return priceLo;
    }

    public double getVolume() {
        return volume;
    }
}
