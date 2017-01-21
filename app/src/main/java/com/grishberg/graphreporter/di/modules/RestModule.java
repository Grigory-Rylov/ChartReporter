package com.grishberg.graphreporter.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.grishberg.graphreporter.common.data.rest.RxErrorHandlingCallAdapterFactory;
import com.grishberg.graphreporter.common.data.rest.SoftErrorDelegate;
import com.grishberg.graphreporter.data.model.common.RestResponse;
import com.grishberg.graphreporter.data.repository.auth.AuthTokenRepository;
import com.grishberg.graphreporter.data.repository.values.DailyDataRepository;
import com.grishberg.graphreporter.data.repository.values.DailyDataRepositoryImpl;
import com.grishberg.graphreporter.data.repository.ProductsRepository;
import com.grishberg.graphreporter.data.repository.ProductsRepositoryImpl;
import com.grishberg.graphreporter.data.repository.values.DailyDataStorage;
import com.grishberg.graphreporter.data.rest.ErrorCheckerImpl;
import com.grishberg.graphreporter.data.rest.Api;
import com.grishberg.graphreporter.data.services.RefreshTokenService;
import com.grishberg.graphreporter.data.services.RefreshTokenServiceImpl;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by grishberg on 12.01.17.
 */
@Module
public class RestModule {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final String baseUrl;

    // Constructor needs one parameter to instantiate.
    public RestModule(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * interceptor для логирования
     *
     * @return HttpLoggingInterceptor
     */

    Interceptor provideInterceptor() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        // Register an adapter to manage the date types as long values
        gsonBuilder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));
        return gsonBuilder
                .setPrettyPrinting()
                .setDateFormat(DATE_PATTERN)
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(provideInterceptor())
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(final Gson gson,
                             final OkHttpClient okHttpClient,
                             final SoftErrorDelegate<RestResponse> softErrorDelegate) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(softErrorDelegate))
                .build();
    }

    @Provides
    @Singleton
    Api provideRetrofitService(final Retrofit retrofit) {
        return retrofit.create(Api.class);
    }

    @Provides
    @Singleton
    DailyDataRepository provideDataRepository(final Api api,
                                              final AuthTokenRepository tokenRepository,
                                              final DailyDataStorage storage) {
        return new DailyDataRepositoryImpl(tokenRepository, api, storage);
    }

    @Provides
    @Singleton
    ProductsRepository provideProductsRepository(final Api api, final AuthTokenRepository tokenRepository) {
        return new ProductsRepositoryImpl(tokenRepository, api);
    }

    @Provides
    @Singleton
    RefreshTokenService provideRefreshTokenServic(final Api api, final AuthTokenRepository tokenRepository) {
        return new RefreshTokenServiceImpl(tokenRepository, api);
    }

    @Provides
    @Singleton
    SoftErrorDelegate<RestResponse> provideSoftErrorDelegate() {
        return new ErrorCheckerImpl();
    }
}
