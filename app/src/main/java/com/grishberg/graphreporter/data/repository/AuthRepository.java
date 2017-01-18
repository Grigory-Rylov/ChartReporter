package com.grishberg.graphreporter.data.repository;

import rx.Observable;

/**
 * Created by grishberg on 11.01.17.
 */

public interface AuthRepository {
    Observable<Boolean> login(String login, CharSequence password);
}
