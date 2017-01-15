package com.grishberg.graphreporter.utils;

/**
 * Created by grishberg on 14.01.17.
 */

public interface LogService {
    void d(String tag, String message);

    void e(String tag, String message, Throwable throwable);
}
