package com.grishberg.graphreporter.mvp.view;

import com.grishberg.graphreporter.data.model.AuthContainer;
import com.grishberg.graphreporter.mvp.common.BaseView;

/**
 * Created by grishberg on 12.01.17.
 */

public interface AuthView extends BaseView {
    void showLoginEmptyError();

    void showPasswordEmptyError();

    void showProgress();

    void hideProgress();

    void showSuccess();

    void showWrongCredentials();

    void showFail(String message);
}
