package com.alsash.reciper.di.module;

import android.net.ConnectivityManager;

import com.alsash.reciper.app.AppContract;
import com.alsash.reciper.data.cloud.CloudManager;
import com.alsash.reciper.data.cloud.request.GithubDbRequest;
import com.alsash.reciper.data.cloud.request.UsdaRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * DI Module that provide the StorageLogic with their dependencies
 */
@Module
public abstract class AppDataCloudModule {

    @Provides
    @Singleton
    static CloudManager provideCloudManager(ConnectivityManager connectivityManager,
                                            GithubDbRequest githubDbRequest,
                                            UsdaRequest usdaRequest) {
        return new CloudManager(connectivityManager, githubDbRequest, usdaRequest);
    }

    @Provides
    @Singleton
    static GithubDbRequest provideGithubRequest(OkHttpClient okHttpClient,
                                                @Named("github_db") Gson gson) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppContract.Cloud.Github.Db.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(GithubDbRequest.class);
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
    @Named("github_db")
    static Gson provideGithubDbDeserializer() {
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
    static Gson provideUsdaDeserializer() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }
}
