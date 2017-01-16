package com.grishberg.graphreporter.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.grishberg.graphreporter.App;
import com.grishberg.graphreporter.data.repository.BaseRestRepository;
import com.grishberg.graphreporter.data.repository.ProductsRepository;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.mvp.common.BasePresenter;
import com.grishberg.graphreporter.mvp.view.ProductsView;

import javax.inject.Inject;

/**
 * Created by grishberg on 15.01.17.
 */
@InjectViewState
public class ProductsPresenter extends BasePresenter<ProductsView> {
    private static final String TAG = ProductsPresenter.class.getSimpleName();
    public static final int CATEGORY_ID = 1;

    @Inject
    ProductsRepository repository;

    public ProductsPresenter() {
        DiManager.getAppComponent().inject(this);
    }

    public void requestProducts() {
        getViewState().showProgress();
        repository.getProducts(CATEGORY_ID)
                .subscribe(response -> {
                    getViewState().hideProgress();
                    getViewState().showProducts(response);
                }, exception -> {
                    getViewState().hideProgress();
                    getViewState().showFail(exception.getMessage());
                });
    }
}
