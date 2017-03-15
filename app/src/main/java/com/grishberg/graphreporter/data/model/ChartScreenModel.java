package com.grishberg.graphreporter.data.model;

import android.support.annotation.Nullable;

import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.enums.ChartRange;

/**
 * Created on 13.03.17.
 *
 * @author g
 */
public class ChartScreenModel {
    @Nullable
    private OnChartRangeSelectedListener rangeSelectedListener;
    @Nullable
    private OnPeriodSelectedListener periodSelectedListener;

    public void selectPeriod(ChartPeriod period) {
        if (periodSelectedListener != null) {
            periodSelectedListener.onPeriodSelected(period);
        }
    }

    public void setRangeSelectedListener(ChartRange range) {
        if (rangeSelectedListener != null) {
            rangeSelectedListener.onRangeSelected(range);
        }
    }

    public interface OnChartRangeSelectedListener {
        void onRangeSelected(ChartRange range);
    }

    public interface OnPeriodSelectedListener {
        void onPeriodSelected(ChartPeriod period);
    }
}
