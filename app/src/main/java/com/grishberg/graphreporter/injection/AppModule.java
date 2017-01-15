package com.grishberg.graphreporter.injection;

import android.content.Context;
import android.content.res.Resources;

import com.grishberg.graphreporter.App;
import com.grishberg.graphreporter.utils.LogService;
import com.grishberg.graphreporter.utils.LogServiceImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by grishberg on 12.01.17.
 */
@Module
public class AppModule {
    private final App app;

    public AppModule(final App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    Resources provideResources() {
        return app.getResources();
    }

    @Provides
    @Singleton
    LogService provideLogService() {
        return new LogServiceImpl();
    }
}
