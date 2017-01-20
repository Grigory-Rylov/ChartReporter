package com.grishberg.graphreporter.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.grishberg.graphreporter.data.enums.ChartMode;
import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.model.DualChartContainer;
import com.grishberg.graphreporter.data.repository.values.DailyDataRepository;
import com.grishberg.graphreporter.data.repository.exceptions.EmptyDataException;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.mvp.common.BasePresenter;
import com.grishberg.graphreporter.mvp.view.CandlesChartView;
import com.grishberg.graphreporter.utils.LogService;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by grishberg on 01.01.17.
 * Презентер для управления экраном отображения графика с японскими свечами
 */
@InjectViewState
public class CandlesChartPresenter extends BasePresenter<CandlesChartView> {
    private static final String TAG = CandlesChartPresenter.class.getSimpleName();

    @Inject
    LogService log;
    @Inject
    DailyDataRepository repository;

    public CandlesChartPresenter() {
        DiManager.getAppComponent().inject(this);
    }

    /**
     * Пересчитать точки в зависимости от периода
     *
     * @param productId -  идентификатор продукта
     * @param period    -  индекс периода
     */
    public void recalculatePeriod(final long productId,
                                  final ChartMode chartMode,
                                  final ChartPeriod period) {
        getViewState().showProgress();
        repository.getDailyValues(productId)
                .flatMap(dailyValues -> {
                    if (dailyValues.isEmpty()) {
                        return Observable.error(new EmptyDataException());
                    }
                    switch (chartMode) {
                        case CHART_MODE:
                            return Observable.just(
                                    DualChartContainer.makeCandle(period, ChartsHelper.getCandleDataForPeriod(period, dailyValues, false))
                            );
                        case LINE_MODE:
                            return Observable.just(
                                    DualChartContainer.makeLine(period, ChartsHelper.getLineData(period, dailyValues, false))
                            );
                        default:
                            return Observable.just(
                                    DualChartContainer.makeCandleAndLine(period, ChartsHelper.getLineData(period, dailyValues, true),
                                            ChartsHelper.getCandleDataForPeriod(period, dailyValues, true))
                            );
                    }
                })
                .subscribe(response -> {
                    getViewState().hideProgress();
                    getViewState().showChart(response);
                }, exception -> {
                    getViewState().hideProgress();
                    if (exception instanceof EmptyDataException) {
                        getViewState().showEmptyDataError();
                        return;
                    }
                    getViewState().showFail(exception.getMessage());
                    log.e(TAG, "requestDailyValues: ", exception);
                });
    }
}
