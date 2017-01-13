package com.grishberg.graphreporter.data.services;

import com.grishberg.graphreporter.data.model.AuthContainer;
import com.grishberg.graphreporter.data.model.common.RestResponse;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by grishberg on 12.01.17.
 */

public interface Api {
    @POST("auth")
    Observable<RestResponse<AuthContainer>> login(@Query("login") String login,
                                                  @Query("password") CharSequence pass);
}
