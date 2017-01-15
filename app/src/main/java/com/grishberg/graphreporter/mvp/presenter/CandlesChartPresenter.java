package com.grishberg.graphreporter.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.github.mikephil.charting.data.CandleEntry;
import com.grishberg.graphreporter.App;
import com.grishberg.graphreporter.data.model.ChartResponseContainer;
import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.repository.DailyDataRepository;
import com.grishberg.graphreporter.data.repository.exceptions.EmptyDataException;
import com.grishberg.graphreporter.mvp.common.BasePresenter;
import com.grishberg.graphreporter.mvp.view.CandlesChartView;
import com.grishberg.graphreporter.utils.LogService;

import java.util.ArrayList;
import java.util.List;

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
        App.getAppComponent().inject(this);
    }

    public void requestDailyValues(final int productId) {
        getViewState().showProgress();
        repository.getDailyValues(productId)
                .flatMap(dailyValues -> {
                    if (dailyValues.isEmpty()) {
                        return Observable.error(new EmptyDataException());
                    }
                    final List<Long> dates = new ArrayList<>();
                    final List<CandleEntry> entries = new ArrayList<>();
                    for (int i = 0, len = dailyValues.size(); i < len; i++) {
                        final DailyValue element = dailyValues.get(i);
                        entries.add(new CandleEntry(i,
                                element.getPrice3(),
                                element.getPrice4(),
                                element.getPrice1(),
                                element.getPrice2()));
                        dates.add(element.getDt() * 1000L);
                    }
                    return Observable.just(new ChartResponseContainer(entries, dates));
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
