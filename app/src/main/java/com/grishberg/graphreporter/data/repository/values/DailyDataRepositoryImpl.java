package com.grishberg.graphreporter.data.repository.values;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.beans.AuthContainer;
import com.grishberg.graphreporter.data.beans.DailyValue;
import com.grishberg.graphreporter.data.beans.DailyValueProtos;
import com.grishberg.graphreporter.data.beans.DailyValueProtos.DailyValueContainer;
import com.grishberg.graphreporter.data.beans.common.RestResponse;
import com.grishberg.graphreporter.data.repository.BaseRestRepository;
import com.grishberg.graphreporter.data.repository.auth.AuthTokenRepository;
import com.grishberg.graphreporter.data.repository.exceptions.WrongCredentialsException;
import com.grishberg.graphreporter.data.rest.Api;
import com.grishberg.graphreporter.data.rest.ProtocApi;
import com.grishberg.graphreporter.data.rest.RestConst;
import com.grishberg.graphreporter.data.storage.DailyDataStorage;

import java.util.List;
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
    private final ProtocApi protocApi;
    private final CacheActualityChecker cacheChecker;
    private final AtomicLong offsetAtomic = new AtomicLong(INITIAL_OFFSET);
    DailyDataStorage dataStorage;

    public DailyDataRepositoryImpl(final AuthTokenRepository authTokenRepository,
                                   final Api api,
                                   final ProtocApi protocApi,
                                   final DailyDataStorage dataStorage,
                                   final CacheActualityChecker cacheChecker) {
        this.authTokenRepository = authTokenRepository;
        this.api = api;
        this.protocApi = protocApi;
        this.dataStorage = dataStorage;
        this.cacheChecker = cacheChecker;
    }

    @Override
    public Observable<ListResultCloseable<DailyValue>> getValues(final long productId,
                                                                 final long startDate) {
        final AuthContainer authInfo = authTokenRepository.getAuthInfo();
        if (authInfo == null) {
            return Observable.error(new WrongCredentialsException(null));
        }

        final Observable<ListResultCloseable<DailyValue>> dailyValuesFromCache = getDataFromCache(productId, startDate);
        final Observable<DailyValueContainer> remoteAllDataObservable = Observable.defer(() ->
                protocApi.getValuesFromDate(authInfo.getAccessToken(),
                        productId,
                        startDate,
                        offsetAtomic.get(),
                        RestConst.PAGE_LIMIT));

        return dailyValuesFromCache.concatMap(cachedResult -> {
            if (cacheChecker.isCacheDataValid(cachedResult)) {
                return Observable.just(cachedResult);
            }
            if (hasDataInCache(cachedResult)) {
                final DailyValue lastItemFromCache = cachedResult.get(cachedResult.size() - 1);
                final Observable<DailyValueContainer> remoteFromDateDataObservable = Observable.defer(() ->
                        protocApi.getValuesFromDate(authInfo.getAccessToken(),
                                productId,
                                lastItemFromCache.getDt(),
                                offsetAtomic.get(),
                                RestConst.PAGE_LIMIT));

                final Observable<ListResultCloseable<DailyValue>> appendedRemoteData = getRemotePageData(productId, remoteFromDateDataObservable);
                return Observable.concat(appendedRemoteData, Observable.just(cachedResult))
                        .filter(dataList -> !dataList.isEmpty())
                        .first()
                        .doOnUnsubscribe(cachedResult::silentClose);
            }
            return getRemotePageData(productId, remoteAllDataObservable);
        }).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<ListResultCloseable<DailyValue>> getDataFromCache(long productId, long startDate) {
        return dataStorage
                .getValues(productId, startDate)
                .filter(this::checkCacheValid)
                .subscribeOn(Schedulers.computation());
    }

    private boolean hasDataInCache(final ListResultCloseable<DailyValue> cachedResult) {
        return !cachedResult.isEmpty();
    }

    private Observable<ListResultCloseable<DailyValue>> getRemotePageData(final long productId,
                                                                          final Observable<DailyValueContainer> remoteDataObservable) {
        return Observable
                .range(0, Integer.MAX_VALUE)
                .concatMap(pageIndex -> {
                    offsetAtomic.set(pageIndex.longValue() * RestConst.PAGE_LIMIT);
                    return remoteDataObservable;
                })
                .takeWhile(response -> isDailyValuesExists(response))
                .onErrorResumeNext(
                        refreshTokenAndRetry(remoteDataObservable))
                .subscribeOn(Schedulers.io())
                .doOnNext(response -> {
                    dataStorage.appendData(productId, response.getDailyValueList());
                })
                .takeLast(1)
                .flatMap(response -> dataStorage.getValues(productId, INITIAL_OFFSET));
    }

    private boolean isDailyValuesExists(DailyValueContainer response) {
        return response.getDailyValueCount() > 0;
    }

    private boolean checkCacheValid(final ListResultCloseable<DailyValue> response) {
        return response != null;
    }
}
