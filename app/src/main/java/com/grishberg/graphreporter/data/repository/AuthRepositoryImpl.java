package com.grishberg.graphreporter.data.repository;

import com.grishberg.graphreporter.data.model.AuthContainer;
import com.grishberg.graphreporter.data.repository.exceptions.WrongCredentialsException;
import com.grishberg.graphreporter.data.rest.Api;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by grishberg on 12.01.17.
 */
public class AuthRepositoryImpl implements AuthRepository {

    private final Api api;
    private final AuthTokenRepository authRepository;

    public AuthRepositoryImpl(final Api api, final AuthTokenRepository authRepository) {
        this.api = api;
        this.authRepository = authRepository;
    }

    @Override
    public Observable<Boolean> login(final String login, final CharSequence password) {
        return api.login(login, password.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(response -> {
                    // сохранить в хранилище токен авторизации
                    authRepository.setCurrentLogin(login);
                    authRepository.setAuthInfo(response.getData());
                    return Observable.just(true);
                });
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
