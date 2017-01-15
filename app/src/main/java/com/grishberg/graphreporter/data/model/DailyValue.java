package com.grishberg.graphreporter.data.model;

/**
 * Created by grishberg on 11.01.17.
 */

public class DailyValue {

    private int id;
    private long dt;
    private float price1;
    private float price2;
    private float price3;
    private float price4;
    private float volume;

    public int getId() {
        return id;
    }

    public long getDt() {
        return dt;
    }

    public float getPrice1() {
        return price1;
    }

    public float getPrice2() {
        return price2;
    }

    public float getPrice3() {
        return price3;
    }

    public float getPrice4() {
        return price4;
    }

    public float getVolume() {
        return volume;
    }
}
