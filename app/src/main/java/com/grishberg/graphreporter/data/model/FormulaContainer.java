package com.grishberg.graphreporter.data.model;

import java.io.Serializable;

/**
 * Created by grishberg on 29.01.17.
 * Контейнер для хранения формулы
 */
public class FormulaContainer implements Serializable {
    private final String name;
    private final VertexType vertexType;
    private final double growValue;
    private final double fallValue;
    private final boolean isGrowPercent;
    private final boolean isFallPercent;
    private final int growColor;
    private final int fallColor;
    public FormulaContainer(final String name,
                            final VertexType vertexType,
                            final double growValue,
                            final boolean isGrowPercent,
                            final int growColor,
                            final double fallValue,
                            final boolean isFallPercent,
                            final int fallColor) {
        this.name = name;
        this.vertexType = vertexType;
        this.growValue = growValue;
        this.fallValue = fallValue;
        this.isGrowPercent = isGrowPercent;
        this.isFallPercent = isFallPercent;
        this.growColor = growColor;
        this.fallColor = fallColor;
    }

    public String getName() {
        return name;
    }

    public VertexType getVertexType() {
        return vertexType;
    }

    public double getGrowValue() {
        return growValue;
    }

    public double getFallValue() {
        return fallValue;
    }

    public boolean isGrowPercent() {
        return isGrowPercent;
    }

    public boolean isFallPercent() {
        return isFallPercent;
    }

    public int getGrowColor() {
        return growColor;
    }

    public int getFallColor() {
        return fallColor;
    }

    public enum VertexType {
        OPEN,
        CLOSED,
        HIGH,
        LOW
    }
}
