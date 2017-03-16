package com.grishberg.graphreporter.data.repository;

import com.grishberg.graphreporter.data.beans.AuthContainer;
import com.grishberg.graphreporter.data.beans.ProductItem;
import com.grishberg.graphreporter.data.beans.common.RestResponse;
import com.grishberg.graphreporter.data.services.RefreshTokenService;
import com.grishberg.graphreporter.utils.BaseTestCase;
import com.grishberg.graphreporter.utils.RxSchedulersOverrideRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.*;

/**
 * Created by grishberg on 15.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductsRepositoryImplTest extends BaseTestCase{

    private static final int CATEGORY_ID = 1;
    public static final AuthContainer AUTH_CONTAINER = new AuthContainer("123", "456");

    @Rule
    // Must be added to every test class that targets app code that uses RxJava
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    RestResponse<List<ProductItem>> response;

    @Mock
    RefreshTokenService refreshTokenService;

    ProductsRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        repository = new ProductsRepositoryImpl(tokenRepository, api);
        repository.refreshTokenService = refreshTokenService;
    }

    @Test
    public void testRequestProductsSuccess(){
        //given
        when(tokenRepository.getAuthInfo()).thenReturn(AUTH_CONTAINER);
        final ProductItem item = mock(ProductItem.class);
        final List<ProductItem> data = new ArrayList<>();
        data.add(item);
        when(response.getData()).thenReturn(data);
        when(api.getProducts(anyString(), anyLong()))
                .thenReturn(Observable.just(response));
        final TestSubscriber<List<ProductItem>> testSubscriber = new TestSubscriber<>();
        //when
        repository.getProducts(CATEGORY_ID).subscribe(testSubscriber);
        //then
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Arrays.asList(data));
    }
}