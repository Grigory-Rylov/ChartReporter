package com.grishberg.graphreporter.data.services;

import com.grishberg.graphreporter.data.beans.AuthContainer;
import com.grishberg.graphreporter.data.repository.auth.AuthTokenRepository;
import com.grishberg.graphreporter.data.repository.exceptions.WrongCredentialsException;
import com.grishberg.graphreporter.data.rest.Api;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by grishberg on 18.01.17.
 */
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final Api api;
    private final AuthTokenRepository authRepository;

    public RefreshTokenServiceImpl(final AuthTokenRepository authRepository, final Api api) {
        this.api = api;
        this.authRepository = authRepository;
    }

    @Override
    public Observable<Boolean> refreshToken() {
        final AuthContainer authInfo = authRepository.getAuthInfo();
        if (authInfo == null) {
            return Observable.error(new WrongCredentialsException(null));
        }
        return api.refreshToken(authInfo.getRefreshToken())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(response -> {
                    // сохранить в хранилище токен авторизации
                    authRepository.updateAccessToken(response.getData().getAccessToken());
                    return Observable.just(true);
                });
    }
}
