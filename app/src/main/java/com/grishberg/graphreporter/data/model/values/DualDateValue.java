package com.grishberg.graphreporter.data.model.values;

import com.grishberg.graphreporter.data.model.DailyValue;

/**
 * Created by grishberg on 01.03.17.
 */
public class DualDateValue {
    private long dtStart;
    private long dtEnd;
    private double priceOpen;
    private double priceClose;
    private double priceHigh;
    private double priceLow;
    private double volume;

    public DualDateValue() {
    }

    public DualDateValue(long dtStart,
                         long dtEnd,
                         double priceOpen,
                         double priceClose,
                         double priceHigh,
                         double priceLow,
                         double volume) {
        this.dtStart = dtStart;
        this.dtEnd = dtEnd;
        this.priceOpen = priceOpen;
        this.priceClose = priceClose;
        this.priceHigh = priceHigh;
        this.priceLow = priceLow;
        this.volume = volume;
    }

    public static DualDateValue makeFromOpen(final double priceOpen) {
        final DualDateValue value = new DualDateValue();
        value.priceOpen = priceOpen;
        return value;
    }

    public static DualDateValue makeFromClosed(final double priceClose) {
        final DualDateValue value = new DualDateValue();
        value.priceClose = priceClose;
        return value;
    }

    public static DualDateValue makeFromHi(final double priceHigh) {
        final DualDateValue value = new DualDateValue();
        value.priceHigh = priceHigh;
        return value;
    }

    public static DualDateValue makeFromLo(final double priceLow) {
        final DualDateValue value = new DualDateValue();
        value.priceLow = priceLow;
        return value;
    }

    public long getDtStart() {
        return dtStart;
    }

    public long getDtEnd() {
        return dtEnd;
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

    public long getMidDt() {
        return (dtStart + dtEnd) / 2;
    }
}
