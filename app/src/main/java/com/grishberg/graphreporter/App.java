package com.grishberg.graphreporter;

import android.app.Application;

import com.grishberg.graphreporter.data.RestConst;
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
    private static final String TAG = App.class.getSimpleName();
    private static AppComponent appComponent;
    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public synchronized static AppComponent getAppComponent() {
        if (appComponent == null) {
            initComponents(DaggerAppComponent
                    .builder()
                    .appModule(new AppModule(sInstance))
                    .restModule(new RestModule(RestConst.END_POINT))
                    .profileModule(new ProfileModule(new AuthTokenRepositoryImpl()))
                    .build()
            );
        }
        return appComponent;
    }

    public static void initComponents(final AppComponent component) {
        appComponent = component;
    }
}
