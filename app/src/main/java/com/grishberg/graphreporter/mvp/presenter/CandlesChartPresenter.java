package com.grishberg.graphreporter.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.grishberg.graphreporter.data.enums.ChartMode;
import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.model.DualChartContainer;
import com.grishberg.graphreporter.data.model.FormulaChartContainer;
import com.grishberg.graphreporter.data.model.FormulaContainer;
import com.grishberg.graphreporter.data.repository.values.DailyDataRepository;
import com.grishberg.graphreporter.data.repository.exceptions.EmptyDataException;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.mvp.common.BasePresenter;
import com.grishberg.graphreporter.mvp.view.CandlesChartView;
import com.grishberg.graphreporter.utils.LogService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static com.grishberg.graphreporter.data.enums.ChartMode.CANDLE_AND_LINE_MODE;

/**
 * Created by grishberg on 01.01.17.
 * Презентер для управления экраном отображения графика с японскими свечами
 */
@InjectViewState
public class CandlesChartPresenter extends BasePresenter<CandlesChartView> implements Runnable {
    private static final String TAG = CandlesChartPresenter.class.getSimpleName();
    public static final int DURATION = 30 * 60 * 1000;
    private static final int INITIAL_OFFSET = 0;

    private final List<FormulaChartContainer> formulaArray;

    @Inject
    LogService log;
    @Inject
    DailyDataRepository repository;
    private int maxOffset;
    //@Inject
    //TimerUtil timer;
    private ChartMode currentChartMode = ChartMode.CANDLE_MODE;
    private ChartPeriod period = ChartPeriod.DAY;
    private long currentProductId;

    public CandlesChartPresenter() {
        DiManager.getAppComponent().inject(this);
        formulaArray = new ArrayList<>();
        //timer.setHandler(this);
    }

    /**
     * Пересчитать точки в зависимости от периода
     *
     * @param productId -  идентификатор продукта
     */
    public void onSelectedProduct(final long productId) {
        currentProductId = productId;
        getViewState().showProgress();
        getDataFromOffset(productId, INITIAL_OFFSET, currentChartMode, period);
    }

    public void onCandlesModeClicked() {
        currentChartMode = ChartMode.CANDLE_MODE;
        onSelectedProduct(currentProductId);
    }

    public void onLinesModeClicked() {
        currentChartMode = ChartMode.LINE_MODE;
        onSelectedProduct(currentProductId);
    }

    public void onCandlesAndLinesMode() {
        currentChartMode = CANDLE_AND_LINE_MODE;
        onSelectedProduct(currentProductId);
    }

    /**
     * Смена периода
     *
     * @param period новый период
     */
    public void onPeriodChanged(final ChartPeriod period) {
        this.period = period;
        onSelectedProduct(currentProductId);
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
                            case CANDLE_MODE:
                                return Observable.just(
                                        DualChartContainer.makeCandle(period,
                                                ChartsHelper.getCandleDataForPeriod(period, dailyValues, false))
                                );
                            case LINE_MODE:
                                return Observable.just(
                                        DualChartContainer.makeLine(period,
                                                ChartsHelper.getLineData(period, dailyValues, false))
                                );
                            default:
                                return Observable.just(
                                        DualChartContainer.makeCandleAndLine(period,
                                                ChartsHelper.getLineData(period, dailyValues, true),
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
                    for (final FormulaChartContainer currentFormula : formulaArray) {
                        getViewState().formulaPoints(currentFormula);
                    }
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

    /**
     * Применить формулу
     *
     * @param formulaContainer
     */
    public void addNewFormula(final FormulaContainer formulaContainer) {
        requestPointForFormula(currentProductId, formulaContainer);
    }

    private void requestPointForFormula(final long productId, final FormulaContainer formulaContainer) {
        repository.getDailyValues(productId, 0)
                .flatMap(dailyValues -> {
                    if (dailyValues.isEmpty()) {
                        return Observable.error(new EmptyDataException());
                    }
                    return Observable.just(
                            ChartsHelper.getFormulaDataForPeriod(period,
                                    dailyValues,
                                    formulaContainer)
                    );
                })
                .subscribe(response -> {
                    getViewState().hideProgress();
                    if (!response.getFallPoints().isEmpty() && response.getGrowPoints().isEmpty()) {
                        formulaArray.add(response);
                        getViewState().formulaPoints(response);
                    }
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
