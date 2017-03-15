package com.grishberg.graphreporter.data.services;

import rx.Observable;

/**
 * Created by grishberg on 18.01.17.
 */
public interface RefreshTokenService {
    Observable<Boolean> refreshToken();
}
