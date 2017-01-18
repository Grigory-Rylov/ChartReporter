package com.grishberg.graphreporter.di.components;

import com.grishberg.graphreporter.di.scopes.AuthScope;
import com.grishberg.graphreporter.di.sub.modules.AuthModule;
import com.grishberg.graphreporter.mvp.presenter.AuthPresenter;

import dagger.Subcomponent;

/**
 * Created by grishberg on 18.01.17.
 */

@AuthScope
@Subcomponent(modules = AuthModule.class)
public interface AuthComponent {

    void inject(AuthPresenter authPresenter);
}