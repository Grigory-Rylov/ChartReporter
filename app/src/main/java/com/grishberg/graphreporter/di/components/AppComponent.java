package com.grishberg.graphreporter.di.components;

import com.grishberg.graphreporter.data.repository.BaseRestRepository;
import com.grishberg.graphreporter.di.modules.AppModule;
import com.grishberg.graphreporter.di.modules.ProfileModule;
import com.grishberg.graphreporter.di.modules.RestModule;
import com.grishberg.graphreporter.di.sub.modules.AuthModule;
import com.grishberg.graphreporter.mvp.presenter.CandlesChartPresenter;
import com.grishberg.graphreporter.mvp.presenter.ProductsPresenter;
import com.grishberg.graphreporter.mvp.presenter.SplashScreenPresenter;
import com.grishberg.graphreporter.ui.fragments.CandleFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by grishberg on 12.01.17.
 */
@Singleton
@Component(modules = {RestModule.class,
        AppModule.class,
        ProfileModule.class
})

public interface AppComponent {

    AuthComponent plusAuthModule(AuthModule authModule);

    void inject(CandlesChartPresenter candlesChartPresenter);

    void inject(SplashScreenPresenter splashScreenPresenter);

    void inject(BaseRestRepository baseRestRepository);

    void inject(ProductsPresenter productsPresenter);

    void inject(CandleFragment candleFragment);
}
