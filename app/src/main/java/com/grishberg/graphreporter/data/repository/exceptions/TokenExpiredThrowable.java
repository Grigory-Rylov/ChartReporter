package com.grishberg.graphreporter.data.repository.exceptions;

import com.grishberg.graphreporter.data.model.common.RestError;

/**
 * Created by grishberg on 13.11.16.
 * Ошибка авторизации - ссессия устарела
 */
public class TokenExpiredThrowable extends Throwable {
    public TokenExpiredThrowable(final RestError message) {
        super(message != null ? message.getMessage() : "");
    }
}
