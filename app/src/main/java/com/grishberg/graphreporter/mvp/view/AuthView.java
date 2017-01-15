package com.grishberg.graphreporter.mvp.view;

import com.grishberg.graphreporter.mvp.common.BaseViewWithProgress;

/**
 * Created by grishberg on 12.01.17.
 */

public interface AuthView extends BaseViewWithProgress {
    void showLoginEmptyError();

    void showPasswordEmptyError();

    void showSuccess();

    void showWrongCredentials();
}
