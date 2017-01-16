package com.grishberg.graphreporter.data.repository;

import com.grishberg.graphreporter.data.model.DailyValue;

import java.util.List;

import rx.Observable;

/**
 * Created by grishberg on 15.01.17.
 */

public interface DailyDataRepository {
    Observable<List<DailyValue>> getDailyValues(long productId);

    Observable<List<DailyValue>> getDetailValues(long productId);
}
