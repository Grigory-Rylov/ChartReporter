package com.grishberg.graphreporter;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;

import com.grishberg.graphreporter.data.model.DaoMaster;
import com.grishberg.graphreporter.data.model.DaoSession;
import com.grishberg.graphreporter.data.repository.auth.AuthTokenRepositoryImpl;
import com.grishberg.graphreporter.di.DiManager;

import com.crashlytics.android.Crashlytics;
import com.grishberg.graphreporter.di.components.DaggerAppComponent;
import com.grishberg.graphreporter.di.modules.AppModule;
import com.grishberg.graphreporter.di.modules.DbModule;
import com.grishberg.graphreporter.di.modules.ProfileModule;
import com.grishberg.graphreporter.di.modules.RestModule;
import com.grishberg.graphreporter.di.modules.StorageModule;
import com.squareup.leakcanary.LeakCanary;

import io.fabric.sdk.android.Fabric;

/**
 * Created by grishberg on 12.01.17.
 */
public final class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
            // Normal app init code..
            initStrictMode();
        } else {
            Fabric.with(this, new Crashlytics());
        }
        DiManager.initComponents(DaggerAppComponent
                .builder()
                .storageModule(new StorageModule())
                .appModule(new AppModule(this))
                .restModule(new RestModule(getString(R.string.end_point)))
                .profileModule(new ProfileModule(new AuthTokenRepositoryImpl(getApplicationContext())))
                .dbModule(new DbModule(this))
                .build()
        );
    }

    private void initStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }
}
