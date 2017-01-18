package com.grishberg.graphreporter.utils;

import android.util.Log;

/**
 * Created by grishberg on 14.01.17.
 */
public class LogServiceImpl implements LogService {

    @Override
    public void d(final String tag, final String message) {
        Log.d(tag, message);
    }

    @Override
    public void e(final String tag, final String message) {
        Log.e(tag, message);
    }

    @Override
    public void e(final String tag, final String message, final Throwable throwable) {
        Log.e(tag, message, throwable);
    }
}
