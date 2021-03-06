package com.grishberg.graphreporter.data.beans;

/**
 * Created by grishberg on 11.01.17.
 * Модель данных об авторизации
 */
public class AuthContainer {

    private final String refreshToken;

    private String accessToken;

    private long expires;

    private int role;

    public AuthContainer(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

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
