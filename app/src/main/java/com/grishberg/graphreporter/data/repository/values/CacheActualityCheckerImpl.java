package com.grishberg.graphreporter.data.repository.values;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by grishberg on 21.01.17.
 */
public class CacheActualityCheckerImpl implements CacheActualityChecker {
    public static final int INITIAL_CAPACITY = 16;
    private final long timeout;

    private final Map<Long, Date> lastDate;

    public CacheActualityCheckerImpl(final long timeout) {
        this.timeout = timeout;
        lastDate = new ConcurrentHashMap<>(INITIAL_CAPACITY);
    }

    @Override
    public void updateNewData(final long id) {
        lastDate.put(id, new Date());
    }

    @Override
    public boolean isCacheDataValid(final long id) {
        if (lastDate.get(id) == null || new Date().getTime() - lastDate.get(id).getTime() > timeout) {
            return false;
        }
        return true;
    }
}
