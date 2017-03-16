package com.grishberg.graphreporter.mvp.presenter;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.graphreporter.data.beans.ProductItem;
import com.grishberg.graphreporter.data.repository.ProductsRepository;
import com.grishberg.graphreporter.mvp.view.ProductsView;
import com.grishberg.graphreporter.utils.BaseTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by grishberg on 15.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductsPresenterTest extends BaseTestCase {

    @Mock
    ProductsRepository repository;

    @Mock
    ProductsView view;

    @Mock
    ListResultCloseable<ProductItem> result;

    ProductsPresenter presenter;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        presenter = new ProductsPresenter();
        presenter.attachView(view);
        presenter.repository = repository;
    }

    @Test
    public void testRequestProductsSuccess(){
        //given
        when(repository.getProducts(anyLong())).thenReturn(Observable.just(result));
        //when
        presenter.requestProducts();
        //then
        verify(view, times(1)).showProgress();
        verify(view, times(1)).hideProgress();
        verify(view, times(1)).showProducts(result);
    }
}