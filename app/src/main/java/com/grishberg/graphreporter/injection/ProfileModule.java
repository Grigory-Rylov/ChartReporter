package com.grishberg.graphreporter.injection;

import com.grishberg.graphreporter.data.repository.AuthTokenRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by grishberg on 12.01.17.
 */
@Module
public class ProfileModule {
    private static final String TAG = ProfileModule.class.getSimpleName();
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
