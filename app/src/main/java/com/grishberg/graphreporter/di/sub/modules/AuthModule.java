package com.grishberg.graphreporter.di.sub.modules;

import com.grishberg.graphreporter.data.repository.AuthRepository;
import com.grishberg.graphreporter.data.repository.AuthRepositoryImpl;
import com.grishberg.graphreporter.data.repository.AuthTokenRepository;
import com.grishberg.graphreporter.data.rest.Api;
import com.grishberg.graphreporter.di.scopes.AuthScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by grishberg on 18.01.17.
 */
@Module
public class AuthModule {

    @AuthScope
    @Provides
    AuthRepository provideAuthRepository(final Api api, final AuthTokenRepository tokenRepository) {
        return new AuthRepositoryImpl(api, tokenRepository);
    }
}
