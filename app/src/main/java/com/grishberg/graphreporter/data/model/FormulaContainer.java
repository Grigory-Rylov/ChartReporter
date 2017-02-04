package com.grishberg.graphreporter.data.model;

import java.io.Serializable;

/**
 * Created by grishberg on 29.01.17.
 * Контейнер для хранения формулы
 */
public class FormulaContainer implements Serializable {
    public enum VertexType {
        OPEN,
        CLOSED,
        HIGH,
        LOW
    }

    private final String name;
    private final VertexType vertexType;
    private final double growValue;
    private final double fallValue;
    private final boolean isGrowPercent;
    private final boolean isFallPercent;

    public FormulaContainer(final String name,
                            final VertexType vertexType,
                            final double growValue,
                            final boolean isGrowPercent,
                            final double fallValue,
                            final boolean isFallPercent) {
        this.name = name;
        this.vertexType = vertexType;
        this.growValue = growValue;
        this.fallValue = fallValue;
        this.isGrowPercent = isGrowPercent;
        this.isFallPercent = isFallPercent;
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
}
