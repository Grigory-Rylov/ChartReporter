package com.grishberg.graphreporter.data.model;

/**
 * Created by grishberg on 11.01.17.
 */
public class AuthContainer {

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

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpires() {
        return expires;
    }

    public int getRole() {
        return role;
    }
}
