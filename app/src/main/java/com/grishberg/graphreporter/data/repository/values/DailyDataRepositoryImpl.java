package com.grishberg.graphreporter.data.repository.values;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.model.AuthContainer;
import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.repository.BaseRestRepository;
import com.grishberg.graphreporter.data.repository.auth.AuthTokenRepository;
import com.grishberg.graphreporter.data.repository.exceptions.WrongCredentialsException;
import com.grishberg.graphreporter.data.rest.Api;
import com.grishberg.graphreporter.data.rest.RestConst;
import com.grishberg.graphreporter.data.storage.DailyDataStorage;

import java.util.concurrent.atomic.AtomicLong;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by grishberg on 15.01.17.
 */
public class DailyDataRepositoryImpl extends BaseRestRepository implements DailyDataRepository {
    private static final int INITIAL_OFFSET = 0;
    private final AuthTokenRepository authTokenRepository;
    private final Api api;
    private final CacheActualityChecker cacheChecker;
    DailyDataStorage dataStorage;

    public DailyDataRepositoryImpl(final AuthTokenRepository authTokenRepository,
                                   final Api api,
                                   final DailyDataStorage dataStorage,
                                   final CacheActualityChecker cacheChecker) {
        this.authTokenRepository = authTokenRepository;
        this.api = api;
        this.dataStorage = dataStorage;
        this.cacheChecker = cacheChecker;
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

        // извлечь данные из сети
        return dailyValuesFromCache.concatMap(cachedResult -> {
            if (cacheChecker.isCacheDataValid(cachedResult)) {
                return Observable.just(cachedResult);
            }
            if (hasDataInCache(cachedResult)) {
                final DailyValue lastItem = cachedResult.get(cachedResult.size() - 1);
                final Observable<ListResultCloseable<DailyValue>> appendedRemoteData = getAndAppendRemoteData(productId, authInfo, lastItem.getDt());
                return Observable.concat(appendedRemoteData, Observable.just(cachedResult))
                        .filter(dataList -> !dataList.isEmpty())
                        .first();
            } else {
                return getAllDataObservable(productId, authInfo);
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    private boolean hasDataInCache(final ListResultCloseable<DailyValue> cachedResult) {
        return !cachedResult.isEmpty();
    }

    private Observable<ListResultCloseable<DailyValue>> getAndAppendRemoteData(final long productId,
                                                                               final AuthContainer authInfo,
                                                                               final long startDate) {
        final AtomicLong offsetAtomic = new AtomicLong(INITIAL_OFFSET);

        return Observable
                .range(0, Integer.MAX_VALUE)
                .concatMap(pageIndex -> {
                    offsetAtomic.set(pageIndex * RestConst.PAGE_LIMIT);
                    return api.getValuesFromDate(authInfo.getAccessToken(),
                            productId,
                            startDate,
                            offsetAtomic.get(),
                            RestConst.PAGE_LIMIT);
                })
                .onErrorResumeNext(
                        refreshTokenAndRetry(Observable.defer(() ->
                                api.getValues(authInfo.getAccessToken(), productId, offsetAtomic.get(), RestConst.PAGE_LIMIT))))

                .takeWhile(response -> {
                    if (!response.getData().isEmpty()) {
                        dataStorage.appendDailyData(productId, response.getData() );
                        return true;
                    }
                    return false;
                })
                .subscribeOn(Schedulers.io())
                .takeLast(1)
                .flatMap(response -> dataStorage.getDailyValues(productId, INITIAL_OFFSET));
    }

    private Observable<ListResultCloseable<DailyValue>> getAllDataObservable(final long productId, final AuthContainer authInfo) {
        final AtomicLong offsetAtomic = new AtomicLong(INITIAL_OFFSET);

        return Observable
                .range(0, Integer.MAX_VALUE)
                .concatMap(pageIndex -> {
                    offsetAtomic.set(pageIndex * RestConst.PAGE_LIMIT);
                    return api.getValues(authInfo.getAccessToken(),
                            productId,
                            offsetAtomic.get(),
                            RestConst.PAGE_LIMIT);
                })
                .takeWhile(response -> !response.getData().isEmpty())
                .onErrorResumeNext(
                        refreshTokenAndRetry(Observable.defer(() ->
                                api.getValues(authInfo.getAccessToken(), productId, offsetAtomic.get(), RestConst.PAGE_LIMIT))))
                .subscribeOn(Schedulers.io())
                .doOnNext(response -> dataStorage.appendDailyData(productId, response.getData()))
                .last()
                .flatMap(response -> dataStorage.getDailyValues(productId, INITIAL_OFFSET));
    }

    private boolean checkCacheValid(final ListResultCloseable<DailyValue> response) {
        return response != null;
    }
}
