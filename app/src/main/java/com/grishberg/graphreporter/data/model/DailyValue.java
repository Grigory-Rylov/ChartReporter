package com.grishberg.graphreporter.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by grishberg on 11.01.17.
 */

public class DailyValue {

    private int id;
    private long dt;
    @SerializedName("priceStart")
    private float priceStart;
    @SerializedName("priceEnd")
    private float priceEnd;
    @SerializedName("priceHi")
    private float priceHi;
    @SerializedName("priceLo")
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
