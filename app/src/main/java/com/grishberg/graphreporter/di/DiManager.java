package com.grishberg.graphreporter.di;

/**
 * Created by grishberg on 16.01.17.
 * Мэнеджер зависимостей
 */
public class DiManager {
    private static final String TAG = DiManager.class.getSimpleName();
    private static AppComponent appComponent;

    public static void initComponents(final AppComponent component) {
        appComponent = component;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
