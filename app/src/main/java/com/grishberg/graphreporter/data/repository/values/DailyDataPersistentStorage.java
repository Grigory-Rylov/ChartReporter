package com.grishberg.graphreporter.data.repository.values;

import com.grishberg.graphreporter.data.model.DailyValue;

import java.util.List;

import rx.Observable;

/**
 * Created by grishberg on 20.01.17.
 * Кэщ дневных данных
 */
public class DailyDataPersistentStorage implements DailyDataRepository {

    @Override
    public Observable<List<DailyValue>> getDailyValues(final long productId) {
        return null;
    }
}
