package com.grishberg.graphreporter.data.beans;

/**
 * Created by grishberg on 11.01.17.
 */
public class RefreshTokenContainer {
    private static final String TAG = RefreshTokenContainer.class.getSimpleName();
    private String accessToken;
    private long expires;

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpires() {
        return expires;
    }
}
