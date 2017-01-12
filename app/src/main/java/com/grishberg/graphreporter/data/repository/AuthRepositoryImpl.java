package com.grishberg.graphreporter.data.repository;

import com.grishberg.graphreporter.data.model.AuthContainer;
import com.grishberg.graphreporter.data.model.RefreshTokenContainer;
import com.grishberg.graphreporter.data.services.Api;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by grishberg on 12.01.17.
 */
public class AuthRepositoryImpl implements AuthRepository {
    private static final String TAG = AuthRepositoryImpl.class.getSimpleName();

    private final Api api;
    private final AuthTokenRepository authRepository;

    public AuthRepositoryImpl(final Api api, final AuthTokenRepository authRepository) {
        this.api = api;
        this.authRepository = authRepository;
    }

    @Override
    public Observable<AuthContainer> login(String login, CharSequence password) {
        Observable<AuthContainer> observable = api.login(login, password.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(response -> {
                    // сохранить в хранилище токен авторизации
                    authRepository.setCurrentLogin(login);
                    authRepository.setAuthInfo(response.getData());
                    return Observable.just(response.getData());
                });
        return observable;
    }

    @Override
    public Observable<RefreshTokenContainer> refreshToken() {
        return null;
    }
}
