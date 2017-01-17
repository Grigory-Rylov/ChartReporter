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
    private float priceStart;
    @SerializedName("price2")
    private float priceEnd;
    @SerializedName("price3")
    private float priceHi;
    @SerializedName("price4")
    private float priceLo;
    private float volume;

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
