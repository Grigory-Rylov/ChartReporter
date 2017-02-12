package com.grishberg.graphreporter.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by grishberg on 11.01.17.
 * Модель дневных данных с сервера
 */
@Entity(indexes = {
        @Index(value = "dt ASC", unique = false)
})
public class DailyValue {

    private long productId;
    @Id
    private long id;
    @Property()
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

    @Generated(hash = 175731636)
    public DailyValue(long productId, long id, long dt, double priceOpen,
                      double priceClose, double priceHigh, double priceLow, double volume) {
        this.productId = productId;
        this.id = id;
        this.dt = dt;
        this.priceOpen = priceOpen;
        this.priceClose = priceClose;
        this.priceHigh = priceHigh;
        this.priceLow = priceLow;
        this.volume = volume;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public double getPriceOpen() {
        return priceOpen;
    }

    public void setPriceOpen(double priceOpen) {
        this.priceOpen = priceOpen;
    }

    public double getPriceClose() {
        return priceClose;
    }

    public void setPriceClose(double priceClose) {
        this.priceClose = priceClose;
    }

    public double getPriceHigh() {
        return priceHigh;
    }

    public void setPriceHigh(double priceHigh) {
        this.priceHigh = priceHigh;
    }

    public double getPriceLow() {
        return priceLow;
    }

    public void setPriceLow(double priceLow) {
        this.priceLow = priceLow;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(final long productId) {
        this.productId = productId;
    }
}
