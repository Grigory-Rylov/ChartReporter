package com.grishberg.graphreporter.utils;

/**
 * Created by grishberg on 15.01.17.
 */
public class DebugLogger implements LogService {
    private static final String TAG = DebugLogger.class.getSimpleName();

    @Override
    public void d(final String tag, final String message) {
        System.out.println(message);
    }

    @Override
    public void e(final String tag, final String message, final Throwable throwable) {
        throwable.printStackTrace();
    }
}
