package com.grishberg.graphreporter.mvp.view;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.model.ProductItem;
import com.grishberg.graphreporter.mvp.common.BaseViewWithProgress;

/**
 * Created by grishberg on 15.01.17.
 */

public interface ProductsView extends BaseViewWithProgress {
    void showProducts(ListResultCloseable<ProductItem> productItems);
}
