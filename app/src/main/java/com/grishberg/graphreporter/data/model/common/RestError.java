package com.grishberg.graphreporter.data.model.common;

/**
 * Created by grishberg on 12.01.17.
 */
public class RestError {
    private static final String TAG = RestError.class.getSimpleName();
    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
