package com.grishberg.graphreporter.data.repository;

import com.grishberg.graphreporter.App;
import com.grishberg.graphreporter.data.repository.exceptions.TokenExpiredException;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.utils.LogService;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by grishberg on 15.01.17.
 */
public class BaseRestRepository {
    private static final String TAG = BaseRestRepository.class.getCanonicalName();
    @Inject
    LogService log;

    @Inject
    AuthRepository authRepository;

    public BaseRestRepository() {
        DiManager.getAppComponent().inject(this);
    }

    protected <T> Func1<Throwable, ? extends Observable<? extends T>> refreshTokenAndRetry(final Observable<T> toBeResumed) {
        return new Func1<Throwable, Observable<? extends T>>() {
            @Override
            public Observable<? extends T> call(final Throwable throwable) {
                // проверка на ошибку TokenExpiredException
                if (throwable instanceof TokenExpiredException) {
                    return authRepository.refreshToken()
                            .flatMap(new Func1<Boolean, Observable<? extends T>>() {
                                @Override
                                public Observable<? extends T> call(final Boolean isSuccess) {
                                    log.d(TAG, "flatMap: success refreshed token, " + Thread.currentThread());
                                    return toBeResumed;
                                }
                            });
                }
                log.e(TAG, "re-throw error", throwable);
                // re-throw this error because it's not recoverable from here
                return Observable.error(throwable);
            }
        };
    }
}
