package com.grishberg.graphreporter.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.grishberg.graphreporter.data.model.AuthContainer;
import com.grishberg.graphreporter.utils.StringUtils;

/**
 * Created by grishberg on 12.01.17.
 */
public class AuthTokenRepositoryImpl implements AuthTokenRepository {
    private static final String DEF_VALUE = "";
    private static final String PREF_REFRESH_TOKEN = "pref_refresh_token";
    private static final String PREF_ACCESS_TOKEN = "pref_access_token";
    private static final String PREF_ROLE = "pref_role";
    private static final String PREF_LOGIN = "pref_login";

    private AuthContainer authContainer;
    private String login;
    private final SharedPreferences preferences;

    public AuthTokenRepositoryImpl(final Context context) {
        preferences = context.getSharedPreferences("mainPreferences", Context.MODE_PRIVATE);
    }

    @Override
    public AuthContainer getAuthInfo() {
        if( authContainer == null){
            final String accessToken = get(PREF_ACCESS_TOKEN);
            final String refreshToken = get(PREF_REFRESH_TOKEN);
            if(!StringUtils.isEmpty(refreshToken )){
                authContainer = new AuthContainer(accessToken, refreshToken);
            }
        }
        return authContainer;
    }

    @Override
    public void setAuthInfo(final AuthContainer authContainer) {
        put(PREF_ACCESS_TOKEN, authContainer.getAccessToken());
        put(PREF_REFRESH_TOKEN, authContainer.getRefreshToken());
        this.authContainer = authContainer;
    }

    @Override
    public void updateAccessToken(final String newAccessToken) {
        authContainer.setAccessToken(newAccessToken);
        put(PREF_ACCESS_TOKEN, newAccessToken);
    }

    @Override
    public void setCurrentLogin(final String login) {
        this.login = login;
        put(PREF_LOGIN, login);

    }

    @Override
    public String getLogin() {
        if(login == null){
            login = get(PREF_LOGIN);
        }
        return login;
    }

    private void put(final String name, final String val) {
        final SharedPreferences.Editor ed = preferences.edit();
        ed.putString(name, val);
        ed.apply();
    }

    private String get(final String name) {
        return preferences.getString(name, DEF_VALUE);
    }
}
