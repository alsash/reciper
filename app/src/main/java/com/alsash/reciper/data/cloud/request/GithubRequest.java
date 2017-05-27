package com.alsash.reciper.data.cloud.request;

import com.alsash.reciper.app.AppContract.Cloud.Github;
import com.alsash.reciper.data.cloud.response.JsonConfigResponse;

import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * A Request to Github REST service.
 * Documentation at https://developer.github.com/v3/
 */
public interface GithubRequest {
    @Headers({Github.HEADER_ACCEPT, Github.HEADER_AGENT})
    @GET(Github.RECIPER_JSON + "/config.json")
    Maybe<JsonConfigResponse> getConfig();
}
