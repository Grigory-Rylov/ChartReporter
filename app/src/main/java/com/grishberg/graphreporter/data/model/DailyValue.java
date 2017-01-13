package com.grishberg.graphreporter.data.model;

import java.util.Date;

/**
 * Created by grishberg on 11.01.17.
 */

public class DailyValue {
    private static final String TAG = DailyValue.class.getSimpleName();

    private int id;
    private Date dt;
    private double price1;
    private double price2;
    private double price3;
    private double price4;
    private double volume;

    public int getId() {
        return id;
    }

    public Date getDt() {
        return dt;
    }

    public double getPrice1() {
        return price1;
    }

    public double getPrice2() {
        return price2;
    }

    public double getPrice3() {
        return price3;
    }

    public double getPrice4() {
        return price4;
    }

    public double getVolume() {
        return volume;
    }
}
