package com.grishberg.graphreporter.data.model;

import java.util.Date;

import lombok.Getter;

/**
 * Created by grishberg on 11.01.17.
 */
@Getter
public class DailyValue {
    private static final String TAG = DailyValue.class.getSimpleName();

    private int id;
    private Date dt;
    private double price1;
    private double price2;
    private double price3;
    private double price4;
    private double volume;
}
