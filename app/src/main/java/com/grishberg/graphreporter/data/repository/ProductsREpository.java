package com.grishberg.graphreporter.data.repository;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.model.ProductItem;

import rx.Observable;

/**
 * Created by grishberg on 15.01.17.
 */

public interface ProductsRepository {
    Observable<ListResultCloseable<ProductItem>> getProducts(long categoryId);
}
