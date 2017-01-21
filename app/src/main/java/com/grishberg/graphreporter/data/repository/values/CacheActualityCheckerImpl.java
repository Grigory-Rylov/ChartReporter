package com.grishberg.graphreporter.data.repository.values;

import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Created by grishberg on 21.01.17.
 */
public class CacheActualityCheckerImpl implements CacheActualityChecker {
    private final long timeout;

    @Nullable
    private Date lastDate;

    public CacheActualityCheckerImpl(final long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void updateNewData(final long id) {
        lastDate = new Date();
    }

    @Override
    public boolean isCacheDataValid(final long id) {
        if (lastDate == null || new Date().getTime() - lastDate.getTime() > timeout) {
            return false;
        }
        return true;
    }
}
