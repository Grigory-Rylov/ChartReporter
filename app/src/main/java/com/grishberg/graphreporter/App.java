package com.grishberg.graphreporter;

import android.app.Application;

import com.grishberg.graphreporter.data.repository.AuthTokenRepositoryImpl;
import com.grishberg.graphreporter.di.AppModule;
import com.grishberg.graphreporter.di.DaggerAppComponent;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.di.ProfileModule;
import com.grishberg.graphreporter.di.RestModule;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by grishberg on 12.01.17.
 */
public final class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        DiManager.initComponents(DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .restModule(new RestModule(getString(R.string.end_point)))
                .profileModule(new ProfileModule(new AuthTokenRepositoryImpl(getApplicationContext())))
                .build()
        );
    }
}
