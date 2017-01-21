package com.grishberg.graphreporter.data.repository.values;

/**
 * Created by grishberg on 20.01.17.
 * Интерфейс для валидации кэша
 */
public interface CacheActualityChecker {
    void updateNewData(long id);

    boolean isCacheDataValid(long id);
}
