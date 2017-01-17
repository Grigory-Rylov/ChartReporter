package com.grishberg.graphreporter.mvp.presenter;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.github.mikephil.charting.data.CandleEntry;
import com.grishberg.graphreporter.data.model.ChartPeriod;
import com.grishberg.graphreporter.data.model.ChartResponseContainer;
import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.repository.DailyDataRepository;
import com.grishberg.graphreporter.data.repository.exceptions.EmptyDataException;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.mvp.common.BasePresenter;
import com.grishberg.graphreporter.mvp.view.CandlesChartView;
import com.grishberg.graphreporter.utils.LogService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static com.grishberg.graphreporter.data.model.ChartPeriod.DAY;

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

    public void requestDailyValues(final long productId) {
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
                                element.getPriceHi(),
                                element.getPriceLo(),
                                element.getPriceStart(),
                                element.getPriceEnd()));
                        dates.add(element.getDt() * 1000L);
                    }
                    return Observable.just(new ChartResponseContainer(entries, dates, DAY));
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

    /**
     * Пересчитать точки в зависимости от периода
     *
     * @param productId -  идентификатор продукта
     * @param period    -  индекс периода
     */
    public void recalculatePeriod(final long productId, final ChartPeriod period) {
        getViewState().showProgress();
        repository.getDailyValues(productId)
                .flatMap(dailyValues -> {
                    if (dailyValues.isEmpty()) {
                        return Observable.error(new EmptyDataException());
                    }
                    return Observable.just(convertResponsePeriod(period, dailyValues));
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

    @NonNull
    private ChartResponseContainer convertResponsePeriod(final ChartPeriod period, final List<DailyValue> dailyValues) {
        final List<Long> dates = new ArrayList<>();
        final List<CandleEntry> entries = new ArrayList<>();
        final int periodPartionCount = period.getPartion();
        long currentDt = 0;
        int pos = 0;
        final int size = dailyValues.size();
        int periodCount = 0;
        while (pos < size) {
            float hi = 0;
            float lo = Float.MAX_VALUE;
            float start = 0;
            float end = 0;
            int i;
            for (i = 0; i < periodPartionCount && pos < size; i++, pos++) {
                final DailyValue element = dailyValues.get(pos);
                hi = Math.max(element.getPriceHi(), hi);
                lo = Math.min(element.getPriceLo(), lo);
                if (i == 0) {
                    start = element.getPriceStart();
                }
                if ((i == periodPartionCount - 1) || (pos == size - 1)) {
                    end = element.getPriceEnd();
                }
                currentDt = element.getDt() * 1000L;
            }
            dates.add(currentDt);
            entries.add(new CandleEntry(periodCount++, hi, lo, start, end));
        }

        return new ChartResponseContainer(entries, dates, period);
    }
}
