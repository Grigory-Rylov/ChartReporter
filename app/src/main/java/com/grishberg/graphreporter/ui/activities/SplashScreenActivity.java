package com.grishberg.graphreporter.ui.activities;

import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.mvp.presenter.SplashScreenPresenter;
import com.grishberg.graphreporter.mvp.view.SplashScreenView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends MvpAppCompatActivity implements SplashScreenView {

    @InjectPresenter
    SplashScreenPresenter presenter;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        presenter.checkAuth();
    }

    @Override
    public void showMainScreen() {
        ChartActivity.start(this);
    }

    @Override
    public void showLoginScreen() {
        AuthActivity.start(this);
    }
}
