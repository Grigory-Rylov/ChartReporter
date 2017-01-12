package com.grishberg.graphreporter.data.model.common;

import lombok.Getter;

/**
 * Created by grishberg on 12.01.17.
 */
@Getter
public class RestError {
    private static final String TAG = RestError.class.getSimpleName();
    private int code;

    private String message;
}
