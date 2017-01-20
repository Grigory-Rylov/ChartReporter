package com.grishberg.graphreporter.di.modules;

import com.grishberg.graphreporter.data.repository.auth.AuthTokenRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by grishberg on 12.01.17.
 * DI для данных профиля
 */
@Module
public class ProfileModule {
    private final AuthTokenRepository authRepository;

    public ProfileModule(final AuthTokenRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Provides
    @Singleton
    AuthTokenRepository provideAuthRepository() {
        return authRepository;
    }
}
