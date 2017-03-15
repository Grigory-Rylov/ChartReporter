package com.grishberg.graphreporter.common.data.rest;

/**
 * Created by grishberg on 13.01.17.
 */

public interface SoftErrorDelegate<T> {
    Throwable checkSoftError(T body);
}

