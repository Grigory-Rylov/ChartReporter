package com.grishberg.graphreporter.data.repository.values;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.db.GreenDaoListResult;
import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.model.DailyValueDao;

import java.util.List;

import rx.Observable;

/**
 * Created by grishberg on 12.02.17.
 */
public class GreenDaoDataStorage implements DailyDataStorage {
    private final CacheActualityChecker cacheChecker;
    private final DailyValueDao dailyValueDao;

    public GreenDaoDataStorage(final CacheActualityChecker cacheChecker,
                               final DailyValueDao dailyValueDao) {
        this.cacheChecker = cacheChecker;
        this.dailyValueDao = dailyValueDao;
    }

    @Override
    public void setDailyData(final long productId, final List<DailyValue> values) {
        for (final DailyValue value : values) {
            value.setProductId(productId);
            dailyValueDao.insertOrReplaceInTx(value);
        }
        cacheChecker.updateNewData(productId);
    }

    @Override
    public void appendDailyData(final long productId, final List<DailyValue> values) {
        for (final DailyValue value : values) {
            value.setProductId(productId);
            dailyValueDao.insertOrReplaceInTx(value);
        }
        cacheChecker.updateNewData(productId);
    }

    @Override
    public Observable<ListResultCloseable<DailyValue>> getDailyValues(final long productId,
                                                                      final int offset) {
        if (cacheChecker.isCacheDataValid(productId)) {
            return Observable.just(
                    new GreenDaoListResult<DailyValue>(dailyValueDao
                            .queryBuilder()
                            .orderAsc(DailyValueDao.Properties.Dt)
                            .build())
            );
        }
        return Observable.just(null);
    }
}
