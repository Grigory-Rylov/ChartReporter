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
import com.grishberg.graphreporter.utils.TimerUtil;

import java.io.IOException;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by grishberg on 01.01.17.
 * Презентер для управления экраном отображения графика с японскими свечами
 */
@InjectViewState
public class CandlesChartPresenter extends BasePresenter<CandlesChartView> implements Runnable {
    private static final String TAG = CandlesChartPresenter.class.getSimpleName();
    public static final int DURATION = 30 * 60 * 1000;
    public static final int INITIAL_OFFSET = 0;

    @Inject
    LogService log;
    @Inject
    DailyDataRepository repository;
    private int maxOffset;
    //@Inject
    //TimerUtil timer;

    public CandlesChartPresenter() {
        DiManager.getAppComponent().inject(this);
        //timer.setHandler(this);
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
        getDataFromOffset(productId, INITIAL_OFFSET, chartMode, period);
    }

    private void getDataFromOffset(final long productId,
                                   final int initialOffset,
                                   final ChartMode chartMode,
                                   final ChartPeriod period) {
        repository.getDailyValues(productId, initialOffset)
                .flatMap(dailyValues -> {
                    if (dailyValues.isEmpty()) {
                        return Observable.error(new EmptyDataException());
                    }
                    maxOffset = dailyValues.size();
                    try {
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
                    } finally {
                        try {
                            dailyValues.close();
                        } catch (final IOException e) {
                            log.e(TAG, "close exception", e);
                        }
                        //timer.startTimer(DURATION);
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

    @Override
    public void run() {
        //TODO: update from server
    }
}
