package com.grishberg.graphreporter.mvp.presenter;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.enums.ChartMode;
import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.enums.ChartRange;
import com.grishberg.graphreporter.data.beans.DailyValue;
import com.grishberg.graphreporter.data.beans.DualChartContainer;
import com.grishberg.graphreporter.data.beans.FormulaChartContainer;
import com.grishberg.graphreporter.data.beans.FormulaContainer;
import com.grishberg.graphreporter.data.repository.exceptions.EmptyDataException;
import com.grishberg.graphreporter.data.repository.settings.SettingsDataSource;
import com.grishberg.graphreporter.data.repository.values.DailyDataRepository;
import com.grishberg.graphreporter.data.storage.FormulaDataSource;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.mvp.common.BasePresenter;
import com.grishberg.graphreporter.mvp.view.CandlesChartView;
import com.grishberg.graphreporter.utils.LogService;
import com.grishberg.graphreporter.utils.XAxisValueToDateFormatter;
import com.grishberg.graphreporter.utils.XAxisValueToDateFormatterImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

import static com.grishberg.graphreporter.data.enums.ChartMode.CANDLE_AND_LINE_MODE;

/**
 * Created by grishberg on 01.01.17.
 * Презентер для управления экраном отображения графика с японскими свечами
 */
@InjectViewState()
public class CandlesChartPresenter extends BasePresenter<CandlesChartView> implements Runnable {
    private static final int DURATION = 30 * 60 * 1000;
    private static final int FORMULA_CAPACITY = 5;
    private static final String TAG = CandlesChartPresenter.class.getSimpleName();
    private static final boolean PAGING_ENABLED = false;
    private final List<FormulaChartContainer> formulaChartArray;
    private final List<FormulaContainer> formulaArray;
    @Inject
    LogService log;
    @Inject
    DailyDataRepository repository;
    @Inject
    ChartsHelper chartsHelper;
    @Inject
    SettingsDataSource settings;
    @Inject
    FormulaDataSource formulaStorage;
    private ListResultCloseable<DailyValue> dailyListResultForClose;
    private XAxisValueToDateFormatter dateFormatter;
    //@Inject
    //TimerUtil timer;
    private ChartMode currentChartMode = ChartMode.CANDLE_MODE;
    private ChartRange currentChartRange = ChartRange.YEAR;
    private ChartPeriod period = ChartPeriod.DAY;
    private long currentProductId;
    private boolean isNeedShowFormula;
    private Subscription subscription;

    public CandlesChartPresenter() {
        DiManager.getAppComponent().inject(this);
        formulaChartArray = new ArrayList<>(FORMULA_CAPACITY);
        formulaArray = new ArrayList<>(FORMULA_CAPACITY);
        isNeedShowFormula = settings.isNeedShowFormula();
        log.d(TAG, "CandlesChartPresenter");
        //timer.setHandler(this);
    }

    public void onInitChartScreen(final long productId) {
        log.d(TAG, "onInitChartScreen");
        currentProductId = productId;
        currentChartMode = ChartMode.values()[settings.getChartType()];
        currentChartRange = ChartRange.values()[settings.getChartRange()];

        formulaStorage.getFormulas(currentProductId)
                .subscribe(formulas -> {
                    formulaArray.clear();
                    formulaArray.addAll(formulas);
                    requestDataFromRepositoryAndShow(currentProductId,
                            currentChartRange.getStartDate(),
                            currentChartMode,
                            period);
                });
    }

    public void onCandlesModeClicked() {
        currentChartMode = ChartMode.CANDLE_MODE;
        requestDataFromRepositoryAndShow(currentProductId,
                currentChartRange.getStartDate(),
                currentChartMode,
                period);
    }

    public void onLinesModeClicked() {
        currentChartMode = ChartMode.LINE_MODE;
        requestDataFromRepositoryAndShow(currentProductId,
                currentChartRange.getStartDate(),
                currentChartMode,
                period);
    }

    public void onCandlesAndLinesMode() {
        currentChartMode = CANDLE_AND_LINE_MODE;
        requestDataFromRepositoryAndShow(currentProductId,
                currentChartRange.getStartDate(),
                currentChartMode,
                period);
    }

    public void onPeriodChanged(final ChartPeriod period) {
        this.period = period;
        requestDataFromRepositoryAndShow(currentProductId,
                currentChartRange.getStartDate(),
                currentChartMode,
                period);
    }

    private void requestDataFromRepositoryAndShow(final long productId,
                                                  final long startDate,
                                                  final ChartMode chartMode,
                                                  final ChartPeriod period) {
        log.d(TAG, "requestDataFromRepositoryAndShow");
        getViewState().showProgress();
        subscription = repository.getValues(productId, startDate)
                .flatMap(dailyValues -> {
                    if (dailyValues == null || dailyValues.isEmpty()) {
                        return Observable.error(new EmptyDataException());
                    }
                    holdAndCloseListResult(dailyValues);
                    return getChartsPoints(chartMode, period, dailyValues);
                })
                .subscribe(dualChartContainer -> {
                            log.d(TAG, "requestDataFromRepositoryAndShow: success");
                            dateFormatter = new XAxisValueToDateFormatterImpl(extractDatesArray(dualChartContainer));
                            getViewState().hideProgress();
                            getViewState().showChart(dualChartContainer, dateFormatter);
                            for (final FormulaChartContainer currentFormula : formulaChartArray) {
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
                        }
                );
    }

    private void holdAndCloseListResult(ListResultCloseable<DailyValue> dailyValues) {
        if (dailyListResultForClose != null && !dailyListResultForClose.isClosed()) {
            dailyListResultForClose.silentClose();
        }
        dailyListResultForClose = dailyValues;
    }

    private void rebuildFormulaCharts(final ChartPeriod period,
                                      final ListResultCloseable<DailyValue> dailyValues) {
        formulaChartArray.clear();
        for (final FormulaContainer formulaContainer : formulaArray) {
            formulaChartArray.add(
                    chartsHelper.getFormulaDataForPeriod(period,
                            dailyValues,
                            formulaContainer
                    ));
        }
    }

    @NonNull
    private Observable<? extends DualChartContainer> getChartsPoints(final ChartMode chartMode,
                                                                     final ChartPeriod period,
                                                                     final ListResultCloseable<DailyValue> dailyValues) {
        rebuildFormulaCharts(period, dailyValues);
        switch (chartMode) {
            case CANDLE_MODE:

                final DualChartContainer value = DualChartContainer.makeCandle(period,
                        chartsHelper.getCandleDataForPeriod(period, dailyValues));
                dateFormatter = new XAxisValueToDateFormatterImpl(
                        extractDatesArray(value));
                return Observable.just(value);
            case LINE_MODE:
                return Observable.just(
                        DualChartContainer.makeLine(period,
                                chartsHelper.getLineData(period, dailyValues))
                );
            default:
                return Observable.just(
                        DualChartContainer.makeCandleAndLine(period,
                                chartsHelper.getLineData(period, dailyValues),
                                chartsHelper.getCandleDataForPeriod(period, dailyValues))
                );
        }
    }

    private List<Long> extractDatesArray(DualChartContainer value) {
        return value.getCandleResponse() != null ? value.getCandleResponse().getDates()
                : value.getEntryResponse().getDates();
    }

    @Override
    public void run() {
        //TODO: update from server
    }

    public void onChartValueSelected(final Entry entry) {
        if (entry instanceof CandleEntry) {
            getViewState().showPointInfo(
                    ((CandleEntry) entry).getOpen(),
                    ((CandleEntry) entry).getHigh(),
                    ((CandleEntry) entry).getLow(),
                    ((CandleEntry) entry).getClose(),
                    dateFormatter.getDateAsString(entry.getX()));
            return;
        }

        getViewState().showPointInfo(entry.getY(), dateFormatter.getDateAsString(entry.getX()));
    }

    public void onNothingSelected() {
        getViewState().hidePointInfo();
    }

    public void onSaveSettingsClicked() {
        settings.storeChartType(currentChartMode.ordinal());
        settings.storeShowFormulaState(isNeedShowFormula);
        settings.storeChartRange(currentChartRange.ordinal());
        getViewState().showSavingMessage();
    }

    public void onShowFormulaSettings() {
        getViewState().showFormulaSettingsScreen(currentProductId);
    }

    public void onFormulaSettingsClosed() {
        onInitChartScreen(currentProductId);
    }

    public void onCancelRequest() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            log.d(TAG, "unsubscribe");
            subscription.unsubscribe();
            subscription = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dailyListResultForClose != null) {
            dailyListResultForClose.silentClose();
            dailyListResultForClose = null;
        }
    }
}
