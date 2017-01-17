package com.grishberg.graphreporter.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.CandleStickChart;

/**
 * Created by grishberg on 17.01.17.
 * Наследник графика свечей с возможности инициализации
 */
public class CandleStickChartInitiable extends CandleStickChart {

    public CandleStickChartInitiable(final Context context) {
        super(context);
    }

    public CandleStickChartInitiable(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public CandleStickChartInitiable(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void init() {
        super.init();
    }
}
