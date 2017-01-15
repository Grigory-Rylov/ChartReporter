package com.grishberg.graphreporter.data.repository;

import com.grishberg.graphreporter.data.model.AuthContainer;

/**
 * Created by grishberg on 12.01.17.
 */
public class AuthTokenRepositoryImpl implements AuthTokenRepository {
    private AuthContainer authContainer;
    private String login;

    @Override
    public AuthContainer getAuthInfo() {
        return authContainer;
    }

    @Override
    public void setAuthInfo(final AuthContainer authContainer) {
        this.authContainer = authContainer;
    }

    @Override
    public void updateAccessToken(final String newAccessToken) {
        authContainer.setAccessToken(newAccessToken);
    }

    @Override
    public void setCurrentLogin(final String login) {
        this.login = login;
    }

    @Override
    public String getLogin() {
        return login;
    }
}
