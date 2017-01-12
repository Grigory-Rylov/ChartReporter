package com.grishberg.graphreporter.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by grishberg on 11.01.17.
 */
@Getter
public class AuthContainer {
    private static final String TAG = AuthContainer.class.getSimpleName();

    private String refreshToken;

    private String accessToken;

    private long expires;

    private int role;
}
