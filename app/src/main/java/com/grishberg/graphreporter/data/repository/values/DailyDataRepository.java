package com.grishberg.graphreporter.data.repository.values;

import com.grishberg.graphreporter.data.model.DailyValue;

import java.util.List;

import rx.Observable;

/**
 * Created by grishberg on 15.01.17.
 * Интерфейс репозитория дневных данных
 */

public interface DailyDataRepository {
    Observable<List<DailyValue>> getDailyValues(long productId);
}
