package com.grishberg.graphreporter.data.repository.values;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.model.DailyValue;

/**
 * Created by grishberg on 20.01.17.
 * Интерфейс для валидации кэша
 */
@FunctionalInterface
public interface CacheActualityChecker {
    boolean isCacheDataValid(ListResultCloseable<DailyValue> cachedResult);
}
