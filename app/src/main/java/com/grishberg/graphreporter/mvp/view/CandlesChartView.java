package com.grishberg.graphreporter.mvp.view;

import com.grishberg.graphreporter.data.model.DualChartContainer;
import com.grishberg.graphreporter.data.model.FormulaChartContainer;
import com.grishberg.graphreporter.mvp.common.BaseViewWithProgress;
import com.grishberg.graphreporter.utils.XAxisValueToDateFormatter;

/**
 * Created by grishberg on 01.01.17.
 * Интерфейс вида для отображения графиков
 */

public interface CandlesChartView extends BaseViewWithProgress {

    void showEmptyDataError();

    void showChart(DualChartContainer response, XAxisValueToDateFormatter dateFormatter);

    void formulaPoints(FormulaChartContainer response);

    void showPointInfo(float open, float high, float low, float close, String date);

    void hidePointInfo();

    void showPointInfo(float y, String date);

    void showSavingMessage();

    void showFormulaSettingsScreen(long currentProductId);
}
