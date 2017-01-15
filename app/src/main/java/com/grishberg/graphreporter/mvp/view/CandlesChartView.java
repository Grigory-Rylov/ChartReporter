package com.grishberg.graphreporter.mvp.view;

import com.github.mikephil.charting.data.CandleEntry;
import com.grishberg.graphreporter.mvp.common.BaseView;

import java.util.List;

/**
 * Created by grishberg on 01.01.17.
 */

public interface CandlesChartView extends BaseView {
    void showChart(List<CandleEntry> values);
}
