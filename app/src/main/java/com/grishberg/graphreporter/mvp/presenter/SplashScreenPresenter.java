package com.grishberg.graphreporter.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.grishberg.graphreporter.App;
import com.grishberg.graphreporter.data.repository.AuthTokenRepository;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.mvp.common.BasePresenter;
import com.grishberg.graphreporter.mvp.view.SplashScreenView;
import com.grishberg.graphreporter.utils.StringUtils;

import javax.inject.Inject;

/**
 * Created by grishberg on 15.01.17.
 */
@InjectViewState
public class SplashScreenPresenter extends BasePresenter<SplashScreenView> {
    @Inject
    AuthTokenRepository repository;

    public SplashScreenPresenter() {
        DiManager.getAppComponent().inject(this);
    }

    public void checkAuth() {
        if (repository.getAuthInfo() != null && !StringUtils.isEmpty(repository.getAuthInfo().getRefreshToken())) {
            getViewState().showMainScreen();
            return;
        }
        getViewState().showLoginScreen();
    }
}
