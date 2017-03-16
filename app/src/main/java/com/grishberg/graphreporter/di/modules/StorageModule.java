package com.grishberg.graphreporter.di.modules;

import android.content.Context;

import com.grishberg.graphreporter.data.beans.DaoSession;
import com.grishberg.graphreporter.data.repository.settings.SettingsDataSource;
import com.grishberg.graphreporter.data.repository.settings.SettingsPreferencesStorage;
import com.grishberg.graphreporter.data.repository.values.CacheActualityChecker;
import com.grishberg.graphreporter.data.repository.values.CacheActualityCheckerImpl;
import com.grishberg.graphreporter.data.storage.DailyDataStorage;
import com.grishberg.graphreporter.data.storage.FormulaDataSource;
import com.grishberg.graphreporter.data.storage.FormulaDataSourceImpl;
import com.grishberg.graphreporter.data.storage.GreenDaoDataStorage;
import com.grishberg.graphreporter.utils.LogService;

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
    DailyDataStorage provideContext(final DaoSession daoSession, final LogService logger) {
        return new GreenDaoDataStorage(daoSession.getDailyValueDao(), logger);
    }

    @Provides
    @Singleton
    SettingsDataSource provideSettingsStorage(final Context context) {
        return new SettingsPreferencesStorage(context);
    }

    @Provides
    @Singleton
    FormulaDataSource provideFormulasStorage(final DaoSession daoSession) {
        return new FormulaDataSourceImpl(daoSession.getFormulaContainerDao());
    }
}
