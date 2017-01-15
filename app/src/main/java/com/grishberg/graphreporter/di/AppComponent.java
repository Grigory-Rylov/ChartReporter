package com.grishberg.graphreporter.di;

import com.grishberg.graphreporter.data.repository.BaseRestRepository;
import com.grishberg.graphreporter.mvp.presenter.AuthPresenter;
import com.grishberg.graphreporter.mvp.presenter.CandlesChartPresenter;
import com.grishberg.graphreporter.mvp.presenter.SplashScreenPresenter;

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
    void inject(AuthPresenter authPresenter);

    void inject(CandlesChartPresenter candlesChartPresenter);

    void inject(SplashScreenPresenter splashScreenPresenter);

    void inject(BaseRestRepository baseRestRepository);
}
