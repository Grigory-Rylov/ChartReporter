package com.grishberg.graphreporter.data.repository;

import com.grishberg.datafacade.ArrayListResult;
import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.model.AuthContainer;
import com.grishberg.graphreporter.data.model.ProductItem;
import com.grishberg.graphreporter.data.repository.exceptions.WrongCredentialsException;
import com.grishberg.graphreporter.data.rest.Api;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by grishberg on 15.01.17.
 */
public class ProductsRepositoryImpl extends BaseRestRepository implements ProductsRepository {
    
    private final AuthTokenRepository authTokenRepository;

    private final Api api;

    public ProductsRepositoryImpl(final AuthTokenRepository authTokenRepository, final Api api) {
        this.authTokenRepository = authTokenRepository;
        this.api = api;
    }

    @Override
    public Observable<ListResultCloseable<ProductItem>> getProducts(final long categoryId) {
        final AuthContainer authInfo = authTokenRepository.getAuthInfo();
        if (authInfo == null) {
            return Observable.error(new WrongCredentialsException(null));
        }
        return api.getProducts(authInfo.getAccessToken(), categoryId)
                .onErrorResumeNext(
                        refreshTokenAndRetry(Observable.defer(() ->
                                api.getProducts(authInfo.getAccessToken(), categoryId))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(response -> Observable.just(ArrayListResult.fromList(response.getData())));
    }
}
