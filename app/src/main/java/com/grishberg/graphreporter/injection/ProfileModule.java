package com.grishberg.graphreporter.injection;

import com.grishberg.graphreporter.data.repository.AuthRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by grishberg on 12.01.17.
 */
@Module
public class ProfileModule {
    private static final String TAG = ProfileModule.class.getSimpleName();
    private final AuthRepository authRepository;

    public ProfileModule(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Provides
    @Singleton
    AuthRepository provideAuthRepository() {
        return authRepository;
    }
}
