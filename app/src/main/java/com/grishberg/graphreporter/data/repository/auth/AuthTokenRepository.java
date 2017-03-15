package com.grishberg.graphreporter.data.repository.auth;

import com.grishberg.graphreporter.data.beans.AuthContainer;

/**
 * Created by grishberg on 11.01.17.
 * Сервис сохранения информации о пользователе
 */

public interface AuthTokenRepository {
    AuthContainer getAuthInfo();

    void setAuthInfo(AuthContainer authContainer);

    void updateAccessToken(String newAccessToken);

    void setCurrentLogin(String login);

    String getLogin();
}
