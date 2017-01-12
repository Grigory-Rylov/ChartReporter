package com.grishberg.graphreporter.data.model;

import lombok.Getter;

/**
 * Created by grishberg on 11.01.17.
 */
@Getter
public class RefreshTokenContainer {
    private static final String TAG = RefreshTokenContainer.class.getSimpleName();
    private String accessToken;
    private long expires;
}
