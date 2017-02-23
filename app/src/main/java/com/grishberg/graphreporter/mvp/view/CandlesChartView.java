package com.grishberg.graphreporter.mvp.view;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
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

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void showChart(DualChartContainer response, XAxisValueToDateFormatter dateFormatter);

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void formulaPoints(FormulaChartContainer response);

    void showPointInfo(float open, float high, float low, float close, String date);

    void hidePointInfo();

    void showPointInfo(float y, String date);

    void showSavingMessage();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void showFormulaSettingsScreen(long currentProductId);
}
