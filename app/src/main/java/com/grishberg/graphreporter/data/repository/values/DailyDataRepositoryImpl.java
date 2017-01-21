package com.grishberg.graphreporter.data.repository.values;

import com.grishberg.graphreporter.data.model.AuthContainer;
import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.repository.BaseRestRepository;
import com.grishberg.graphreporter.data.repository.auth.AuthTokenRepository;
import com.grishberg.graphreporter.data.repository.exceptions.WrongCredentialsException;
import com.grishberg.graphreporter.data.rest.Api;

import java.util.List;
import java.util.Objects;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by grishberg on 15.01.17.
 */
public class DailyDataRepositoryImpl extends BaseRestRepository implements DailyDataRepository {

    private final AuthTokenRepository authTokenRepository;

    DailyDataStorage dataStorage;

    private final Api api;

    public DailyDataRepositoryImpl(final AuthTokenRepository authTokenRepository,
                                   final Api api,
                                   final DailyDataStorage dataStorage) {
        this.authTokenRepository = authTokenRepository;
        this.api = api;
        this.dataStorage = dataStorage;
    }

    @Override
    public Observable<List<DailyValue>> getDailyValues(final long productId) {
        final AuthContainer authInfo = authTokenRepository.getAuthInfo();
        if (authInfo == null) {
            return Observable.error(new WrongCredentialsException(null));
        }

        //Извлечь данные из кэша
        final Observable<List<DailyValue>> dailyValuesFromCache = dataStorage.getDailyValues(productId)
                .filter(response -> response != null)
                .subscribeOn(Schedulers.computation());

        // извлечь данные из сети
        final Observable<List<DailyValue>> dailyValues = api.getDailyData(authInfo.getAccessToken(), productId, 0, 1000)
                .onErrorResumeNext(
                        refreshTokenAndRetry(Observable.defer(() ->
                                api.getDailyData(authInfo.getAccessToken(), productId, 0, 1000))))
                .subscribeOn(Schedulers.io())
                .flatMap(response -> {
                    dataStorage.setDailyData(productId, response.getData());
                    return Observable.just(response.getData());
                });

        /**
         * соединить observables кэша и rest
         */
        return Observable
                //.amb(dailyValuesFromCache, dailyValues)
                .concat(dailyValuesFromCache, dailyValues).first()
                .observeOn(AndroidSchedulers.mainThread());
    }
}
