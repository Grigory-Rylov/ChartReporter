package com.grishberg.graphreporter.mvp.presenter;

import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.beans.ProductItem;
import com.grishberg.graphreporter.data.repository.ProductsRepository;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.mvp.common.BasePresenter;
import com.grishberg.graphreporter.mvp.view.ProductsView;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by grishberg on 15.01.17.
 * Презентер для экрана отрбражения товаров
 */
@InjectViewState
public class ProductsPresenter extends BasePresenter<ProductsView> {
    public static final int CATEGORY_ID = 1;

    @Inject
    ProductsRepository repository;

    @Nullable
    private ListResultCloseable<ProductItem> productItems;

    public ProductsPresenter() {
        DiManager.getAppComponent().inject(this);
    }

    public void requestProducts() {
        getViewState().showProgress();
        repository.getProducts(CATEGORY_ID)
                .subscribe(response -> {
                    getViewState().hideProgress();
                    productItems = response;
                    getViewState().showProducts(response);
                }, exception -> {
                    getViewState().hideProgress();
                    getViewState().showFail(exception.getMessage());
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (productItems != null) {
            try {
                productItems.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
