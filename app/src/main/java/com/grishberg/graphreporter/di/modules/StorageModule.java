package com.grishberg.graphreporter.di.modules;

import com.grishberg.graphreporter.data.repository.values.CacheActualityChecker;
import com.grishberg.graphreporter.data.repository.values.CacheActualityCheckerImpl;
import com.grishberg.graphreporter.data.repository.values.DailyDataStorage;
import com.grishberg.graphreporter.data.repository.values.InMemoryDailyDataStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by grishberg on 12.01.17.
 */
@Module
public class StorageModule {

    public static final int TIMEOUT = 10 * 3600 * 1000;

    @Provides
    @Singleton
    CacheActualityChecker provideCacheChecker() {
        return new CacheActualityCheckerImpl(TIMEOUT);
    }

    @Provides
    @Singleton
    DailyDataStorage provideContext(final CacheActualityChecker cacheChecker) {
        return new InMemoryDailyDataStorage(cacheChecker);
    }
}
