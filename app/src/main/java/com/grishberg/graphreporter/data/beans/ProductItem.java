package com.grishberg.graphreporter.data.beans;

import java.io.Serializable;

/**
 * Created by grishberg on 01.01.17.
 */
public class ProductItem implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
