package com.grishberg.graphreporter.data.repository.values;

import com.grishberg.graphreporter.data.model.DailyValue;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by grishberg on 21.01.17.
 * Реализация хранилища данных в памяти
 */
public class InMemoryDailyDataStorage implements DailyDataStorage {
    private final ArrayList<DailyValue> cache;
    private final CacheActualityChecker cacheChecker;

    public InMemoryDailyDataStorage(final CacheActualityChecker cacheChecker) {
        cache = new ArrayList<>();
        this.cacheChecker = cacheChecker;
    }

    @Override
    public void setDailyData(final long productId, final List<DailyValue> values) {
        cache.clear();
        cache.addAll(values);
        cacheChecker.updateNewData(productId);
    }

    @Override
    public Observable<List<DailyValue>> getDailyValues(final long productId) {
        if (cacheChecker.isCacheDataValid(productId)) {
            return Observable.just(cache);
        }
        return Observable.just(null);
    }
}
