package com.grishberg.graphreporter.data.repository;

import com.grishberg.graphreporter.data.model.AuthContainer;
import com.grishberg.graphreporter.data.model.RefreshTokenContainer;

import rx.Observable;

/**
 * Created by grishberg on 11.01.17.
 */

public interface AuthRepository {
    Observable<AuthContainer> login(String login, CharSequence password);

    Observable<RefreshTokenContainer> refreshToken();
}
