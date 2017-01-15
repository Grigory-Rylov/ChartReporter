package com.grishberg.graphreporter;

import android.app.Application;

import com.grishberg.graphreporter.data.repository.AuthTokenRepositoryImpl;
import com.grishberg.graphreporter.injection.AppComponent;
import com.grishberg.graphreporter.injection.AppModule;
import com.grishberg.graphreporter.injection.DaggerAppComponent;
import com.grishberg.graphreporter.injection.ProfileModule;
import com.grishberg.graphreporter.injection.RestModule;

/**
 * Created by grishberg on 12.01.17.
 */
public final class App extends Application {
    private static AppComponent appComponent;
    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static synchronized AppComponent getAppComponent() {
        if (appComponent == null) {
            initComponents(DaggerAppComponent
                    .builder()
                    .appModule(new AppModule(sInstance))
                    .restModule(new RestModule(sInstance != null ? sInstance.getString(R.string.end_point) : "http://test.com"))
                    .profileModule(new ProfileModule(new AuthTokenRepositoryImpl(sInstance.getApplicationContext())))
                    .build()
            );
        }
        return appComponent;
    }

    public static void initComponents(final AppComponent component) {
        appComponent = component;
    }
}
