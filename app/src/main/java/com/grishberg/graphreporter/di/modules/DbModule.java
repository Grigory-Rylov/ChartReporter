package com.grishberg.graphreporter.di.modules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.grishberg.graphreporter.data.model.DaoMaster;
import com.grishberg.graphreporter.data.model.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by grishberg on 12.02.17.
 */
@Module
public class DbModule {
    private final Context context;

    public DbModule(final Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession() {
        final DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "chart-db", null);
        final SQLiteDatabase db = helper.getWritableDatabase();
        final DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }
}
