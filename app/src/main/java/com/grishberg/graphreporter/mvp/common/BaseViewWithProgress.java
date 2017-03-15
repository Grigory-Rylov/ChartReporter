package com.grishberg.graphreporter.mvp.common;

/**
 * Created by grishberg on 15.01.17.
 */

public interface BaseViewWithProgress extends BaseView {
    void showProgress();

    void hideProgress();

    void showFail(String message);
}
