package com.grishberg.graphreporter.data.rest;

import com.grishberg.graphreporter.data.beans.AuthContainer;
import com.grishberg.graphreporter.data.beans.DailyValue;
import com.grishberg.graphreporter.data.beans.ProductItem;
import com.grishberg.graphreporter.data.beans.RefreshTokenContainer;
import com.grishberg.graphreporter.data.beans.common.RestResponse;

import java.util.List;

import retrofit2.http.GET;
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

    @POST("refreshToken")
    Observable<RestResponse<RefreshTokenContainer>> refreshToken(@Query("refreshToken") String refreshToken);

    //data

    @GET("values")
    Observable<RestResponse<List<DailyValue>>> getValues(@Query("accessToken") String accessToken,
                                                         @Query("productId") long productId,
                                                         @Query("offset") long offset,
                                                         @Query("limit") long limit);

    @GET("valuesFromDate")
    Observable<RestResponse<List<DailyValue>>> getValuesFromDate(@Query("accessToken") String accessToken,
                                                                 @Query("productId") long productId,
                                                                 @Query("startDt") long startDt,
                                                                 @Query("offset") long offset,
                                                                 @Query("limit") long limit);

    @GET("products")
    Observable<RestResponse<List<ProductItem>>> getProducts(@Query("accessToken") String accessToken,
                                                            @Query("categoryId") long categoryId);
}
