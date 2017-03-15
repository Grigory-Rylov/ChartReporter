package com.grishberg.graphreporter.data.beans.common;

/**
 * Created by grishberg on 12.01.17.
 */
public class RestResponse<T> {
    private final boolean isCached;

    private final T data;

    private RestError error;

    private RestPage page;

    public RestResponse(final T data) {
        this.data = data;
        this.isCached = false;
    }

    public RestResponse(final T data, final boolean isCached) {
        this.data = data;
        this.isCached = isCached;
    }

    public boolean isCached() {
        return isCached;
    }

    public T getData() {
        return data;
    }

    public RestError getError() {
        return error;
    }

    public RestPage getPage() {
        return page;
    }
}
