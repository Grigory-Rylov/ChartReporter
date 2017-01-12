package com.grishberg.graphreporter.data.repository;

import com.grishberg.graphreporter.data.model.AuthContainer;

/**
 * Created by grishberg on 12.01.17.
 */
public class AuthTokenRepositoryImpl implements AuthTokenRepository {
    private static final String TAG = AuthTokenRepositoryImpl.class.getSimpleName();
    private AuthContainer authContainer;
    private String login;

    @Override
    public AuthContainer getAuthInfo() {
        return authContainer;
    }

    @Override
    public void setAuthInfo(AuthContainer authContainer) {
        this.authContainer = authContainer;
    }

    @Override
    public void setCurrentLogin(String login) {
        this.login = login;
    }

    @Override
    public String getLogin() {
        return login;
    }
}
