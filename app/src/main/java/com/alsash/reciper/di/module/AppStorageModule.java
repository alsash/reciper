package com.alsash.reciper.di.module;

import android.content.Context;

import com.alsash.reciper.BuildConfig;
import com.alsash.reciper.app.AppContract;
import com.alsash.reciper.data.cloud.CloudManager;
import com.alsash.reciper.data.cloud.request.GithubRequest;
import com.alsash.reciper.data.cloud.request.UsdaRequest;
import com.alsash.reciper.data.db.DbManager;
import com.alsash.reciper.logic.StorageLogic;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * DI Module that provide the StorageLogic with their dependencies
 */
@Module
public abstract class AppStorageModule {

    @Provides
    @Singleton
    static StorageLogic provideStorageLogic(DbManager dbManager,
                                            CloudManager cloudManager) {
        return new StorageLogic(dbManager, cloudManager);
    }

    @Provides
    @Singleton
    static DbManager provideDbManager(Context context) {
        return new DbManager(context, AppContract.Db.DATABASE_NAME);
    }

    @Provides
    @Singleton
    static CloudManager provideCloudManager(Context context,
                                            GithubRequest githubRequest,
                                            UsdaRequest usdaRequest) {
        return new CloudManager(context, githubRequest, usdaRequest);
    }

    @Provides
    @Singleton
    static GithubRequest provideGithubRequest(OkHttpClient okHttpClient,
                                              @Named("github") Gson gson) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppContract.Cloud.Github.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(GithubRequest.class);
    }

    @Provides
    @Singleton
    static UsdaRequest provideUsdaRequest(OkHttpClient okHttpClient,
                                          @Named("usda") Gson gson) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppContract.Cloud.Usda.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(UsdaRequest.class);
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(Level.BODY));
        }
        builder.connectTimeout(AppContract.Cloud.CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .readTimeout(AppContract.Cloud.READ_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        return builder.build();
    }

    @Provides
    @Singleton
    @Named("github")
    public Gson provideGithubDeserializer() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    @Override
                    public Date deserialize(JsonElement json,
                                            Type typeOfT,
                                            JsonDeserializationContext context)
                            throws JsonParseException {
                        return (json == null) ? null : new Date(json.getAsLong() * 1000); // unix
                    }
                })
                .create();
    }

    @Provides
    @Singleton
    @Named("usda")
    public Gson provideUsdaDeserializer() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }
}
