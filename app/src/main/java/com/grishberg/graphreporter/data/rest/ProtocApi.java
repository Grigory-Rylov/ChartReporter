package com.grishberg.graphreporter.data.rest;

import com.grishberg.graphreporter.data.beans.DailyValueProtos;
import com.grishberg.graphreporter.data.beans.common.RestResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by grishberg on 21.03.17.
 */

public interface ProtocApi {
    @GET("valuesFromDate")
    Observable<DailyValueProtos.DailyValueContainer> getValuesFromDate(@Query("accessToken") String accessToken,
                                                                                     @Query("productId") long productId,
                                                                                     @Query("startDt") long startDt,
                                                                                     @Query("offset") long offset,
                                                                                     @Query("limit") long limit);
}
