package com.grishberg.graphreporter.data.model;

/**
 * Created by grishberg on 11.01.17.
 */
public class AuthContainer {
    private static final String TAG = AuthContainer.class.getSimpleName();

    private String refreshToken;

    private String accessToken;

    private long expires;

    private int role;

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpires() {
        return expires;
    }

    public int getRole() {
        return role;
    }
}
