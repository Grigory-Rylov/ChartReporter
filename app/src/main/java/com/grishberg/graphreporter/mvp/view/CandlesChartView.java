package com.grishberg.graphreporter.mvp.view;

import com.grishberg.graphreporter.data.model.ChartResponseContainer;
import com.grishberg.graphreporter.mvp.common.BaseViewWithProgress;

/**
 * Created by grishberg on 01.01.17.
 */

public interface CandlesChartView extends BaseViewWithProgress {
    void showChart(ChartResponseContainer values);

    void showEmptyDataError();
}
