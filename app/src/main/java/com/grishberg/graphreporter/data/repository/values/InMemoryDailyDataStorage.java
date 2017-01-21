package com.grishberg.graphreporter.data.repository.values;

import android.util.SparseArray;

import com.grishberg.graphreporter.data.model.DailyValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;

/**
 * Created by grishberg on 21.01.17.
 * Реализация хранилища данных в памяти
 */
public class InMemoryDailyDataStorage implements DailyDataStorage {
    private final Map<Long, List<DailyValue>> cache;
    private final CacheActualityChecker cacheChecker;

    public InMemoryDailyDataStorage(final CacheActualityChecker cacheChecker) {
        cache = new ConcurrentHashMap<>();
        this.cacheChecker = cacheChecker;
    }

    @Override
    public void setDailyData(final long productId, final List<DailyValue> values) {
        List<DailyValue> list = cache.get(productId);
        if (list != null) {
            cache.clear();
        } else {
            list = new ArrayList<>(300);
            cache.put(productId, list);
        }
        list.addAll(values);
        cacheChecker.updateNewData(productId);
    }

    @Override
    public Observable<List<DailyValue>> getDailyValues(final long productId) {
        if (cacheChecker.isCacheDataValid(productId)) {
            return Observable.just(cache.get(productId));
        }
        cache.remove(productId);
        return Observable.just(null);
    }
}
