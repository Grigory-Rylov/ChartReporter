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
    private final double formulaValue;
    private final boolean isPercent;
    private final boolean isGreater;

    public FormulaContainer(final String name,
                            final VertexType vertexType,
                            final double formulaValue,
                            final boolean isPercent,
                            final boolean isGreater) {
        this.name = name;
        this.vertexType = vertexType;
        this.formulaValue = formulaValue;
        this.isPercent = isPercent;
        this.isGreater = isGreater;
    }

    public String getName() {
        return name;
    }

    public VertexType getVertexType() {
        return vertexType;
    }

    public double getFormulaValue() {
        return formulaValue;
    }

    public boolean isPercent() {
        return isPercent;
    }

    public boolean isGreater() {
        return isGreater;
    }
}
