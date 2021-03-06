package com.grishberg.graphreporter.data.storage;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.db.GreenDaoListResult;
import com.grishberg.graphreporter.data.beans.DailyValue;
import com.grishberg.graphreporter.data.beans.DailyValueDao;
import com.grishberg.graphreporter.utils.LogService;

import java.io.IOException;
import java.util.List;

import rx.Observable;

/**
 * Created by grishberg on 12.02.17.
 */
public class GreenDaoDataStorage implements DailyDataStorage {
    public static final String TAG = GreenDaoDataStorage.class.getSimpleName();
    private final DailyValueDao dailyValueDao;
    private final LogService logger;

    public GreenDaoDataStorage(final DailyValueDao dailyValueDao, final LogService logger) {
        this.dailyValueDao = dailyValueDao;
        this.logger = logger;
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
        final GreenDaoListResult<DailyValue> listResult = new GreenDaoListResult<>(dailyValueDao
                .queryBuilder()
                .where(DailyValueDao.Properties.ProductId.eq(productId))
                .where(DailyValueDao.Properties.Dt.gt(startDate))
                .orderAsc(DailyValueDao.Properties.Dt)
                .build());
        final Observable<ListResultCloseable<DailyValue>> observable = Observable.just(listResult);
        observable.doOnUnsubscribe(() -> {
            try {
                logger.d(TAG, "doOnUnsubscribe");
                listResult.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return observable;
    }
}
