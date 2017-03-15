package com.grishberg.graphreporter.data.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by grishberg on 29.01.17.
 * Контейнер для хранения формулы
 */
@Entity(indexes = {
        @Index(value = "productId ASC", unique = false)
})
public class FormulaContainer implements Serializable {
    static final long serialVersionUID = 112345L;
    private long productId;
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private int vertexType;
    private double growValue;
    private double fallValue;
    private boolean isGrowPercent;
    private boolean isFallPercent;
    private int growColor;
    private int fallColor;
    private boolean isVisible;

    public FormulaContainer(final String name,
                            final int vertexType,
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

    @Generated(hash = 1242474684)
    public FormulaContainer(long productId, Long id, String name, int vertexType, double growValue,
                            double fallValue, boolean isGrowPercent, boolean isFallPercent, int growColor, int fallColor,
                            boolean isVisible) {
        this.productId = productId;
        this.id = id;
        this.name = name;
        this.vertexType = vertexType;
        this.growValue = growValue;
        this.fallValue = fallValue;
        this.isGrowPercent = isGrowPercent;
        this.isFallPercent = isFallPercent;
        this.growColor = growColor;
        this.fallColor = fallColor;
        this.isVisible = isVisible;
    }

    @Generated(hash = 260664154)
    public FormulaContainer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVertexType() {
        return vertexType;
    }

    public void setVertexType(int vertexType) {
        this.vertexType = vertexType;
    }

    public double getGrowValue() {
        return growValue;
    }

    public void setGrowValue(double growValue) {
        this.growValue = growValue;
    }

    public double getFallValue() {
        return fallValue;
    }

    public void setFallValue(double fallValue) {
        this.fallValue = fallValue;
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

    public void setGrowColor(int growColor) {
        this.growColor = growColor;
    }

    public int getFallColor() {
        return fallColor;
    }

    public void setFallColor(int fallColor) {
        this.fallColor = fallColor;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsGrowPercent() {
        return this.isGrowPercent;
    }

    public void setIsGrowPercent(boolean isGrowPercent) {
        this.isGrowPercent = isGrowPercent;
    }

    public boolean getIsFallPercent() {
        return this.isFallPercent;
    }

    public void setIsFallPercent(boolean isFallPercent) {
        this.isFallPercent = isFallPercent;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean getIsVisible() {
        return this.isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
}
