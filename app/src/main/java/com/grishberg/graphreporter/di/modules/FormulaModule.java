package com.grishberg.graphreporter.di.modules;

import com.grishberg.graphreporter.mvp.presenter.DailyValueToGraphEntryConverter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by grishberg on 04.02.17.
 * Модуль Dagger для формирования точек формулы
 */
@Module
public class FormulaModule {
    @Provides
    @Singleton
    DailyValueToGraphEntryConverter provideChartHelper() {
        return new DailyValueToGraphEntryConverter();
    }
}
