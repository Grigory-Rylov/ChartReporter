package com.grishberg.graphreporter.data.repository.values;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.beans.DailyValue;

import rx.Observable;

/**
 * Created by grishberg on 15.01.17.
 * Интерфейс репозитория дневных данных
 */

public interface DailyDataRepository {
    Observable<ListResultCloseable<DailyValue>> getValues(long productId, long startDate);
}
