package com.grishberg.graphreporter.data.services;

import com.grishberg.graphreporter.data.beans.CategoryItem;
import com.grishberg.graphreporter.data.beans.DailyValue;
import com.grishberg.graphreporter.data.beans.ProductItem;

import rx.Observable;

/**
 * Created by grishberg on 11.01.17.
 */

public interface DataService {
    Observable<CategoryItem> getCategories();

    Observable<ProductItem> getProducts(int categoryId);

    Observable<DailyValue> getdailyData(int productId, int offset, int limit);
}
