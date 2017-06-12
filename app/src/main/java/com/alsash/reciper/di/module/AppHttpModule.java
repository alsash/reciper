package com.alsash.reciper.di.module;

import com.alsash.reciper.BuildConfig;
import com.alsash.reciper.app.AppContract;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * DI Module that provide the single OkHttp3Client instance
 */
@Module
public abstract class AppHttpModule {

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        builder.connectTimeout(AppContract.Cloud.CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .readTimeout(AppContract.Cloud.READ_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        return builder.build();
    }
}
