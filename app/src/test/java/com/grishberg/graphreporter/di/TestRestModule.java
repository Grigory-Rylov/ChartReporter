package com.grishberg.graphreporter.di;

import com.grishberg.graphreporter.data.repository.AuthRepository;
import com.grishberg.graphreporter.data.repository.AuthTokenRepository;
import com.grishberg.graphreporter.data.repository.DailyDataRepository;
import com.grishberg.graphreporter.data.rest.Api;
import com.grishberg.graphreporter.utils.DebugLogger;
import com.grishberg.graphreporter.utils.LogService;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by grishberg on 15.01.17.
 */
@Module
public class TestRestModule {
    private static final String TAG = TestRestModule.class.getSimpleName();

    private final AuthTokenRepository authTokenRepository;
    private final Api api;

    public TestRestModule(final AuthTokenRepository authTokenRepository, final Api api) {
        this.authTokenRepository = authTokenRepository;
        this.api = api;
    }

    @Provides
    @Singleton
    AuthRepository provideAuthRepository() {
        return Mockito.mock(AuthRepository.class);
    }

    @Provides
    @Singleton
    LogService provideLogService() {
        return new DebugLogger();
    }

    @Provides
    @Singleton
    AuthTokenRepository provideTokenRepository() {
        return authTokenRepository;
    }

    @Provides
    @Singleton
    Api provideApi() {
        return api;
    }

    @Provides
    @Singleton
    DailyDataRepository provideDailyDataRepository() {
        return Mockito.mock(DailyDataRepository.class);
    }
}
