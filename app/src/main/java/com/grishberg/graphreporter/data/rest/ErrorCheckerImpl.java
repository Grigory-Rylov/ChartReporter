package com.grishberg.graphreporter.data.rest;

import com.grishberg.graphreporter.common.data.rest.SoftErrorDelegate;
import com.grishberg.graphreporter.data.RestConst;
import com.grishberg.graphreporter.data.model.common.RestResponse;
import com.grishberg.graphreporter.data.repository.exceptions.InvalidTokenException;
import com.grishberg.graphreporter.data.repository.exceptions.WrongCredentialsException;

import lombok.NonNull;

/**
 * Created by grishberg on 13.01.17.
 */
public class ErrorCheckerImpl implements SoftErrorDelegate<RestResponse> {
    private static final String TAG = ErrorCheckerImpl.class.getSimpleName();

    @Override
    public Throwable checkSoftError(@NonNull final RestResponse body) {
        if (body.getError() == null) {
            return null;
        }
        switch (body.getError().getCode()) {
            case RestConst.Errors.USER_NOT_FOUND:
            case RestConst.Errors.WRONG_CREDENTIALS:
                return new WrongCredentialsException(body.getError());
            case RestConst.Errors.TOKEN_INVALID:
                return new InvalidTokenException(body.getError());
            default:
                return null;
        }
    }
}
