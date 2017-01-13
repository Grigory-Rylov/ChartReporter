package com.grishberg.graphreporter.data.model;

import com.grishberg.datafacade.helpers.ExapndableItem;

/**
 * Created by grishberg on 01.01.17.
 */
public class CategoryItem implements ExapndableItem {
    private static final String TAG = CategoryItem.class.getSimpleName();
    private int id;
    private String name;
    private boolean isExpanded;

    @Override
    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public void setExpanded(boolean expaned) {
        this.isExpanded = expaned;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
