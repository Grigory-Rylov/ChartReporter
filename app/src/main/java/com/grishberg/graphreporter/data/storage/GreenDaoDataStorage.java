package com.grishberg.graphreporter.data.storage;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.db.GreenDaoListResult;
import com.grishberg.graphreporter.data.beans.DailyValue;
import com.grishberg.graphreporter.data.beans.DailyValueDao;

import java.util.List;

import rx.Observable;

/**
 * Created by grishberg on 12.02.17.
 */
public class GreenDaoDataStorage implements DailyDataStorage {
    private final DailyValueDao dailyValueDao;

    public GreenDaoDataStorage(final DailyValueDao dailyValueDao) {
        this.dailyValueDao = dailyValueDao;
    }

    @Override
    public void appendData(final long productId, final List<DailyValue> values) {
        for (final DailyValue value : values) {
            value.setProductId(productId);
            dailyValueDao.insertOrReplaceInTx(value);
        }
    }

    @Override
    public Observable<ListResultCloseable<DailyValue>> getValues(final long productId,
                                                                 final long startDate) {
        return Observable.just(
                new GreenDaoListResult<>(dailyValueDao
                        .queryBuilder()
                        .where(DailyValueDao.Properties.ProductId.eq(productId))
                        .where(DailyValueDao.Properties.Dt.gt(startDate))
                        .orderAsc(DailyValueDao.Properties.Dt)
                        .build())
        );
    }
}
