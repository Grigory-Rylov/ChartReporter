package com.grishberg.graphreporter.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.grishberg.graphreporter.App;
import com.grishberg.graphreporter.data.model.AuthContainer;
import com.grishberg.graphreporter.data.repository.AuthTokenRepository;
import com.grishberg.graphreporter.data.repository.exceptions.WrongCredentialsException;
import com.grishberg.graphreporter.data.repository.AuthRepository;
import com.grishberg.graphreporter.mvp.common.BasePresenter;
import com.grishberg.graphreporter.mvp.view.AuthView;
import com.grishberg.graphreporter.utils.StringUtils;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by grishberg on 12.01.17.
 */
@InjectViewState
public class AuthPresenter extends BasePresenter<AuthView> {
    private static final String TAG = AuthPresenter.class.getSimpleName();

    @Inject
    AuthTokenRepository authRepository;

    @Inject
    AuthRepository authService;

    public AuthPresenter() {
        App.getAppComponent().inject(this);
    }

    public void auth(final String login, final CharSequence password) {
        if (StringUtils.isEmpty(login)) {
            getViewState().showLoginEmptyError();
            return;
        }
        if (StringUtils.isEmpty(password)) {
            getViewState().showPasswordEmptyError();
            return;
        }

        getViewState().showProgress();

        final Observable<AuthContainer> observable = authService.login(login, password);
        observable.subscribe(response -> {
                    getViewState().hideProgress();
                    getViewState().showSuccess();
                }, exception -> {
                    getViewState().hideProgress();
                    if (exception instanceof WrongCredentialsException) {
                        getViewState().showWrongCredentials();
                        return;
                    }
                    getViewState().showFail(exception.getMessage());
                }

        );
    }
}
