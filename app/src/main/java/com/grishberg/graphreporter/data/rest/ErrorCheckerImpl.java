package com.grishberg.graphreporter.data.rest;

import android.support.annotation.NonNull;

import com.grishberg.graphreporter.common.data.rest.SoftErrorDelegate;
import com.grishberg.graphreporter.data.beans.common.RestResponse;
import com.grishberg.graphreporter.data.repository.exceptions.TokenExpiredException;
import com.grishberg.graphreporter.data.repository.exceptions.WrongCredentialsException;

/**
 * Created by grishberg on 13.01.17.
 */
public class ErrorCheckerImpl implements SoftErrorDelegate<RestResponse> {

    @Override
    public Throwable checkSoftError(@NonNull final RestResponse body) {
        if (body.getError() == null) {
            return null;
        }
        switch (body.getError().getCode()) {
            case RestConst.Errors.WRONG_CREDENTIALS:
                return new WrongCredentialsException(body.getError());
            case RestConst.Errors.TOKEN_EXPIRED:
                return new TokenExpiredException(body.getError());
            default:
                return null;
        }
    }
}
