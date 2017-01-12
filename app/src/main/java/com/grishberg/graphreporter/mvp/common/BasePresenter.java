package com.grishberg.graphreporter.mvp.common;

import com.arellomobile.mvp.MvpPresenter;

/**
 * Created by grishberg on 01.01.17.
 */
public abstract class BasePresenter<V extends BaseView> extends MvpPresenter<V> {
    private static final String TAG = BasePresenter.class.getSimpleName();
}
