package com.grishberg.graphreporter.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.CombinedChart;

/**
 * Created by grishberg on 19.01.17.
 * Комбинированный график с инициализацией
 */
public class CombinedChartInitiable extends CombinedChart {
    public CombinedChartInitiable(final Context context) {
        super(context);
    }

    public CombinedChartInitiable(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public CombinedChartInitiable(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void init() {
        super.init();
        mRenderer = new CombinedChartRendererEx(this, mAnimator, mViewPortHandler);
    }
}
