package com.grishberg.graphreporter.data.model.common;

import lombok.Getter;

/**
 * Created by grishberg on 12.01.17.
 */
@Getter
public class RestResponse<T> {
    private static final String TAG = RestResponse.class.getSimpleName();

    private final boolean isCached;

    private final T data;

    private RestError error;

    public RestResponse(final T data) {
        this.data = data;
        this.isCached = false;
    }

    public RestResponse(final T data, final boolean isCached) {
        this.data = data;
        this.isCached = isCached;
    }
}
