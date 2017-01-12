package com.grishberg.graphreporter.data.repository;

import com.grishberg.graphreporter.data.model.AuthContainer;

/**
 * Created by grishberg on 12.01.17.
 */
public class AuthRepositoryImpl implements AuthRepository {
    private static final String TAG = AuthRepositoryImpl.class.getSimpleName();
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
