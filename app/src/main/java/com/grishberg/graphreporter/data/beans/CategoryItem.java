package com.grishberg.graphreporter.data.beans;

import com.grishberg.datafacade.helpers.ExapndableItem;

/**
 * Created by grishberg on 01.01.17.
 * Модель данных для категорий, на данный момент категорий нет
 */
public class CategoryItem implements ExapndableItem {
    private int id;
    private String name;
    private boolean isExpanded;

    @Override
    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public void setExpanded(final boolean expaned) {
        this.isExpanded = expaned;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
