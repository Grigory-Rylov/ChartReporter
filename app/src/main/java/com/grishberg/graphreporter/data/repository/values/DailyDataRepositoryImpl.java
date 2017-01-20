package com.grishberg.graphreporter.data.repository.values;

import com.grishberg.graphreporter.data.model.AuthContainer;
import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.repository.BaseRestRepository;
import com.grishberg.graphreporter.data.repository.auth.AuthTokenRepository;
import com.grishberg.graphreporter.data.repository.exceptions.WrongCredentialsException;
import com.grishberg.graphreporter.data.rest.Api;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by grishberg on 15.01.17.
 */
public class DailyDataRepositoryImpl extends BaseRestRepository implements DailyDataRepository {

    private final AuthTokenRepository authTokenRepository;

    private final Api api;

    public DailyDataRepositoryImpl(final AuthTokenRepository authTokenRepository, final Api api) {
        this.authTokenRepository = authTokenRepository;
        this.api = api;
    }

    @Override
    public Observable<List<DailyValue>> getDailyValues(final long productId) {
        final AuthContainer authInfo = authTokenRepository.getAuthInfo();
        if (authInfo == null) {
            return Observable.error(new WrongCredentialsException(null));
        }
        return api.getDailyData(authInfo.getAccessToken(), productId, 0, 1000)
                .onErrorResumeNext(
                        refreshTokenAndRetry(Observable.defer(() ->
                                api.getDailyData(authInfo.getAccessToken(), productId, 0, 1000))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(response -> Observable.just(response.getData()));
    }

}
