package com.alsash.reciper.data.cloud.request;

import com.alsash.reciper.data.cloud.response.GithubDbConfigResponse;

import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Headers;

import static com.alsash.reciper.app.AppContract.Cloud.Github.Db.CONFIG_ENDPOINT;
import static com.alsash.reciper.app.AppContract.Cloud.Github.Db.HEADER_ACCEPT;
import static com.alsash.reciper.app.AppContract.Cloud.Github.Db.HEADER_AGENT;

/**
 * A Request to Github REST service for cloud database, that persist in github repository
 * Documentation at https://developer.github.com/v3/
 */
public interface GithubDbRequest {
    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET(CONFIG_ENDPOINT)
    Maybe<GithubDbConfigResponse> getConfig();
}
