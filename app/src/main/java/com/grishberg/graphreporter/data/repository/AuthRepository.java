package com.grishberg.graphreporter.data.repository;

import com.grishberg.graphreporter.data.model.AuthContainer;

/**
 * Created by grishberg on 11.01.17.
 * Сервис сохранения информации о пользователе
 */

public interface AuthRepository {
    AuthContainer getAuthInfo();

    void setAuthInfo(AuthContainer authContainer);
}
