package com.grishberg.graphreporter.data.repository.exceptions;

import com.grishberg.graphreporter.data.model.common.RestError;

/**
 * Created by grishberg on 13.11.16.
 * Ошибка авторизации - ссессия устарела
 */
public class InvalidTokenException extends Throwable {
    private static final String TAG = InvalidTokenException.class.getSimpleName();

    public InvalidTokenException(final RestError message) {
        super(message != null ? message.getMessage() : "");
    }
}
