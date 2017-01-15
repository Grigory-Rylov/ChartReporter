package com.grishberg.graphreporter.injection;

import com.grishberg.graphreporter.mvp.presenter.AuthPresenter;
import com.grishberg.graphreporter.mvp.presenter.CandlesChartPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by grishberg on 12.01.17.
 */
@Singleton
@Component(modules = {RestModule.class,
        AppModule.class,
        ProfileModule.class
        //DbModule.class
})

public interface AppComponent {
    void inject(AuthPresenter authPresenter);

    void inject(CandlesChartPresenter candlesChartPresenter);
}
