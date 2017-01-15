package com.grishberg.graphreporter.data.repository;

import com.grishberg.graphreporter.data.model.ProductItem;

import java.util.List;

import rx.Observable;

/**
 * Created by grishberg on 15.01.17.
 */

public interface ProductsRepository {
    Observable<List<ProductItem>> getProducts(int categoryId);
}
