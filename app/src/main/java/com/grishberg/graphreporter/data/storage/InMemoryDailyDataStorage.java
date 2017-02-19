package com.grishberg.graphreporter.data.storage;

import com.grishberg.datafacade.ArrayListResult;
import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.repository.values.CacheActualityChecker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;

/**
 * Created by grishberg on 21.01.17.
 * Реализация хранилища данных в памяти
 */
public class InMemoryDailyDataStorage implements DailyDataStorage {
    private final Map<Long, ListResultCloseable<DailyValue>> cache;
    private final CacheActualityChecker cacheChecker;

    public InMemoryDailyDataStorage(final CacheActualityChecker cacheChecker) {
        cache = new ConcurrentHashMap<>();
        this.cacheChecker = cacheChecker;
    }

    @Override
    public void setDailyData(final long productId, final List<DailyValue> values) {
        ListResultCloseable<DailyValue> list = cache.get(productId);
        if (list != null) {
            list.clear();
            list.addAll(values);
        } else {
            list = ArrayListResult.fromList(values);
            cache.put(productId, list);
        }
        cacheChecker.updateNewData(productId);
    }

    @Override
    public void appendDailyData(final long productId, final List<DailyValue> values) {
        final ListResultCloseable<DailyValue> list = cache.get(productId);
        if (list != null) {
            list.addAll(0, values);
        }
        cacheChecker.updateNewData(productId);
    }

    @Override
    public Observable<ListResultCloseable<DailyValue>> getDailyValues(final long productId, final int offset) {
        if (cacheChecker.isCacheDataValid(productId)) {
            if (cache.get(productId).size() <= offset) {
                return Observable.just(null);
            }
            return Observable.just(cache.get(productId));
        }
        cache.remove(productId);
        return Observable.just(null);
    }
}
