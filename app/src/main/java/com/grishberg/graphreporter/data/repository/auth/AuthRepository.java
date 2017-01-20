package com.grishberg.graphreporter.data.repository.auth;

import rx.Observable;

/**
 * Created by grishberg on 11.01.17.
 * Интерфейс репозитория авторизации
 */

public interface AuthRepository {
    Observable<Boolean> login(String login, CharSequence password);
}
