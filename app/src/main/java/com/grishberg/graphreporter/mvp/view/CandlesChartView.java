package com.grishberg.graphreporter.mvp.view;

import com.github.mikephil.charting.data.CandleEntry;
import com.grishberg.graphreporter.data.model.ChartResponseContainer;
import com.grishberg.graphreporter.mvp.common.BaseViewWithProgress;

/**
 * Created by grishberg on 01.01.17.
 * Интерфейс вида для отображения графиков
 */

public interface CandlesChartView extends BaseViewWithProgress {
    void showChart(ChartResponseContainer<CandleEntry> values);

    void showEmptyDataError();
}
