package com.grishberg.graphreporter.data.repository.values;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.model.AuthContainer;
import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.model.common.RestResponse;
import com.grishberg.graphreporter.data.repository.BaseRestRepository;
import com.grishberg.graphreporter.data.repository.auth.AuthTokenRepository;
import com.grishberg.graphreporter.data.repository.exceptions.WrongCredentialsException;
import com.grishberg.graphreporter.data.rest.Api;
import com.grishberg.graphreporter.data.rest.RestConst;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by grishberg on 15.01.17.
 */
public class DailyDataRepositoryImpl extends BaseRestRepository implements DailyDataRepository {

    public static final int INITIAL_OFFSET = 0;
    private final AuthTokenRepository authTokenRepository;
    private final Api api;
    DailyDataStorage dataStorage;

    public DailyDataRepositoryImpl(final AuthTokenRepository authTokenRepository,
                                   final Api api,
                                   final DailyDataStorage dataStorage) {
        this.authTokenRepository = authTokenRepository;
        this.api = api;
        this.dataStorage = dataStorage;
    }

    @Override
    public Observable<ListResultCloseable<DailyValue>> getDailyValues(final long productId,
                                                                      final int offset) {
        final AuthContainer authInfo = authTokenRepository.getAuthInfo();
        if (authInfo == null) {
            return Observable.error(new WrongCredentialsException(null));
        }

        //Извлечь данные из кэша
        final Observable<ListResultCloseable<DailyValue>> dailyValuesFromCache = dataStorage
                .getDailyValues(productId, offset)
                .filter(response -> checkCacheValid(response))
                .subscribeOn(Schedulers.computation());

        final AtomicLong offsetAtomic = new AtomicLong(offset);
        // извлечь данные из сети
        final Observable<ListResultCloseable<DailyValue>> dailyValues = Observable
                .range(0, Integer.MAX_VALUE)
                .concatMap(pageIndex -> {
                    offsetAtomic.set(pageIndex * RestConst.PAGE_LIMIT);
                    return api.getDailyData(authInfo.getAccessToken(),
                            productId,
                            offsetAtomic.get(),
                            RestConst.PAGE_LIMIT);
                })
                .takeWhile(response -> !response.getData().isEmpty())
                .onErrorResumeNext(
                        refreshTokenAndRetry(Observable.defer(() ->
                                api.getDailyData(authInfo.getAccessToken(), productId, offsetAtomic.get(), RestConst.PAGE_LIMIT))))
                .subscribeOn(Schedulers.io())
                .flatMap(response -> {
                    if (offsetAtomic.get() == INITIAL_OFFSET) {
                        dataStorage.setDailyData(productId, response.getData());
                    } else {
                        dataStorage.appendDailyData(productId, response.getData());
                    }
                    return dataStorage.getDailyValues(productId, INITIAL_OFFSET);
                }).last();

        /**
         * соединить observables кэша и rest
         */
        return Observable
                .concat(dailyValuesFromCache, dailyValues).first()
                .observeOn(AndroidSchedulers.mainThread());
    }

    private boolean checkCacheValid(final ListResultCloseable<DailyValue> response) {
        return response != null;
    }
}
