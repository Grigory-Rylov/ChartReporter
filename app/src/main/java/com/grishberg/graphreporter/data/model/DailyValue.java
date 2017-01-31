package com.grishberg.graphreporter.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by grishberg on 11.01.17.
 * Модель дневных данных с сервера
 */
public class DailyValue {

    private final int id;
    private final long dt;
    @SerializedName("price1")
    private final float priceStart;
    @SerializedName("price2")
    private final float priceEnd;
    @SerializedName("price3")
    private final float priceHi;
    @SerializedName("price4")
    private final float priceLo;
    private final float volume;

    public DailyValue(final int id,
                      final long dt,
                      final float priceStart,
                      final float priceEnd,
                      final float priceHi,
                      final float priceLo,
                      final float volume) {
        this.id = id;
        this.dt = dt;
        this.priceStart = priceStart;
        this.priceEnd = priceEnd;
        this.priceHi = priceHi;
        this.priceLo = priceLo;
        this.volume = volume;
    }

    public DailyValue(final int id,
                      final long dt,
                      final float priceStart,
                      final float priceHi,
                      final float priceLo,
                      final float priceEnd) {
        this.id = id;
        this.dt = dt;
        this.priceStart = priceStart;
        this.priceEnd = priceEnd;
        this.priceHi = priceHi;
        this.priceLo = priceLo;
        this.volume = 0;
    }

    public int getId() {
        return id;
    }

    public long getDt() {
        return dt;
    }

    public float getPriceStart() {
        return priceStart;
    }

    public float getPriceEnd() {
        return priceEnd;
    }

    public float getPriceHi() {
        return priceHi;
    }

    public float getPriceLo() {
        return priceLo;
    }

    public float getVolume() {
        return volume;
    }
}
